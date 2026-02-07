package com.group14.qa.api.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/api/user_plant_api.feature",
        glue = {
                "com.group14.qa.api.steps",
                "com.group14.qa.common"
        },
        tags = "@user",
        plugin = {"pretty"}
)
public class UserPlantApiTestRunner {
}