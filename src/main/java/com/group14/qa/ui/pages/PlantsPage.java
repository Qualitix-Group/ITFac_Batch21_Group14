package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;
import org.openqa.selenium.By;

import java.util.List;

public class PlantsPage extends PageObject {

    // ---------- Navigation ----------
    @FindBy(linkText = "Plants")
    private WebElementFacade plantsTab;

    @FindBy(css = "a[href='/ui/plants/add']")
    private WebElementFacade addPlantButton;

    // ---------- Add Plant Form ----------
    @FindBy(id = "name")
    private WebElementFacade plantNameField;

    @FindBy(id = "categoryId")
    private WebElementFacade categoryDropdown;

    @FindBy(id = "price")
    private WebElementFacade priceField;

    @FindBy(id = "quantity")
    private WebElementFacade quantityField;

    @FindBy(css = "button.btn.btn-primary")
    private WebElementFacade saveButton;

    // 🔴 Name validation error message
    @FindBy(css = "#name + .text-danger, #name ~ .invalid-feedback, .field-error")
    private WebElementFacade nameErrorMessage;

    // 🔴 Price validation error message
    @FindBy(css = "#price + .text-danger, #price ~ .invalid-feedback, .field-error")
    private WebElementFacade priceErrorMessage;

    // ---------- Plant List Table ----------
    @FindBy(css = "table tbody tr")
    private List<WebElementFacade> plantRows;

    // ---------- Admin Actions ----------
    public void openPlantsTab() {
        plantsTab.click();
    }

    public void clickAddPlant() {
        addPlantButton.waitUntilClickable().click();
    }

    public void enterPlantName(String name) {
        plantNameField.type(name);
    }

    public void clearPlantName() {
        plantNameField.clear();
    }

    public void selectCategoryByVisibleText(String categoryName) {
        Select select = new Select(categoryDropdown);
        select.selectByVisibleText(categoryName);
    }

    public void enterPrice(String price) {
        priceField.type(price);
    }

    public void enterQuantity(String quantity) {
        quantityField.type(quantity);
    }

    public void clickSave() {
        saveButton.click();
    }

    public void clearPrice() {
        priceField.clear();
    }

    // ---------- Validations ----------
    public boolean isPlantDisplayedInList(String plantName) {
        return plantRows.stream()
                .anyMatch(row -> row.getText().contains(plantName));
    }

    public boolean isNameErrorDisplayed() {
        try {
            return nameErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getNameErrorMessage() {
        return nameErrorMessage.getText();
    }

    public boolean isNameValidationMessageDisplayed(String expectedText) {
        try {
            nameErrorMessage.waitUntilVisible();
            return nameErrorMessage.getText().toLowerCase()
                    .contains(expectedText.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPriceValidationMessageDisplayed(String expectedText) {
        try {
            priceErrorMessage.waitUntilVisible();
            return priceErrorMessage.getText().toLowerCase()
                    .contains(expectedText.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    // ================= EDIT PLANT =================

    public void clickEditForPlant(String plantName) {
        WebElementFacade editButton = find(By.xpath(
                "//table//tr[td[1][contains(text(),'" + plantName + "')]]//a[contains(@href,'/edit')]"
        ));
        editButton.waitUntilClickable().click();
    }

    public void updatePlantName(String newName) {
        plantNameField.clear();
        plantNameField.type(newName);
    }

    public void updatePrice(String price) {
        priceField.clear();
        priceField.type(price);
    }

    public void updateQuantity(String qty) {
        quantityField.clear();
        quantityField.type(qty);
    }

    public void saveUpdatedPlant() {
        saveButton.click();
        waitFor("//table//tbody//tr");
    }

// =====================================================
    // 🔹 USER LOW STOCK CHECK (TC_UI_USER_PLANT_008)
    // =====================================================

    /**
     * Check if any plant has quantity less than 5
     */
    public boolean isAnyPlantWithLowQuantityPresent() {
        for (WebElementFacade row : plantRows) {
            try {
                WebElementFacade qtyElement = row.then(By.xpath("./td[4]/span[1]"));
                int qty = Integer.parseInt(qtyElement.getText().trim());
                if (qty < 5) {
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    /**
     * Verify LOW badge appears next to quantity < 5
     */
    public boolean isLowBadgeDisplayedForLowStockPlant() {
        for (WebElementFacade row : plantRows) {
            try {
                WebElementFacade quantityCell = row.then(By.xpath("./td[4]"));
                WebElementFacade qtySpan = quantityCell.then(By.xpath("./span[1]"));
                int qty = Integer.parseInt(qtySpan.getText().trim());

                if (qty < 5) {
                    WebElementFacade lowBadge = quantityCell.then(
                            By.xpath(".//span[contains(@class,'badge') and contains(text(),'Low')]")
                    );
                    return lowBadge.isDisplayed();
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    /**
     * Verify that plants with quantity >= 5 do NOT show Low badge
     */
    public boolean isLowBadgeAbsentForSufficientStockPlant() {
        for (WebElementFacade row : plantRows) {
            try {
                WebElementFacade quantityCell = row.then(By.xpath("./td[4]"));
                WebElementFacade qtySpan = quantityCell.then(By.xpath("./span[1]"));
                int qty = Integer.parseInt(qtySpan.getText().trim());

                if (qty >= 5) {
                    List<WebElementFacade> lowBadges = quantityCell.thenFindAll(
                            By.xpath(".//span[contains(@class,'badge') and contains(text(),'Low')]")
                    );
                    return lowBadges.isEmpty(); // badge should NOT exist
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    // =====================================================
// 🔹 PAGINATION CHECK (USER)
// =====================================================

    @FindBy(css = "ul.pagination")
    private WebElementFacade paginationContainer;

    /**
     * Verify pagination controls are visible
     */
    public boolean isPaginationVisible() {
        try {
            return paginationContainer.waitUntilVisible().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }



}
