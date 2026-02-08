package qa.steps.ui;

import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import org.openqa.selenium.WebDriver;
import qa.utils.TestData;

public class SecuritySteps {

    @Managed
    WebDriver driver;

    @When("I try to access the add category page directly")
    public void iTryToAccessTheAddCategoryPageDirectly() {
        driver.get(TestData.get("base.url") + "/ui/categories/add");
    }
}
