package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

public class PlantsAddEditPage extends PageObject {

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

    // Validation messages
    @FindBy(css = "#name + .text-danger, #name ~ .invalid-feedback, .field-error")
    private WebElementFacade nameErrorMessage;

    @FindBy(css = "#price + .text-danger, #price ~ .invalid-feedback, .field-error")
    private WebElementFacade priceErrorMessage;

    // ---------- Plant List Table ----------
    @FindBy(css = "table tbody tr")
    private List<WebElementFacade> plantRows;

    @FindBy(css = "table tbody tr td:nth-child(1)")
    private List<WebElementFacade> plantNameCells;

    // ---------- Pagination ----------
    @FindBy(css = "ul.pagination")
    private WebElementFacade paginationContainer;

    @FindBy(xpath = "//li[not(contains(@class,'disabled'))]/a[normalize-space()='Next']")
    private WebElementFacade nextPageButton;

    @FindBy(xpath = "//li[not(contains(@class,'disabled'))]/a[normalize-space()='Previous']")
    private WebElementFacade previousPageButton;

    // ---------- Empty State ----------
    @FindBy(xpath = "//table//tbody//td[contains(normalize-space(),'No plants found')]")
    private WebElementFacade emptyPlantsMessage;

    // =====================================================
    // 🔹 NAVIGATION ACTIONS
    // =====================================================

    public void openPlantsTab() {
        plantsTab.click();
    }

    public void clickAddPlant() {
        addPlantButton.waitUntilClickable().click();
    }

    // =====================================================
    // 🔹 ADD / EDIT PLANT
    // =====================================================

    public void enterPlantName(String name) {
        plantNameField.type(name);
    }

    public void clearPlantName() {
        plantNameField.clear();
    }

    public void selectCategoryByVisibleText(String categoryName) {
        new Select(categoryDropdown).selectByVisibleText(categoryName);
    }

    public void enterPrice(String price) {
        priceField.type(price);
    }

    public void clearPrice() {
        priceField.clear();
    }

    public void enterQuantity(String quantity) {
        quantityField.type(quantity);
    }

    public void clickSave() {
        saveButton.click();
    }

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
    // 🔹 VALIDATIONS
    // =====================================================

    public boolean isPlantDisplayedInList(String plantName) {
        return plantRows.stream().anyMatch(row -> row.getText().contains(plantName));
    }

    public boolean isNameValidationMessageDisplayed(String expectedText) {
        try {
            nameErrorMessage.waitUntilVisible();
            return nameErrorMessage.getText().toLowerCase().contains(expectedText.toLowerCase());
        } catch (Exception e) { return false; }
    }

    public boolean isPriceValidationMessageDisplayed(String expectedText) {
        try {
            priceErrorMessage.waitUntilVisible();
            return priceErrorMessage.getText().toLowerCase().contains(expectedText.toLowerCase());
        } catch (Exception e) { return false; }
    }

    // =====================================================
    // 🔹 LOW STOCK CHECK
    // =====================================================

    public boolean isAnyPlantWithLowQuantityPresent() {
        for (WebElementFacade row : plantRows) {
            try {
                int qty = Integer.parseInt(row.then(By.xpath("./td[4]/span[1]")).getText().trim());
                if (qty < 5) return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    public boolean isLowBadgeDisplayedForLowStockPlant() {
        for (WebElementFacade row : plantRows) {
            try {
                WebElementFacade cell = row.then(By.xpath("./td[4]"));
                int qty = Integer.parseInt(cell.then(By.xpath("./span[1]")).getText().trim());
                if (qty < 5) {
                    return cell.then(By.xpath(".//span[contains(@class,'badge') and contains(text(),'Low')]")).isDisplayed();
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    public boolean isLowBadgeAbsentForSufficientStockPlant() {
        for (WebElementFacade row : plantRows) {
            try {
                WebElementFacade cell = row.then(By.xpath("./td[4]"));
                int qty = Integer.parseInt(cell.then(By.xpath("./span[1]")).getText().trim());
                if (qty >= 5) {
                    return cell.thenFindAll(By.xpath(".//span[contains(@class,'badge') and contains(text(),'Low')]")).isEmpty();
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    // =====================================================
    // 🔹 PAGINATION VISIBILITY
    // =====================================================

    public boolean isPaginationVisible() {
        try {
            return paginationContainer.waitUntilVisible().isDisplayed();
        } catch (Exception e) { return false; }
    }

    // =====================================================
    // 🔹 PAGINATION NAVIGATION (TC_UI_USER_PLANT_012)
    // =====================================================

    public String getFirstPlantName() {
        waitForCondition().until(driver -> !plantNameCells.isEmpty());
        return plantNameCells.get(0).getText().trim();
    }

    public List<String> getPlantNamesFromCurrentPage() {
        return plantNameCells.stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    public void clickNextPage() {
        String before = getFirstPlantName();
        nextPageButton.waitUntilClickable().click();
        waitForCondition().until(driver -> !getFirstPlantName().equals(before));
    }

    public void clickPreviousPage() {
        String before = getFirstPlantName();
        previousPageButton.waitUntilClickable().click();
        waitForCondition().until(driver -> !getFirstPlantName().equals(before));
    }

    // =====================================================
    // 🔹 EMPTY LIST CHECK
    // =====================================================

    public boolean isEmptyPlantListMessageDisplayed() {
        try {
            return emptyPlantsMessage.waitUntilVisible().isDisplayed();
        } catch (Exception e) { return false; }
    }

    public String getEmptyPlantListMessage() {
        try {
            return emptyPlantsMessage.getText().trim();
        } catch (Exception e) { return ""; }
    }

    public boolean isPlantTableEmpty() {
        return plantRows.stream()
                .noneMatch(row ->
                        !row.getText().trim().equalsIgnoreCase("No plants found")
                                && !row.getText().trim().isEmpty()
                );
    }
}
