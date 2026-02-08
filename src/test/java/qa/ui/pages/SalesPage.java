package qa.ui.pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import net.serenitybdd.core.pages.WebElementFacade;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;

@DefaultUrl("/ui/sales")
public class SalesPage extends PageObject {

    // Prefer stable attributes (id/name/css) before falling back to text/xpath
    private final By salesHeading = By.cssSelector(".main-content h3");
    private final By sellLink = By.cssSelector("a[href*='/ui/sales/new']");
    private final By table = By.cssSelector("table.table");
    private final By tableHeaders = By.cssSelector("table.table thead th");
    private final By tableRows = By.cssSelector("table.table tbody tr");
    private final By deleteButtons = By.cssSelector("form[action*='/ui/sales/delete'] button.btn-outline-danger");
    // Empty placeholder cell typically rendered with text like "No sales found" when list is empty
    private final By emptyState = By.xpath("//td[contains(normalize-space(),'No sales') or contains(normalize-space(),'No data')]");
    private final By pagination = By.cssSelector("ul.pagination");
    private final By paginationLinks = By.cssSelector("ul.pagination li.page-item a.page-link");
    private final By activePageLink = By.cssSelector("ul.pagination li.page-item.active a.page-link");
    private final By successAlert = By.cssSelector(".alert.alert-success");
    private final By errorAlert = By.cssSelector(".alert.alert-danger");

    public void openList() {
        open();
        waitForCondition().until(driver -> $(salesHeading).isVisible() || $(emptyState).isVisible());
    }

    public boolean isSellVisible() {
        return $(sellLink).isVisible();
    }

    public boolean isTableVisible() {
        return $(table).isVisible() || $(emptyState).isVisible();
    }

    /**
     * Page considered loaded when either heading or primary action is visible; used to assert no client error
     * occurred even when data is absent.
     */
    public boolean isLoaded() {
        return $(salesHeading).isVisible() || $(sellLink).isVisible();
    }

    public boolean isDeleteVisible() {
        return !findAll(deleteButtons).isEmpty();
    }

    public int getRowCount() {
        return findAll(tableRows).size();
    }

    public boolean hasEmptyMessage() {
        if (!$(emptyState).isVisible()) {
            return false;
        }
        String text = $(emptyState).getText().toLowerCase();
        // Ensure we specifically assert the intended empty-state copy, not just any td
        return text.contains("no sales");
    }

    public void clickSell() {
        $(sellLink).click();
    }

    public boolean isPaginationVisible() {
        return $(pagination).isVisible();
    }

    public int getActivePageNumber() {
        try {
            String text = $(activePageLink).getText().trim();
            return Integer.parseInt(text);
        } catch (Exception e) {
            return -1;
        }
    }

    public void clickNextPage() {
        clickPaginationLink("Next");
    }

    public void clickPreviousPage() {
        clickPaginationLink("Previous");
    }

    public void clickPageNumber(int pageNumber) {
        String target = String.valueOf(pageNumber);
        for (var link : findAll(paginationLinks)) {
            if (target.equals(link.getText().trim())) {
                link.click();
                return;
            }
        }
    }

    private void clickPaginationLink(String label) {
        for (var link : findAll(paginationLinks)) {
            if (label.equalsIgnoreCase(link.getText().trim())) {
                link.click();
                return;
            }
        }
    }

    public boolean deleteFirstSale() {
        var buttons = findAll(deleteButtons);
        if (buttons.isEmpty()) {
            return false;
        }

        int before = getRowCount();
        buttons.get(0).click();

        // Native confirm pops on form submit in this UI; accept if present
        try {
            getAlert().accept();
        } catch (Exception ignored) {
            // Some browsers auto-accept; proceed either way
        }

        waitABit(500);
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(3))
                    .until(d -> getRowCount() != before || hasEmptyMessage());
        } catch (Exception ignored) {
            // timeout is fine; we'll evaluate below
        }

        int after = getRowCount();
        return after < before || hasEmptyMessage();
    }

    public boolean hasDeleteConfirmationOnFirstSale() {
        var buttons = findAll(deleteButtons);
        if (buttons.isEmpty()) {
            return false;
        }

        buttons.get(0).click();
        try {
            String alertText = getAlert().getText();
            getAlert().dismiss();
            return alertText != null && alertText.toLowerCase().contains("delete");
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean cancelDeleteFirstSale() {
        var buttons = findAll(deleteButtons);
        if (buttons.isEmpty()) {
            return false;
        }

        int before = getRowCount();
        buttons.get(0).click();
        try {
            getAlert().dismiss();
        } catch (Exception ignored) {
            // If there is no alert, deletion might not have been triggered
        }

        waitABit(300);
        int after = getRowCount();
        return after == before;
    }

    public boolean hasSuccessMessage() {
        return $(successAlert).isVisible();
    }

    public boolean hasErrorMessage() {
        return $(errorAlert).isVisible();
    }

    public void clickSortByHeader(String headerText) {
        String needle = headerText.toLowerCase();
        for (WebElementFacade header : findAll(tableHeaders)) {
            String text = header.getText().toLowerCase();
            if (text.contains(needle)) {
                try {
                    header.findElement(By.tagName("a")).click();
                } catch (Exception e) {
                    header.click();
                }
                return;
            }
        }
    }

    public List<String> getColumnValuesByHeader(String headerText) {
        int index = getHeaderIndex(headerText);
        if (index < 0) {
            return List.of();
        }
        List<String> values = new ArrayList<>();
        for (WebElementFacade row : findAll(tableRows)) {
            var cells = row.findElements(By.tagName("td"));
            if (index < cells.size()) {
                values.add(cells.get(index).getText().trim());
            }
        }
        return values;
    }

    private int getHeaderIndex(String headerText) {
        String needle = headerText.toLowerCase();
        List<WebElementFacade> headers = findAll(tableHeaders);
        for (int i = 0; i < headers.size(); i++) {
            String text = headers.get(i).getText().toLowerCase();
            if (text.contains(needle)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isSortedByDateDesc() {
        List<LocalDateTime> dates = new ArrayList<>();

        for (WebElementFacade row : findAll(tableRows)) {
            var cells = row.findElements(By.tagName("td"));
            if (cells.size() < 4) {
                continue;
            }

            String rawDate = cells.get(3).getText().trim();
            LocalDateTime parsed = tryParse(rawDate);
            if (parsed != null) {
                dates.add(parsed);
            }
        }

        if (dates.size() <= 1) {
            return true;
        }

        for (int i = 0; i < dates.size() - 1; i++) {
            if (dates.get(i).isBefore(dates.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    private LocalDateTime tryParse(String raw) {
        DateTimeFormatter[] formats = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm[:ss]"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm[:ss]")
        };

        for (DateTimeFormatter f : formats) {
            try {
                return LocalDateTime.parse(raw, f);
            } catch (DateTimeParseException ignored) {
                // try next
            }
        }
        return null;
    }
}
