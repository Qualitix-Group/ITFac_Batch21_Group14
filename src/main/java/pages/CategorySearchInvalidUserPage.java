package pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

public class CategorySearchInvalidUserPage extends PageObject {

    @FindBy(name = "name")
    private WebElementFacade searchInput;

    @FindBy(css = "button[type='submit']")
    private WebElementFacade searchButton;

    @FindBy(xpath = "//td[contains(text(),'No category found')]")
    private WebElementFacade noCategoryMessage;

    // Perform search with invalid category name
    public void searchInvalidCategory(String categoryName) {
        searchInput.clear();
        searchInput.type(categoryName);
        searchButton.click();
    }

    // Check if 'No category found' message is displayed
    public boolean isNoCategoryFoundMessageDisplayed() {
        return noCategoryMessage.isVisible();
    }
}
