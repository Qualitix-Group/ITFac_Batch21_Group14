package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.Alert;

import java.time.Duration;
import java.util.List;

public class CategoryDeletePage extends PageObject {

    private final By categoriesMenu = By.cssSelector(".sidebar a:nth-child(3)");
    private final By deleteButton = By.xpath("//button[@title='Delete']");


    private final By successMessage = By.xpath("//div[@class='alert alert-success alert-dismissible fade show']");

    // Navigate to Categories Page
    public void openCategoriesPage() {
        find(categoriesMenu).waitUntilClickable().click();
        waitForCondition().until(driver ->
                getDriver().getCurrentUrl().contains("/category")
                        || getDriver().getCurrentUrl().contains("/categories")
        );
    }

    // Click delete button for the first category
    public void clickDeleteButtonForFirstCategory() {
        find(deleteButton).waitUntilClickable().click();
    }

    // Handle JS confirmation popup
    public void confirmDeletion() {
        try {
            Alert alert = getDriver().switchTo().alert();
            System.out.println("Alert says: " + alert.getText());
            alert.accept();
        } catch (Exception e) {
            System.out.println("No alert appeared: " + e.getMessage());
        }
    }


    public boolean waitForSuccessMessage() {
        int maxWaitSeconds = 10;
        int intervalMillis = 250;
        int elapsed = 0;

        while (elapsed < maxWaitSeconds * 1000) {
            try {
                List<WebElementFacade> messages = findAll(successMessage);
                if (!messages.isEmpty() && messages.get(0).isCurrentlyVisible()) {
                    System.out.println("|---- Success message displayed: " + messages.get(0).getText());
                    return true;
                }
            } catch (Exception e) {
                // ignore
            }

            try {
                Thread.sleep(intervalMillis);
            } catch (InterruptedException ignored) {}
            elapsed += intervalMillis;
        }

        System.out.println("|---- Success message not displayed after " + maxWaitSeconds + " seconds");
        return false;
    }
}
