package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

public class CategoryPage extends PageObject {

    private final By categoriesMenu =
            By.cssSelector(".sidebar a:nth-child(3)");

    private final By categoryTable =
            By.cssSelector("table");

    private final By pagination =
            By.cssSelector(".pagination");

    public void openCategoriesPage() {

        // Click Categories from sidebar
        find(categoriesMenu)
                .waitUntilClickable()
                .click();

        // Wait until navigation is completed
        waitForCondition().until(driver ->
                getDriver().getCurrentUrl().contains("/category")
                        || getDriver().getCurrentUrl().contains("/categories")
        );

        // Ensure table is visible
        find(categoryTable).waitUntilVisible();
    }

    public boolean areCategoriesDisplayed() {
        return findAll(categoryTable).size() > 0;
    }

    public boolean isPaginationDisplayed() {
        return findAll(pagination).size() > 0;
    }
}
