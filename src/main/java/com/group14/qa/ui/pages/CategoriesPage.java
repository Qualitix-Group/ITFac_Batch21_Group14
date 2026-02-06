//package com.group14.qa.ui.pages;
//
//import net.serenitybdd.core.pages.PageObject;
//import net.serenitybdd.core.annotations.findby.FindBy;
//import net.serenitybdd.core.pages.WebElementFacade;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.By;
//import java.time.Duration;
//import java.util.List;
//
//public class CategoriesPage extends PageObject {
//
//    // ---------- Navigation ----------
//    @FindBy(linkText = "Categories")
//    private WebElementFacade categoriesTab;
//
//    @FindBy(css = "a[href='/ui/categories/add'], a.btn-primary[href*='add']")
//    private WebElementFacade addCategoryButton;
//
//
//    // ---------- Add Category Form ----------
//    @FindBy(id = "name")
//    private WebElementFacade categoryNameField;
//
//    @FindBy(id = "description")
//    private WebElementFacade descriptionField;
//
//    @FindBy(css = "button.btn.btn-primary[type='submit']")
//    private WebElementFacade saveButton;
//
//    // ---------- Validation Messages ----------
//    @FindBy(css = "#name + .text-danger, #name ~ .invalid-feedback")
//    private WebElementFacade nameErrorMessage;
//
//    @FindBy(css = "#description + .text-danger, #description ~ .invalid-feedback")
//    private WebElementFacade descriptionErrorMessage;
//
//    // ---------- Category List Table ----------
//    @FindBy(css = "table tbody tr")
//    private List<WebElementFacade> categoryRows;
//
//    @FindBy(css = "h1, h2, h3")
//    private List<WebElementFacade> pageHeaders;
//
//    // ---------- Success/Error Messages ----------
//    @FindBy(css = ".alert.alert-success")
//    private WebElementFacade successMessage;
//
//    @FindBy(css = ".alert.alert-danger")
//    private WebElementFacade errorMessage;
//
//    // ---------- Navigation Methods ----------
//    public void openCategoriesTab() {
//        categoriesTab.waitUntilClickable().click();
//        waitForPageToLoad();
//    }
//
//    public void clickAddCategory() {
//        addCategoryButton.waitUntilClickable().click();
//        waitFor(2000).milliseconds();
//    }
//
//    // ---------- Add Category Form Methods ----------
//    public void enterCategoryName(String name) {
//        categoryNameField.type(name);
//    }
//
//    public void clearCategoryName() {
//        categoryNameField.clear();
//    }
//
//    public void enterDescription(String description) {
//        descriptionField.type(description);
//    }
//
//    public void clearDescription() {
//        descriptionField.clear();
//    }
//
//    public void clickSave() {
//        saveButton.click();
//        waitFor(2000).milliseconds();
//    }
//
//    // ---------- Page Loading ----------
//    public void waitForPageToLoad() {
//        waitFor(5000).milliseconds();
//        // Wait for either header or table to appear
//        waitFor("//h1[contains(text(),'Categories')] | //table");
//    }
//
//    public boolean isCategoriesPageLoaded() {
//        try {
//            String currentUrl = getDriver().getCurrentUrl();
//            return currentUrl.contains("/categories") ||
//                    !categoryRows.isEmpty() ||
//                    pageHeaders.stream().anyMatch(header ->
//                            header.getText().contains("Categories"));
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // ---------- List View Methods ----------
//    public boolean isCategoryDisplayedInList(String categoryName) {
//        return categoryRows.stream()
//                .anyMatch(row -> row.getText().contains(categoryName));
//    }
//
//    public int getCategoryCount() {
//        return categoryRows.size();
//    }
//
//    // ---------- Validation Methods ----------
//    public boolean isNameErrorDisplayed() {
//        try {
//            return nameErrorMessage.isDisplayed();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String getNameErrorMessage() {
//        return nameErrorMessage.getText();
//    }
//
//    public boolean isNameValidationMessageDisplayed(String expectedText) {
//        try {
//            nameErrorMessage.waitUntilVisible();
//            return nameErrorMessage.getText().toLowerCase()
//                    .contains(expectedText.toLowerCase());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public boolean isDescriptionValidationMessageDisplayed(String expectedText) {
//        try {
//            descriptionErrorMessage.waitUntilVisible();
//            return descriptionErrorMessage.getText().toLowerCase()
//                    .contains(expectedText.toLowerCase());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // ---------- Success/Error Message Methods ----------
//    public boolean isSuccessMessageDisplayed() {
//        try {
//            return successMessage.isDisplayed();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String getSuccessMessage() {
//        return successMessage.getText();
//    }
//
//    public boolean isErrorMessageDisplayed() {
//        try {
//            return errorMessage.isDisplayed();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String getErrorMessage() {
//        return errorMessage.getText();
//    }
//
//    // ---------- Edit Category Methods ----------
//    public void clickEditForCategory(String categoryName) {
//        WebElementFacade editButton = find(By.xpath(
//                "//table//tr[td[1][contains(text(),'" + categoryName + "')]]//a[contains(@href,'/edit')]"
//        ));
//        editButton.waitUntilClickable().click();
//        waitFor(2000).milliseconds();
//    }
//
//    public void updateCategoryName(String newName) {
//        categoryNameField.clear();
//        categoryNameField.type(newName);
//    }
//
//    public void updateDescription(String description) {
//        descriptionField.clear();
//        descriptionField.type(description);
//    }
//
//    public void saveUpdatedCategory() {
//        saveButton.click();
//        waitFor(3000).milliseconds();
//    }
//
//    // ---------- Delete Category Methods ----------
//    public void clickDeleteForCategory(String categoryName) {
//        WebElementFacade deleteButton = find(By.xpath(
//                "//table//tr[td[1][contains(text(),'" + categoryName + "')]]//button[contains(@class,'btn-danger')]"
//        ));
//        deleteButton.waitUntilClickable().click();
//        waitFor(2000).milliseconds();
//    }
//
//    // ---------- Confirmation Dialog ----------
//    @FindBy(css = ".modal-footer .btn-danger")
//    private WebElementFacade confirmDeleteButton;
//
//    public void confirmDelete() {
//        confirmDeleteButton.waitUntilClickable().click();
//        waitFor(3000).milliseconds();
//    }
//
//    // ---------- Button Visibility Methods (for your test case) ----------
//    public boolean isAddButtonVisible() {
//        try {
//            return addCategoryButton.isVisible();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public boolean isAddButtonEnabled() {
//        try {
//            return addCategoryButton.isEnabled();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // ---------- Search/Sort Methods ----------
//    @FindBy(id = "searchInput")
//    private WebElementFacade searchInput;
//
//    @FindBy(css = "th.sortable")
//    private List<WebElementFacade> sortableHeaders;
//
//    public void searchCategory(String searchTerm) {
//        searchInput.type(searchTerm);
//        waitFor(2000).milliseconds();
//    }
//
//    public void clickSortHeader(String headerName) {
//        for (WebElementFacade header : sortableHeaders) {
//            if (header.getText().contains(headerName)) {
//                header.click();
//                waitFor(2000).milliseconds();
//                break;
//            }
//        }
//    }
//
//    // ---------- Current URL ----------
//    public String getCurrentUrl() {
//        return getDriver().getCurrentUrl();
//    }
//
//    public String getPageTitle() {
//        return getDriver().getTitle();
//    }
//
//    // ---------- Empty State ----------
//    public boolean isEmptyStateDisplayed() {
//        try {
//            return find(By.xpath("//td[contains(text(),'No categories found')]")).isDisplayed() ||
//                    find(By.xpath("//div[contains(text(),'No data available')]")).isDisplayed();
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}

package com.group14.qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;
import java.util.List;

public class CategoriesPage extends PageObject {

    // ---------- Navigation ----------
    @FindBy(linkText = "Categories")
    private WebElementFacade categoriesTab;

    @FindBy(css = "a[href='/ui/categories/add'], a.btn-primary[href*='add']")
    private WebElementFacade addCategoryButton;

    // ---------- Edit Button Locators ----------
    @FindBy(css = "a[href*='/edit'] i.fa-edit, a[href*='/edit'] svg, button.edit-btn, a.edit-link")
    private List<WebElementFacade> editButtons;

    @FindBy(xpath = "//a[contains(@href,'/edit')]//i[contains(@class,'edit')] | //a[contains(@href,'/edit')]")
    private List<WebElementFacade> editIcons;

    // ---------- Category List Table ----------
    @FindBy(css = "table tbody tr")
    private List<WebElementFacade> categoryRows;

    @FindBy(css = "h1, h2, h3")
    private List<WebElementFacade> pageHeaders;

    // ---------- Empty State ----------
    @FindBy(xpath = "//td[contains(text(),'No categories')] | //div[contains(text(),'No data')] | //div[contains(text(),'No categories')]")
    private WebElementFacade emptyStateMessage;

    @FindBy(css = ".empty-state, .no-data, .dataTables_empty")
    private WebElementFacade emptyStateContainer;

    // ---------- Navigation Methods ----------
    public void openCategoriesTab() {
        categoriesTab.waitUntilClickable().click();
        waitForPageToLoad();
    }

    public void clickAddCategory() {
        addCategoryButton.waitUntilClickable().click();
        waitFor(2000).milliseconds();
    }

    // ---------- Page Loading ----------
    public void waitForPageToLoad() {
        waitFor(5000).milliseconds();
        // Wait for either header or table to appear
        waitFor("//h1[contains(text(),'Categories')] | //table");
    }

    public boolean isCategoriesPageLoaded() {
        try {
            String currentUrl = getDriver().getCurrentUrl();
            return currentUrl.contains("/categories") ||
                    !categoryRows.isEmpty() ||
                    pageHeaders.stream().anyMatch(header ->
                            header.getText().contains("Categories"));
        } catch (Exception e) {
            return false;
        }
    }

    // ---------- Add Button Methods ----------
    public boolean isAddButtonVisible() {
        try {
            return addCategoryButton.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAddButtonEnabled() {
        try {
            return addCategoryButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // ---------- Edit Button Methods ----------
    public boolean isEditButtonVisibleForCategory(String categoryName) {
        try {
            // Find the row for the category
            for (WebElementFacade row : categoryRows) {
                if (row.getText().contains(categoryName)) {
                    // Look for edit button/icon in this row
                    try {
                        WebElementFacade editBtn = row.then(By.cssSelector("a[href*='/edit'], button.edit-btn"));
                        return editBtn.isVisible();
                    } catch (Exception e) {
                        // Try alternative locators
                        try {
                            WebElementFacade editIcon = row.then(By.cssSelector("i.fa-edit, svg, .edit-icon"));
                            return editIcon.isVisible();
                        } catch (Exception ex) {
                            return false;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAnyEditButtonVisible() {
        try {
            // Check if any edit button is visible on the page
            if (!editButtons.isEmpty()) {
                for (WebElementFacade btn : editButtons) {
                    if (btn.isVisible()) {
                        return true;
                    }
                }
            }

            // Check edit icons
            if (!editIcons.isEmpty()) {
                for (WebElementFacade icon : editIcons) {
                    if (icon.isVisible()) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public int getVisibleEditButtonsCount() {
        int count = 0;
        try {
            for (WebElementFacade editBtn : editButtons) {
                if (editBtn.isVisible()) {
                    count++;
                }
            }

            // Also count visible edit icons
            for (WebElementFacade editIcon : editIcons) {
                if (editIcon.isVisible()) {
                    count++;
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return count;
    }

    public void clickEditButtonForCategory(String categoryName) {
        try {
            // Find the row for the category
            for (WebElementFacade row : categoryRows) {
                if (row.getText().contains(categoryName)) {
                    // Click edit button
                    try {
                        WebElementFacade editBtn = row.then(By.cssSelector("a[href*='/edit'], button.edit-btn"));
                        if (editBtn.isVisible()) {
                            editBtn.click();
                            break;
                        }
                    } catch (Exception e) {
                        // Try edit icon
                        try {
                            WebElementFacade editIcon = row.then(By.cssSelector("i.fa-edit, svg, .edit-icon"));
                            if (editIcon.isVisible()) {
                                editIcon.click();
                                break;
                            }
                        } catch (Exception ex) {
                            System.out.println("No edit button/icon found for category: " + categoryName);
                        }
                    }
                }
            }
            waitFor(3000).milliseconds();
        } catch (Exception e) {
            System.out.println("Error clicking edit button: " + e.getMessage());
            throw e;
        }
    }

    public void clickFirstEditButton() {
        try {
            // Try edit buttons first
            if (!editButtons.isEmpty()) {
                for (WebElementFacade btn : editButtons) {
                    if (btn.isVisible()) {
                        btn.click();
                        waitFor(3000).milliseconds();
                        return;
                    }
                }
            }

            // Try edit icons
            if (!editIcons.isEmpty()) {
                for (WebElementFacade icon : editIcons) {
                    if (icon.isVisible()) {
                        icon.click();
                        waitFor(3000).milliseconds();
                        return;
                    }
                }
            }

            System.out.println("No visible edit buttons/icons found");
        } catch (Exception e) {
            System.out.println("Error clicking first edit button: " + e.getMessage());
        }
    }

    // ---------- Category List Methods ----------
    public boolean isCategoryDisplayedInList(String categoryName) {
        try {
            return categoryRows.stream()
                    .anyMatch(row -> row.getText().contains(categoryName));
        } catch (Exception e) {
            return false;
        }
    }

    public int getCategoryCount() {
        try {
            return categoryRows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getFirstCategoryName() {
        try {
            if (!categoryRows.isEmpty() && categoryRows.get(0).isVisible()) {
                // Try different columns to find the name
                String rowText = categoryRows.get(0).getText();
                System.out.println("DEBUG: First row text: " + rowText);

                // Try to extract name from different columns
                for (int i = 1; i <= 5; i++) { // Check first 5 columns
                    try {
                        String cellText = categoryRows.get(0)
                                .findElement(By.cssSelector("td:nth-child(" + i + ")"))
                                .getText()
                                .trim();

                        if (!cellText.isEmpty() && !cellText.matches("\\d+")) {
                            // Not just a number, likely a name
                            return cellText;
                        }
                    } catch (Exception e) {
                        // Continue to next column
                    }
                }

                // If all else fails, return the first non-empty cell
                return rowText.split("\\s+")[0];
            }
        } catch (Exception e) {
            System.out.println("Error getting first category name: " + e.getMessage());
        }
        return "";
    }

    // ---------- Empty State Methods ----------
    public boolean isEmptyStateDisplayed() {
        try {
            // Check if empty state message is visible
            if (emptyStateMessage.isVisible()) {
                return true;
            }

            // Check if empty state container is visible
            if (emptyStateContainer.isVisible()) {
                return true;
            }

            // Check if table has "no data" message
            String pageSource = getDriver().getPageSource();
            return pageSource.contains("No categories") ||
                    pageSource.contains("No data") ||
                    pageSource.contains("dataTables_empty");

        } catch (Exception e) {
            return false;
        }
    }

    // ---------- Current URL ----------
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    // ---------- Edit Page Methods ----------
    @FindBy(id = "name")
    private WebElementFacade editNameField;

    @FindBy(id = "description")
    private WebElementFacade editDescriptionField;

    @FindBy(css = "button.btn.btn-primary[type='submit']")
    private WebElementFacade updateButton;

    public boolean isEditPageLoaded() {
        try {
            String currentUrl = getDriver().getCurrentUrl();
            boolean isEditUrl = currentUrl.contains("/edit");
            boolean hasNameField = editNameField.isVisible();
            boolean hasUpdateButton = updateButton.isVisible();

            System.out.println("DEBUG: Edit page check - URL contains '/edit': " + isEditUrl);
            System.out.println("DEBUG: Edit page check - Name field visible: " + hasNameField);
            System.out.println("DEBUG: Edit page check - Update button visible: " + hasUpdateButton);

            return isEditUrl || hasNameField || hasUpdateButton;
        } catch (Exception e) {
            System.out.println("DEBUG: Edit page check - Exception: " + e.getMessage());
            return false;
        }
    }

    public void updateCategoryName(String newName) {
        editNameField.clear();
        editNameField.type(newName);
    }

    public void updateDescription(String description) {
        editDescriptionField.clear();
        editDescriptionField.type(description);
    }

    public void clickUpdate() {
        updateButton.click();
        waitFor(3000).milliseconds();
    }

    // ---------- Success/Error Messages ----------
    @FindBy(css = ".alert.alert-success")
    private WebElementFacade successMessage;

    @FindBy(css = ".alert.alert-danger")
    private WebElementFacade errorMessage;

    public boolean isSuccessMessageDisplayed() {
        try {
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        return successMessage.getText();
    }

    // ---------- Driver Access (for debugging) ----------
    public org.openqa.selenium.WebDriver getDriver() {
        return super.getDriver();
    }

    public void printAllCategories() {
        System.out.println("=== DEBUG: All Categories ===");
        for (int i = 0; i < categoryRows.size(); i++) {
            try {
                WebElementFacade row = categoryRows.get(i);
                System.out.println("Row " + i + ": " + row.getText());

                // Print each cell
                List<WebElementFacade> cells = row.thenFindAll(By.cssSelector("td"));
                for (int j = 0; j < cells.size(); j++) {
                    System.out.println("  Cell " + j + ": '" + cells.get(j).getText() + "'");
                }
            } catch (Exception e) {
                System.out.println("Error reading row " + i + ": " + e.getMessage());
            }
        }
        System.out.println("=============================");
    }

    public String getSuccessMessageText() {
        try {
            return successMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }


}