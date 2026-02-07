package com.group14.qa.ui.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.ui.Select;

public class EditCategoryPage extends PageObject {

    // Page elements for Edit Category form
    @FindBy(id = "name")
    private WebElementFacade categoryNameInput;

    @FindBy(id = "parentId")
    private WebElementFacade parentCategoryDropdown;

    @FindBy(id = "description")
    private WebElementFacade descriptionInput;

    @FindBy(xpath = "//button[contains(text(),'Update') or contains(text(),'Save')]")
    private WebElementFacade updateButton;

    @FindBy(xpath = "//button[contains(text(),'Cancel')]")
    private WebElementFacade cancelButton;

    @FindBy(xpath = "//h3[contains(text(),'Edit Category')]")
    private WebElementFacade pageHeader;

    // Success and Error Messages
    @FindBy(css = ".alert.alert-success")
    private WebElementFacade successMessage;

    @FindBy(css = ".alert.alert-danger")
    private WebElementFacade errorAlertMessage;

    @FindBy(css = "#name + .text-danger, #name ~ .invalid-feedback")
    private WebElementFacade nameFieldError;

    @FindBy(css = ".text-danger, .invalid-feedback, .error-message")
    private WebElementFacade validationErrorMessage;

    // ============ METHODS ============

    public boolean isEditCategoryPageLoaded() {
        try {
            return pageHeader.isVisible() && categoryNameInput.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCategoryNameValue() {
        try {
            return categoryNameInput.getValue();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDescriptionValue() {
        try {
            return descriptionInput.getValue();
        } catch (Exception e) {
            return "";
        }
    }

    public String getSelectedParentCategory() {
        try {
            Select select = new Select(parentCategoryDropdown);
            return select.getFirstSelectedOption().getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void enterCategoryName(String categoryName) {
        categoryNameInput.waitUntilVisible().clear();
        categoryNameInput.type(categoryName);
    }

    public void clearCategoryName() {
        categoryNameInput.waitUntilVisible().clear();
    }

    public void enterDescription(String description) {
        descriptionInput.waitUntilVisible().clear();
        descriptionInput.type(description);
    }

    public void selectParentCategory(String parentCategoryName) {
        try {
            Select parentSelect = new Select(parentCategoryDropdown);

            if (parentCategoryName.isEmpty() || parentCategoryName.equals("Main Category")) {
                parentSelect.selectByIndex(0);
            } else {
                parentSelect.selectByVisibleText(parentCategoryName);
            }
        } catch (Exception e) {
            System.out.println("Error selecting parent category: " + e.getMessage());
        }
    }

    public void clickUpdateButton() {
        updateButton.click();
    }

    public void clickCancelButton() {
        cancelButton.click();
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

    public boolean isValidationErrorDisplayed() {
        try {
            return errorAlertMessage.isVisible() ||
                    validationErrorMessage.isVisible() ||
                    nameFieldError.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getValidationErrorMessage() {
        try {
            if (nameFieldError.isDisplayed()) {
                return nameFieldError.getText();
            }
            if (validationErrorMessage.isDisplayed()) {
                return validationErrorMessage.getText();
            }
            if (errorAlertMessage.isDisplayed()) {
                return errorAlertMessage.getText();
            }
            return "No error message found";
        } catch (Exception e) {
            return "Error retrieving message: " + e.getMessage();
        }
    }

    public boolean isNameFieldInErrorState() {
        try {
            String classValue = categoryNameInput.getAttribute("class");
            return classValue != null &&
                    (classValue.contains("is-invalid") ||
                            classValue.contains("error") ||
                            classValue.contains("invalid"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUpdateButtonEnabled() {
        try {
            return updateButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
}