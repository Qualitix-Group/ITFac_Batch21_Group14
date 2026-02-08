package pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
public class VerifyDeletedCategoryNotDisplayedPage extends PageObject {

    // Search bar
    private final By searchBar =
            By.xpath("//input[@placeholder='Search sub category']");

    // "No category found" message
    private final By noCategoryFoundMessage =
            By.xpath("//td[@class='text-center text-muted py-4']");

    // Type category name into search bar
    public void searchDeletedCategory(String categoryName) {
        WebElementFacade search = find(searchBar);
        search.waitUntilVisible();
        search.clear();
        search.type(categoryName);
        search.sendKeys(Keys.ENTER);
    }


    // Verify "No category found" message
    public boolean isNoCategoryFoundDisplayed() {
        try {
            return find(noCategoryFoundMessage)
                    .waitUntilVisible()
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
