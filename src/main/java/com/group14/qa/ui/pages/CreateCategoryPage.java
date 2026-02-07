//package com.group14.qa.ui.pages;
//
//import net.serenitybdd.core.annotations.findby.FindBy;
//import net.serenitybdd.core.pages.PageObject;
//import net.serenitybdd.core.pages.WebElementFacade;
//import org.openqa.selenium.support.ui.Select;
//
//public class CreateCategoryPage extends PageObject {
//
//    // Page elements for Create Category form
//    @FindBy(id = "name")
//    private WebElementFacade categoryNameInput;
//
//    @FindBy(id = "parentId")
//    private WebElementFacade parentCategoryDropdown;
//
//    @FindBy(xpath = "//button[contains(text(),'Save')]")
//    private WebElementFacade saveButton;
//
//    @FindBy(xpath = "//button[contains(text(),'Cancel')]")
//    private WebElementFacade cancelButton;
//
//    @FindBy(xpath = "//h3[contains(text(),'Edit Category') or contains(text(),'Create Category') or contains(text(),'Add Category')]")
//    private WebElementFacade pageHeader;
//
//    @FindBy(className = "alert-success")
//    private WebElementFacade successMessage;
//
//    // Navigation elements
//    @FindBy(xpath = "//a[contains(text(),'Add A Category') or contains(@href,'/ui/categories/create')]")
//    private WebElementFacade addCategoryButton;
//
//    // Methods
//    public void enterCategoryName(String categoryName) {
//        categoryNameInput.waitUntilVisible().type(categoryName);
//    }
//
//    public void selectParentCategory(String parentCategoryName) {
//        Select parentSelect = new Select(parentCategoryDropdown);
//
//        if (parentCategoryName.isEmpty() || parentCategoryName.equals("Main Category")) {
//            parentSelect.selectByIndex(0); // Select "Main Category"
//        } else {
//            // Try to select by visible text
//            parentSelect.selectByVisibleText(parentCategoryName);
//        }
//    }
//
//    public void clickSaveButton() {
//        saveButton.click();
//    }
//
//    public void clickCancelButton() {
//        cancelButton.click();
//    }
//
//    public boolean isCreateCategoryPageLoaded() {
//        return pageHeader.isVisible() && categoryNameInput.isVisible();
//    }
//
//    public boolean isSuccessMessageDisplayed() {
//        try {
//            return successMessage.isVisible();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String getSuccessMessageText() {
//        return successMessage.getText();
//    }
//
//    public void clickAddCategoryButton() {
//        addCategoryButton.waitUntilVisible().click();
//    }
//
//    // Method to get current category name value
//    public String getCategoryNameValue() {
//        return categoryNameInput.getValue();
//    }
//
//    // Method to get selected parent category
//    public String getSelectedParentCategory() {
//        Select select = new Select(parentCategoryDropdown);
//        return select.getFirstSelectedOption().getText();
//    }
//
//    // Add these methods to your existing CreateCategoryPage.java
//
//    public String getValidationErrorMessage() {
//        try {
//            // Try different error message locations
//            if (nameFieldError.isDisplayed()) {
//                return nameFieldError.getText();
//            }
//            if (validationErrorMessage.isDisplayed()) {
//                return validationErrorMessage.getText();
//            }
//            if (errorAlertMessage.isDisplayed()) {
//                return errorAlertMessage.getText();
//            }
//
//            // Try to find any error message element
//            return getDriver().findElement(
//                    org.openqa.selenium.By.cssSelector(".text-danger, .invalid-feedback, .error-message")
//            ).getText();
//        } catch (Exception e) {
//            return "No error message found";
//        }
//    }
//
//    public boolean isNameFieldInErrorState() {
//        try {
//            String classValue = categoryNameInput.getAttribute("class");
//            return classValue != null &&
//                    (classValue.contains("is-invalid") ||
//                            classValue.contains("error") ||
//                            classValue.contains("invalid"));
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public boolean isValidationErrorDisplayed() {
//        try {
//            return errorAlertMessage.isDisplayed() ||
//                    validationErrorMessage.isDisplayed() ||
//                    nameFieldError.isDisplayed();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//}


package com.group14.qa.ui.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.ui.Select;

public class CreateCategoryPage extends PageObject {

    // Page elements for Create Category form
    @FindBy(id = "name")
    private WebElementFacade categoryNameInput;

    @FindBy(id = "parentId")
    private WebElementFacade parentCategoryDropdown;

    @FindBy(xpath = "//button[contains(text(),'Save')]")
    private WebElementFacade saveButton;

    @FindBy(xpath = "//button[contains(text(),'Cancel')]")
    private WebElementFacade cancelButton;

    @FindBy(xpath = "//h3[contains(text(),'Edit Category') or contains(text(),'Create Category') or contains(text(),'Add Category')]")
    private WebElementFacade pageHeader;

    // Success and Error Messages
    @FindBy(className = "alert-success")
    private WebElementFacade successMessage;

    // === ADD THESE ERROR MESSAGE LOCATORS ===
    @FindBy(css = ".alert.alert-danger")
    private WebElementFacade errorAlertMessage;

    @FindBy(css = ".text-danger, .invalid-feedback, .error-message")
    private WebElementFacade validationErrorMessage;

    @FindBy(css = "#name + .text-danger")
    private WebElementFacade nameFieldError;

    @FindBy(css = "#name ~ .invalid-feedback")
    private WebElementFacade nameFieldInvalidFeedback;

    @FindBy(css = ".error, .validation-error")
    private WebElementFacade genericErrorMessage;

    // Navigation elements
    @FindBy(xpath = "//a[contains(text(),'Add A Category') or contains(@href,'/ui/categories/create')]")
    private WebElementFacade addCategoryButton;

    // Methods
    public void enterCategoryName(String categoryName) {
        categoryNameInput.waitUntilVisible().type(categoryName);
    }

    public void selectParentCategory(String parentCategoryName) {
        Select parentSelect = new Select(parentCategoryDropdown);

        if (parentCategoryName.isEmpty() || parentCategoryName.equals("Main Category")) {
            parentSelect.selectByIndex(0); // Select "Main Category"
        } else {
            // Try to select by visible text
            parentSelect.selectByVisibleText(parentCategoryName);
        }
    }

    public void clickSaveButton() {
        saveButton.click();
    }

    public void clickCancelButton() {
        cancelButton.click();
    }

    public boolean isCreateCategoryPageLoaded() {
        try {
            return pageHeader.isVisible() && categoryNameInput.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return successMessage.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessageText() {
        try {
            return successMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickAddCategoryButton() {
        addCategoryButton.waitUntilVisible().click();
    }

    // Method to get current category name value
    public String getCategoryNameValue() {
        try {
            return categoryNameInput.getValue();
        } catch (Exception e) {
            return "";
        }
    }

    // Method to get selected parent category
    public String getSelectedParentCategory() {
        try {
            Select select = new Select(parentCategoryDropdown);
            return select.getFirstSelectedOption().getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ============ ERROR VALIDATION METHODS ============

    public String getValidationErrorMessage() {
        try {
            // Try different error message locations in order of specificity
            if (nameFieldError.isDisplayed()) {
                return nameFieldError.getText();
            }
            if (nameFieldInvalidFeedback.isDisplayed()) {
                return nameFieldInvalidFeedback.getText();
            }
            if (validationErrorMessage.isDisplayed()) {
                return validationErrorMessage.getText();
            }
            if (errorAlertMessage.isDisplayed()) {
                return errorAlertMessage.getText();
            }
            if (genericErrorMessage.isDisplayed()) {
                return genericErrorMessage.getText();
            }

            // Try to find any error message element
            return getDriver().findElement(
                    org.openqa.selenium.By.cssSelector(".text-danger, .invalid-feedback, .error-message, .alert-danger, .error")
            ).getText();
        } catch (Exception e) {
            return "No error message found";
        }
    }

    public boolean isNameFieldInErrorState() {
        try {
            String classValue = categoryNameInput.getAttribute("class");
            return classValue != null &&
                    (classValue.contains("is-invalid") ||
                            classValue.contains("error") ||
                            classValue.contains("invalid") ||
                            classValue.contains("ng-invalid"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidationErrorDisplayed() {
        try {
            // Check all possible error message locations
            return errorAlertMessage.isDisplayed() ||
                    validationErrorMessage.isDisplayed() ||
                    nameFieldError.isDisplayed() ||
                    nameFieldInvalidFeedback.isDisplayed() ||
                    genericErrorMessage.isDisplayed() ||
                    isNameFieldInErrorState();
        } catch (Exception e) {
            return false;
        }
    }

    // Additional helper method to check for specific error text
    public boolean containsErrorText(String expectedError) {
        try {
            String errorMessage = getValidationErrorMessage().toLowerCase();
            return errorMessage.contains(expectedError.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    // Check if save button is enabled/disabled
    public boolean isSaveButtonEnabled() {
        try {
            return saveButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Get the error message text (simplified version)
    public String getErrorMessageText() {
        try {
            // Try to get error from page source first
            String pageSource = getDriver().getPageSource().toLowerCase();

            // Return first error message found
            return getValidationErrorMessage();
        } catch (Exception e) {
            return "";
        }
    }
}