package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryDeleteButtonView extends PageObject {

    // Locator for all Delete buttons in the category table
    private By deleteButtons = By.xpath("//tbody/tr/td[4]//button");

    // Navigate to Category page
    public void openCategoryPage() {
        // Replace with your actual URL if not using @DefaultUrl
        getDriver().get("http://localhost:8080/ui/categories");
    }

    // Verify all Delete buttons are visible and clickable
    public void verifyAllDeleteButtonsVisibleAndClickable() {
        List<WebElementFacade> buttons = findAll(deleteButtons);
        assertThat(buttons.size()).isGreaterThan(0); // There should be at least one button

        for (WebElementFacade button : buttons) {
            assertThat(button.isVisible()).isTrue();  // Check visibility
            assertThat(button.isEnabled()).isTrue();  // Check if clickable
        }
    }
}
