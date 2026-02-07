package com.group14.qa.api.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/api/admin_create_plant_deleted_category.feature",
        glue = {
                "com.group14.qa.api.steps",
                "com.group14.qa.common"
        },
        tags = "@admin",
        plugin = {"pretty"}
)
public class AdminPlantDeletedCategoryTestRunner {
}
