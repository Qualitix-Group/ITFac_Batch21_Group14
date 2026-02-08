package qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import qa.utils.TestData;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CategoriesPage extends PageObject {


    private final By searchInput = By.name("name");
    private final By parentFilterSelect = By.name("parentId");
    private final By searchButton = By.cssSelector("button[type='submit']");
    private final By addCategoryButton = By.cssSelector("a[href='/ui/categories/add']");
    private final By table = By.cssSelector("table.table");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By sortByIdLink = By.cssSelector("a[href*='sortField=id']");
    private final By sortByNameLink = By.cssSelector("a[href*='sortField=name']");
    private final By sortByParentLink = By.cssSelector("a[href*='sortField=parent']");
    private final By paginationLinks = By.cssSelector("ul.pagination li.page-item a.page-link");
    private final By paginationContainer = By.cssSelector("ul.pagination");
    private final By activePageLink = By.cssSelector("ul.pagination li.page-item.active a.page-link");
    private final By editButtons = By.cssSelector("a[title='Edit']");
    private final By deleteButtons = By.cssSelector("button[title='Delete']");
    private final By noCategoryMessage = By.xpath("//*[contains(text(),'No category found')]");
    private final By validationMessage =
            By.cssSelector(".invalid-feedback, .error, .validation, .alert-danger");

    /**
     * Check that a validation message is present and styled as an error.
     * Accepts common error class names to remain stable across theme tweaks.
     */
    public boolean hasValidationMessageWithErrorStyling() {
        try {
            var message = $(validationMessage)
                    .withTimeoutOf(Duration.ofSeconds(3))
                    .waitUntilVisible();

            String cssClass = message.getAttribute("class");
            String style = message.getAttribute("style");

            boolean classIndicatesError = cssClass != null &&
                    cssClass.toLowerCase().matches(".*(error|invalid|danger|alert).*");
            boolean styleIndicatesError = style != null &&
                    style.toLowerCase().contains("red");

            return classIndicatesError || styleIndicatesError;
        } catch (Exception e) {
            return false;
        }
    }

    /** Check for an explicit validation message only */
    public boolean hasValidationMessageOrPlaceholder() {
        return hasValidationMessageWithErrorStyling();
    }

    /** Open categories page */
    public void openPage() {
        String targetUrl = TestData.get("base.url") + "/ui/categories";
        getDriver().get(targetUrl);
        waitUntilLoaded();
    }

    /** Ensure page is loaded before interacting */
    public void waitUntilLoaded() {
        try {
            $(searchInput).withTimeoutOf(Duration.ofSeconds(15)).waitUntilVisible();
        } catch (Exception e) {
            String url = getDriver().getCurrentUrl();
            String title = getDriver().getTitle();
            System.out.println("[DEBUG] CategoriesPage.waitUntilLoaded failed. url=" + url + ", title=" + title);
            throw e;
        }
    }

    public boolean isSearchInputVisible() {
        try {
            $(searchInput).withTimeoutOf(Duration.ofSeconds(15)).waitUntilVisible();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Table is visible when the list is loaded */
    public boolean isTableVisible() {
        try {
            return $(table).withTimeoutOf(Duration.ofSeconds(10)).isVisible();
        } catch (Exception e) {
            return false;
        }
    }


    /** Search using ENTER key */
    public void searchFor(String text) {
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                $(searchInput).withTimeoutOf(Duration.ofSeconds(15)).waitUntilVisible();
                $(searchInput).clear();
                $(searchInput).type(text);
                return;
            } catch (StaleElementReferenceException ignored) {
                // retry after DOM refresh
            }
        }
    }

    /** Search using Search button (alternative to ENTER) */
    public void clickSearchButton() {
        safeClick($(searchButton).withTimeoutOf(Duration.ofSeconds(15)).waitUntilClickable());
    }

    /** Choose a parent filter by visible text (used when verifying filter behavior) */
    public void selectParentByVisibleText(String visibleText) {
        $(parentFilterSelect).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();
        new Select($(parentFilterSelect)).selectByVisibleText(visibleText);
    }

    /** Select the first non-empty parent option and return its text */
    public String selectFirstParentOption() {
        if (!$(parentFilterSelect).isPresent()) return "";
        $(parentFilterSelect).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();
        Select select = new Select($(parentFilterSelect));
        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            String value = option.getAttribute("value");
            if (value != null && !value.isBlank()) {
                select.selectByValue(value);
                return option.getText().trim();
            }
        }
        return "";
    }

    /** True if Add Category button is visible (admin-only control) */
    public boolean isAddButtonVisible() {
        try {
            return $(addCategoryButton).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPaginationVisible() {
        return $(paginationContainer).isVisible();
    }

    public boolean isAnyEditVisible() {
        return findAll(editButtons).stream()
            .anyMatch(e -> e.isDisplayed() && e.isEnabled());
    }

    public boolean isAnyDeleteVisible() {
        return findAll(deleteButtons).stream()
            .anyMatch(e -> e.isDisplayed() && e.isEnabled());
    }

    public boolean isNoCategoryMessageVisible() {
        return $(noCategoryMessage).withTimeoutOf(Duration.ofSeconds(10)).isVisible();
    }

    public boolean isValidationMessageVisible() {
        return $(validationMessage).isVisible();
    }

    /** True when empty-state message is shown or there are zero data rows */
    public boolean hasNoResultsState() {
        try {
            waitUntilLoaded();
            if (isNoCategoryMessageVisible()) return true;

            var rows = findAll(tableRows);
            if (rows.isEmpty()) return false; 

            long dataRows = rows.stream()
                    .filter(r -> !r.getText().toLowerCase().contains("no category"))
                    .count();
            return dataRows == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /** True if any row contains the provided text (case-insensitive) */
    public boolean rowsContainText(String text) {
        try {
            String needle = text.toLowerCase();
            return findAll(tableRows).stream()
                    .anyMatch(r -> r.getText().toLowerCase().contains(needle));
        } catch (Exception e) {
            return false;
        }
    }

    /** Return the first row's category name (column 2) to use as a real search term */
    public String getFirstRowCategoryName() {
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                var rows = findAll(tableRows);
                if (rows.isEmpty()) return "";
                var cells = rows.get(0).findElements(By.cssSelector("td"));
                if (cells.size() < 2) return "";
                return cells.get(1).getText().trim();
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return "";
    }

    /** Collect column text values from the table (1-based column index) */
    public List<String> getColumnValues(int columnIndex) {
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                List<String> values = new ArrayList<>();
                for (var row : findAll(tableRows)) {
                    var cells = row.findElements(By.cssSelector("td"));
                    if (cells.size() >= columnIndex) {
                        values.add(cells.get(columnIndex - 1).getText().trim());
                    }
                }
                return values;
            } catch (StaleElementReferenceException ignored) {
                // retry after DOM refresh
            }
        }
        return new ArrayList<>();
    }

    /** True if every row's Name column includes the search term */
    public boolean allRowsMatchNameContains(String term) {
        String needle = term.toLowerCase();
        for (String name : getColumnValues(2)) {
            if (!name.toLowerCase().contains(needle)) return false;
        }
        return true;
    }

    /** True if every row's Parent column matches the selected parent name */
    public boolean allRowsMatchParent(String parentName) {
        String needle = parentName.toLowerCase();
        for (String parent : getColumnValues(3)) {
            if (!parent.toLowerCase().contains(needle)) return false;
        }
        return true;
    }

    /** Click the edit button for a specific category name */
    public void clickEditForCategoryName(String categoryName) {
        String needle = categoryName.toLowerCase();
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                for (var row : findAll(tableRows)) {
                    if (row.getText().toLowerCase().contains(needle)) {
                        safeClick(row.findElement(By.cssSelector("a[title='Edit']")));
                        return;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        throw new AssertionError("Edit button not found for category: " + categoryName);
    }

    /** Click sorting links */
    public void clickSortByName() {
        safeClick($(sortByNameLink).withTimeoutOf(Duration.ofSeconds(10)).waitUntilClickable());
    }

    public void clickSortById() {
        safeClick($(sortByIdLink).withTimeoutOf(Duration.ofSeconds(10)).waitUntilClickable());
    }

    public void clickSortByParent() {
        safeClick($(sortByParentLink).withTimeoutOf(Duration.ofSeconds(10)).waitUntilClickable());
    }

    /** Pagination helpers */
    public int getActivePageNumber() {
        try {
            String text = $(activePageLink).withTimeoutOf(Duration.ofSeconds(5)).getText().trim();
            return Integer.parseInt(text);
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean isNextPageEnabled() {
        for (var link : findAll(paginationLinks)) {
            if ("Next".equalsIgnoreCase(link.getText().trim())) {
                String parentClass = link.findElement(By.xpath("..")).getAttribute("class");
                return parentClass == null || !parentClass.contains("disabled");
            }
        }
        return false;
    }

    public void clickNextPage() {
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                for (var link : findAll(paginationLinks)) {
                    if ("Next".equalsIgnoreCase(link.getText().trim())) {
                        safeClick(link);
                        return;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        throw new AssertionError("Next page link not found in pagination");
    }

    public void clickPreviousPage() {
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                for (var link : findAll(paginationLinks)) {
                    if ("Previous".equalsIgnoreCase(link.getText().trim())) {
                        safeClick(link);
                        return;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        throw new AssertionError("Previous page link not found in pagination");
    }

    public void clickPageNumber(int pageNumber) {
        String target = String.valueOf(pageNumber);
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                for (var link : findAll(paginationLinks)) {
                    if (target.equals(link.getText().trim())) {
                        safeClick(link);
                        return;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
    }

    public boolean hasDeleteConfirmationOnFirstRow() {
        var deleteBtns = findAll(deleteButtons);
        if (deleteBtns.isEmpty()) return false;

        deleteBtns.get(0).click();
        try {
            String alertText = getAlert().getText();
            getAlert().dismiss();
            return alertText != null && alertText.toLowerCase().contains("delete");
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean cancelDeleteOnFirstRow() {
        var deleteBtns = findAll(deleteButtons);
        if (deleteBtns.isEmpty()) return false;

        int before = findAll(tableRows).size();
        deleteBtns.get(0).click();
        try {
            getAlert().dismiss();
        } catch (Exception ignored) {
        }
        waitForResults();
        int after = findAll(tableRows).size();
        return after == before;
    }

    public void confirmDeleteOnFirstRow() {
        var deleteBtns = findAll(deleteButtons);
        if (deleteBtns.isEmpty()) return;
        deleteBtns.get(0).click();
        try {
            getAlert().accept();
        } catch (Exception ignored) {
        }
        waitForResults();
    }

    /** Check if admin actions are completely hidden (not just disabled) for user-role validation */
    public boolean adminActionsHiddenOrDisabled() {
        boolean addVisible = isAddButtonVisible();
        boolean anyEditVisible = findAll(editButtons).stream().anyMatch(WebElement::isDisplayed);
        boolean anyDeleteVisible = findAll(deleteButtons).stream().anyMatch(WebElement::isDisplayed);
        return !addVisible && !anyEditVisible && !anyDeleteVisible;
    }

    /** Check if edit/delete buttons are truly disabled (checks both attribute formats) */
    public boolean areEditDeleteButtonsDisabled() {
        var editBtns = findAll(editButtons);
        var deleteBtns = findAll(deleteButtons);

        if (editBtns.isEmpty() && deleteBtns.isEmpty()) {
            return true; // No buttons means they're effectively disabled
        }

        boolean allEditDisabled = editBtns.stream()
                .allMatch(e -> {
                    String disabled = e.getAttribute("disabled");
                    String cssClass = e.getAttribute("class");
                    boolean hasDisabledAttr = disabled != null && !disabled.isEmpty();
                    boolean hasDisabledClass = cssClass != null && cssClass.contains("disabled");
                    return hasDisabledAttr || hasDisabledClass;
                });

        boolean allDeleteDisabled = deleteBtns.stream()
                .allMatch(e -> {
                    String disabled = e.getAttribute("disabled");
                    String cssClass = e.getAttribute("class");
                    boolean hasDisabledAttr = disabled != null && !disabled.isEmpty();
                    boolean hasDisabledClass = cssClass != null && cssClass.contains("disabled");
                    return hasDisabledAttr || hasDisabledClass;
                });

        return allEditDisabled && allDeleteDisabled;
    }


    /** Wait for results after search/filter/sort */
    public void waitForResults() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(driver -> {
                    try {
                        if ($(noCategoryMessage).isVisible()) return true;
                        return !findAll(tableRows).isEmpty();
                    } catch (Exception e) {
                        return false;
                    }
                });
    }

    private void safeClick(WebElement element) {
        try {
            element.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
            js.executeScript("arguments[0].click();", element);
        }
    }
}
