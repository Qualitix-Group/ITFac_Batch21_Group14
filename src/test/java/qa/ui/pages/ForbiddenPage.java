package qa.ui.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

public class ForbiddenPage extends PageObject {

    private final By forbiddenText = By.xpath("//*[contains(text(),'403') or contains(text(),'Forbidden') or contains(text(),'Access Denied')]");

    public boolean isForbiddenVisible() {
        return $(forbiddenText).isVisible();
    }
}
