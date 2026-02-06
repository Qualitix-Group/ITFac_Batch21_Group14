package com.group14.qa.api.runners;

import com.group14.qa.api.steps.AdminPlantSteps;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.TOKEN;

@RunWith(SerenityRunner.class)
public class AdminPlantTest {

    @Steps
    AdminPlantSteps adminPlantSteps;


    private final int validSubCategoryId = 3;

    @Test
    public void TC_API_ADMIN_PLANT_008_create_plant_under_valid_sub_category() {

        String plantName = "SunFlower_" + System.currentTimeMillis();

        adminPlantSteps.createPlant(validSubCategoryId, TOKEN, plantName);
        adminPlantSteps.verifyPlantCreated(plantName, validSubCategoryId);
    }

    @Test
    public void TC_API_ADMIN_PLANT_009_should_fail_when_creating_duplicate_plant() {

        String plantName = "Rose_" + System.currentTimeMillis();

        // Precondition
        adminPlantSteps.createPlant(validSubCategoryId, TOKEN, plantName);
        adminPlantSteps.verifyPlantCreated(plantName, validSubCategoryId);

        // Action
        adminPlantSteps.createPlant(validSubCategoryId, TOKEN, plantName);

        // Assertion
        adminPlantSteps.verifyDuplicatePlantError();
    }
}
