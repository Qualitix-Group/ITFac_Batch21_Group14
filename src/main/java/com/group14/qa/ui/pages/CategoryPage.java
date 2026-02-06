package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

public class CategoryPage extends PageObject {

    private final By categoriesMenu =
            By.cssSelector(".sidebar a:nth-child(3)");

    private final By categoryTable =
            By.cssSelector("table");



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


}
