package com.group14.qa.api.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/api",
        glue = {
                "com.group14.qa.api.tests",
                "com.group14.qa.api.steps",
                "com.group14.qa.common"
        },
        plugin = {"pretty"}
)
public class ApiTestRunner {
}


