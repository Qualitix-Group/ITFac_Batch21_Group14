package com.group14.qa.ui.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/ui/verify_deleted_category_not_displayed.feature",
        glue = {"com.group14.qa.ui.steps"},
        tags = "@Regression",
        plugin = {
                "pretty",
                "html:target/verify-deleted-category-report.html"
        },
        monochrome = true
)
public class VerifyDeletedCategoryNotDisplayedRunner {
}
