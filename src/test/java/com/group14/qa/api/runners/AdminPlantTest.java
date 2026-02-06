package com.group14.qa.api.runners;

import com.group14.qa.api.steps.AdminPlantSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class AdminPlantTest {

    @Steps
    AdminPlantSteps adminPlantSteps;

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzcwNDAyNjI1LCJleHAiOjE3NzA0MDYyMjV9.HXaqsjpOIh1PpdUPZhEkwUCjJJakZP8MSCwr6UjJICs";
    int validSubCategoryId = 3;
    String uniquePlantName = "SunFlower_" + System.currentTimeMillis(); // Generate unique name

    @Test
    public void TC_API_ADMIN_PLANT_008_create_plant_under_valid_sub_category() {
        adminPlantSteps.createPlantUnderSubCategory(validSubCategoryId, token);
        adminPlantSteps.verifyPlantCreationResponse(validSubCategoryId);
    }

    @Test
    public void TC_API_ADMIN_PLANT_009_verify_admin_error_when_creating_duplicate_plant_under_same_category() {
        // Step 1: First create a plant (precondition)
        adminPlantSteps.createPlantWithName(validSubCategoryId, token, uniquePlantName);
        adminPlantSteps.verifyPlantCreationResponse(validSubCategoryId);

        // Step 2: Try to create duplicate plant with same name
        adminPlantSteps.createDuplicatePlant(validSubCategoryId, token);

        // Step 3: Verify error response for duplicate
        adminPlantSteps.verifyDuplicatePlantError();
    }
}