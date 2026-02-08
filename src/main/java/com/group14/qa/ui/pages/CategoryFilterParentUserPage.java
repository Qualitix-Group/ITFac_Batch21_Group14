package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

public class CategoryFilterParentUserPage extends PageObject {

    private final By parentDropdown = By.cssSelector("select[name='parentId']");
    private final By searchButton = By.cssSelector("button[type='submit']");
    private final By categoryRows = By.cssSelector("table tbody tr");

    public void selectParentCategory(String parentName) {
        WebElementFacade dropdown = find(parentDropdown);
        dropdown.selectByVisibleText(parentName);
    }

    public void clickSearch() {
        find(searchButton).click();
        waitForCondition().until(driver ->
                find(categoryRows).isVisible()
        );
    }

    public boolean areAllRowsFilteredByParent(String parentName) {
        return findAll(categoryRows).stream()
                .allMatch(row -> row.findBy(By.cssSelector("td:nth-child(3)"))
                        .getText().equals(parentName));
    }
}