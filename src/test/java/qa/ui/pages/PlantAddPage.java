package qa.ui.pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

@DefaultUrl("/ui/plants/add")
public class PlantAddPage extends PageObject {

    // Locator priority: id > name > css > xpath > other locators.
    // Add Plant form uses stable ids for fields.
    private final By nameInput = By.id("name");
    private final By categorySelect = By.id("categoryId");
    private final By priceInput = By.id("price");
    private final By quantityInput = By.id("quantity");

    // Save/Cancel buttons do not have ids, so css selectors are used.
    private final By saveButton = By.cssSelector("button.btn.btn-primary");
    private final By cancelButton = By.cssSelector("a[href='/ui/plants'].btn.btn-secondary");

    // Validation/error messages have no fixed id in current html.
    private final By validationMessage = By.cssSelector(".invalid-feedback, .error, .validation, .alert-danger, .text-danger");
    private final By pageHeading = By.xpath("//*[self::h1 or self::h2 or self::h3][contains(normalize-space(),'Plant')]");

    public void openForm() {
        open();
        waitUntilLoaded();
    }

    public void waitUntilLoaded() {
        try {
            $(nameInput).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();
        } catch (Exception ignored) {
            // tolerate redirect or different layout
        }
    }

    public void enterPlantName(String name) {
        $(nameInput).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();
        $(nameInput).clear();
        $(nameInput).type(name);
    }

    public String selectFirstCategoryOption() {
        if (!$(categorySelect).isPresent()) return "";
        Select select = new Select($(categorySelect).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible());
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

    public void selectCategoryByVisibleText(String visibleText) {
        new Select($(categorySelect).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible())
                .selectByVisibleText(visibleText);
    }

    public void enterPrice(String price) {
        $(priceInput).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();
        $(priceInput).clear();
        $(priceInput).type(price);
    }

    public void enterQuantity(String quantity) {
        $(quantityInput).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();
        $(quantityInput).clear();
        $(quantityInput).type(quantity);
    }

    public void clickSave() {
        $(saveButton).withTimeoutOf(Duration.ofSeconds(10)).waitUntilClickable().click();
    }

    public void clickCancel() {
        $(cancelButton).withTimeoutOf(Duration.ofSeconds(10)).click();
    }

    public boolean hasValidationMessage() {
        try {
            return $(validationMessage).withTimeoutOf(Duration.ofSeconds(5)).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasValidationTextContaining(String text) {
        String needle = text.toLowerCase();
        return findAll(validationMessage).stream()
                .map(e -> e.getText() == null ? "" : e.getText().toLowerCase())
                .anyMatch(msg -> msg.contains(needle));
    }

    public void submitWith(String name, String price, String quantity) {
        enterPlantName(name);
        if (price != null) enterPrice(price);
        if (quantity != null) enterQuantity(quantity);
        clickSave();
    }

    public boolean isOnList() {
        String url = getDriver().getCurrentUrl();
        return url.contains("/ui/plant") && !url.contains("/add");
    }
}
