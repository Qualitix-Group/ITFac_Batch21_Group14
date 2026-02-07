package com.group14.qa.ui.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/ui/admin_delete_plants.feature",
        glue = {"com.group14.qa.ui.steps"},
        tags = "@Smoke",
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        monochrome = true
)

public class AdminPlantDeleteTestRunner {
}
