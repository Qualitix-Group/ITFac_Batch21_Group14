package com.group14.qa.ui.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = {
                "src/test/resources/features/ui/categories/admin_category_001.feature",
                //"src/test/resources/features/ui/categories/admin_category_002.feature"
        },
        glue = {"com.group14.qa.ui.steps"},
        tags = "@Smoke or @Admin or @User or @Regression or @Integration",
        plugin = {"pretty"}
)
public class CategoryTestRunner {
}