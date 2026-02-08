package qa.ui.pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

@DefaultUrl("/ui/categories/add")
public class CategoryAddPage extends PageObject {


    private final By nameInput = By.id("name");
    private final By parentSelect = By.id("parentId");
    private final By saveButton = By.cssSelector("button[type='submit']");
    private final By cancelButton = By.cssSelector("a[href='/ui/categories']");
    private final By pageHeading = By.xpath("//*[self::h1 or self::h2 or self::h3][contains(normalize-space(),'Category')]");
    private final By validationText = By.cssSelector(".invalid-feedback, .error, .validation, .alert-danger");

    public void openForm() {
        open();
        waitUntilLoaded();
    }

    public void waitUntilLoaded() {
        try {
            $(nameInput).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();
        } catch (Exception ignored) {
            // some builds may redirect immediately; continue
        }
    }

    public void clickCancel() {
        $(cancelButton).withTimeoutOf(Duration.ofSeconds(10)).click();
    }

    public void clickSave() {
        $(saveButton).withTimeoutOf(Duration.ofSeconds(10)).click();
    }

    /** Enter category name (used for both Add and Edit) */
    public void enterName(String name) {
        $(nameInput).withTimeoutOf(Duration.ofSeconds(10)).waitUntilVisible();
        $(nameInput).clear();
        $(nameInput).type(name);
    }

    /** Select parent category when needed; for main category leave it unchanged */
    public void selectParentByVisibleText(String visibleText) {
        if (!$(parentSelect).isPresent()) return;
        new Select($(parentSelect)).selectByVisibleText(visibleText);
    }

    public boolean isOnForm() {
        return $(nameInput).withTimeoutOf(Duration.ofSeconds(10)).isVisible();
    }

    public boolean isOnList() {
        String url = getDriver().getCurrentUrl();
        return url.contains("/ui/categories") && !url.contains("/add");
    }

    /** Submit with empty name and check validation text appears */
    public boolean showsNameValidationMessage() {
        $(nameInput).withTimeoutOf(Duration.ofSeconds(10)).clear();
        clickSave();
        try {
            var msg = $(validationText).withTimeoutOf(Duration.ofSeconds(5)).waitUntilVisible();
            String text = msg.getText().toLowerCase();
            return msg.isVisible() && (text.contains("required") || text.contains("must be"));
        } catch (Exception e) {
            return false;
        }
    }

    public String getNameValidationText() {
        try {
            return $(validationText).withTimeoutOf(Duration.ofSeconds(5)).waitUntilVisible().getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean hasValidationContaining(String needle) {
        String text = getNameValidationText().toLowerCase();
        return text.contains(needle.toLowerCase());
    }
}
