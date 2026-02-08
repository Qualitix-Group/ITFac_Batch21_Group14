package qa.ui.pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import net.serenitybdd.core.pages.WebElementFacade;

import java.time.Duration;
import java.util.stream.Collectors;

@DefaultUrl("/ui/sales/new")
public class SalesCreatePage extends PageObject {

    private final By cancelButton = By.cssSelector("a.btn.btn-secondary, a[href$='/ui/sales'], button.btn-secondary");
    private final By heading = By.cssSelector(".main-content h3");
    private final By plantSelect = By.id("plantId"); // id present in form markup
    private final By quantityInput = By.id("quantity");
    private final By sellButton = By.cssSelector("form[action='/ui/sales'] button.btn-primary");
    private final By validationMessage = By
            .xpath("//*[contains(.,'Quantity') and (contains(.,'required') or contains(.,'greater'))]");
    private final By plantError = By.xpath("//label[contains(normalize-space(),'Plant')]/following::div[contains(@class,'text-danger')][1]");
    private final By quantityError = By.xpath("//label[contains(normalize-space(),'Quantity')]/following::div[contains(@class,'text-danger')][1]");
    private final By errorAlert = By.cssSelector(".alert.alert-danger");

    public void openForm() {
        open();
        waitUntilLoaded();
    }

    public void waitUntilLoaded() {
        try {
            $(cancelButton).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();
        } catch (Exception ignored) {
            // tolerate redirect or missing cancel in current build
        }
    }

    public void clickCancel() {
        $(cancelButton).withTimeoutOf(Duration.ofSeconds(10)).click();
    }

    public boolean isOnForm() {
        String url = getDriver().getCurrentUrl();
        return url.contains("/ui/sales") && (url.contains("/new") || url.contains("/add"));
    }

    /**
     * Select a plant in the dropdown and return the actual selected option text.
     * Tries exact match first, then contains match, then first non-placeholder
     * option.
     */
    public String choosePlant(String plantName) {
        WebElementFacade select = $(plantSelect);
        select.withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();

        // 1. Try exact match
        try {
            select.selectByVisibleText(plantName);
            return getSelectedPlantText(select);
        } catch (Exception ignored) {
            // continue to partial match
        }

        // 2. Try contains match (case-insensitive)
        var options = select.thenFindAll("option");
        String needle = plantName.toLowerCase();
        var matched = options.stream()
                .filter(opt -> opt.getText().toLowerCase().contains(needle))
                .findFirst();
        if (matched.isPresent()) {
            matched.get().click();
            return getSelectedPlantText(select);
        }

        // 3. Fallback: first non-placeholder option
        var fallback = options.stream()
                .filter(opt -> !opt.getText().toLowerCase().contains("select") && !opt.getText().isBlank())
                .findFirst()
                .or(() -> options.stream().findFirst());
        fallback.ifPresent(WebElementFacade::click);
        return getSelectedPlantText(select);
    }

    private String getSelectedPlantText(WebElementFacade select) {
        try {
            var selected = select.thenFindAll("option").stream()
                    .filter(o -> o.isSelected())
                    .findFirst();
            return selected.map(WebElementFacade::getText).map(String::trim).orElse("");
        } catch (Exception e) {
            return "";
        }
    }

    public java.util.List<String> getPlantOptionTexts() {
        WebElementFacade select = $(plantSelect);
        if (!select.isPresent()) {
            return java.util.List.of();
        }
        return select.thenFindAll("option").stream()
                .map(opt -> opt.getText().trim())
                .filter(t -> !t.isBlank())
                .collect(Collectors.toList());
    }

    public boolean optionsContainStockQuantities() {
        for (String text : getPlantOptionTexts()) {
            if (text.toLowerCase().contains("stock:")) {
                return true;
            }
        }
        return false;
    }

    public String getFirstPlantOptionWithStock() {
        return getPlantOptionTexts().stream()
                .filter(t -> t.toLowerCase().contains("stock:"))
                .findFirst()
                .orElse("");
    }

    public int parseStockFromOptionText(String optionText) {
        if (optionText == null) return -1;
        String normalized = optionText.replaceAll("(?i).*stock:\\s*", "");
        String digits = normalized.replaceAll("[^0-9]", "");
        if (digits.isBlank()) return -1;
        return Integer.parseInt(digits);
    }

    /**
     * Return the plant name from the first non-placeholder option in the dropdown.
     * Strips the "(Stock: N)" suffix so the name matches what the plants table
     * shows.
     */
    public String getFirstPlantOptionName() {
        try {
            WebElementFacade select = $(plantSelect);
            if (!select.isPresent())
                return "";
            var options = select.thenFindAll("option");
            return options.stream()
                    .map(o -> o.getText().trim())
                    .filter(t -> !t.isBlank() && !t.toLowerCase().contains("select"))
                    .findFirst()
                    .map(SalesCreatePage::stripStockSuffix)
                    .orElse("");
        } catch (Exception e) {
            return "";
        }
    }

    /** Strip "(Stock: 99)" or similar suffix from dropdown text. */
    private static String stripStockSuffix(String text) {
        return text.replaceAll("\\s*\\(Stock:.*\\)\\s*$", "").trim();
    }

    public void setQuantity(int quantity) {
        $(quantityInput).clear();
        $(quantityInput).type(String.valueOf(quantity));
    }

    public void submitSale() {
        $(sellButton).click();
        // Wait for the form POST to complete and redirect away from the form page
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(getDriver(), Duration.ofSeconds(10))
                    .until(d -> {
                        String url = d.getCurrentUrl();
                        return !url.contains("/new") && !url.contains("/add");
                    });
        } catch (Exception ignored) {
            // tolerate if redirect doesn't match expected pattern
        }
    }

    public boolean hasQuantityValidation() {
        if ($(validationMessage).isVisible()) {
            return true;
        }

        var qty = $(quantityInput);

        // Native HTML5 validation message (min="1")
        try {
            String msg = qty.getAttribute("validationMessage");
            if (msg != null && !msg.isBlank()) {
                return true;
            }
        } catch (Exception ignored) {
            // ignore and try other signals
        }

        // Browser validity state
        try {
            Object valid = evaluateJavascript("return arguments[0].validity.valid;", qty);
            if (valid instanceof Boolean && !((Boolean) valid)) {
                return true;
            }
        } catch (Exception ignored) {
            // ignore and try aria-invalid
        }

        String ariaInvalid = qty.getAttribute("aria-invalid");
        return ariaInvalid != null && ariaInvalid.equalsIgnoreCase("true");
    }

    public boolean hasPlantValidation() {
        try {
            String text = $(plantError).getText();
            return text != null && !text.isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasQuantityErrorMessage() {
        try {
            String text = $(quantityError).getText();
            return text != null && !text.isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasErrorAlert() {
        return $(errorAlert).isVisible();
    }

    /**
     * After submit the app returns to list; confirm URL change without /new or
     * /add.
     */
    public boolean isOnList() {
        String url = getDriver().getCurrentUrl();
        boolean notAdd = !(url.contains("/add") || url.contains("/new"));
        boolean landedOnSalesOrDashboard = url.contains("/ui/sales") || url.contains("/ui/dashboard");
        return notAdd && landedOnSalesOrDashboard;
    }
}
