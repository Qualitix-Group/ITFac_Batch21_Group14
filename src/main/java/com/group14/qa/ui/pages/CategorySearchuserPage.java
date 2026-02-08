package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

import java.util.List;

public class CategorySearchuserPage extends PageObject {

    private final By categoryRows = By.cssSelector("table tbody tr");
    private final By categoryNameCell = By.cssSelector("td:nth-child(2)");
    private final By searchInput = By.cssSelector("input[name='name']");
    private final By searchButton = By.cssSelector("button[type='submit']");

    private String searchedCategory;

    // Get first category from table
    public String getFirstCategoryName() {
        find(categoryRows).waitUntilVisible();
        List<WebElementFacade> rows = findAll(categoryRows);
        if (rows.isEmpty()) {
            return null;
        }
        return rows.get(0).find(categoryNameCell).getText();
    }

    // Perform search
    public void searchCategory(String name) {
        searchedCategory = name;
        find(searchInput).clear();
        find(searchInput).type(name);
        find(searchButton).click();
        waitForCondition().until(driver ->
                findAll(categoryRows).size() > 0
        );
    }

    // Check if searched category is displayed in results
    public boolean isSearchedCategoryDisplayed() {
        for (WebElementFacade row : findAll(categoryRows)) {
            String name = row.find(categoryNameCell).getText();
            if (name.equalsIgnoreCase(searchedCategory)) {
                return true;
            }
        }
        return false;
    }

    // Getter for searchedCategory
    public String getSearchedCategory() {
        return searchedCategory;
    }
}
