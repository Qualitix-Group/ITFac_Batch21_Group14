package com.group14.qa.api.runners;

import com.group14.qa.api.steps.AdminPlantNegativeSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class AdminPlantInvalidCategoryTest {

    @Steps
    AdminPlantNegativeSteps adminPlantNegativeSteps;

    // Admin token (same as other tests)
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzcwNDA1MzY5LCJleHAiOjE3NzA0MDg5Njl9.mGWdUOd6oLj-f9tURe_Y6qycmvq1CUE3Ujd2t3bVv74";

    // Test Data
    int invalidCategoryIdZero = 0; // Zero is typically invalid
    int invalidCategoryIdNegative = -1; // Negative ID is invalid
    int invalidCategoryIdLarge = 999999999; // Very large number

    @Test
    public void TC_API_ADMIN_PLANT_010B_verify_error_with_zero_category_ID() {
        System.out.println("Running additional test: Category ID = 0");

        adminPlantNegativeSteps.createPlantWithInvalidCategoryId(
                invalidCategoryIdZero, token);

        adminPlantNegativeSteps.verifyInvalidCategoryIdError();
        adminPlantNegativeSteps.verifyNoPlantCreated();
    }

    @Test
    public void TC_API_ADMIN_PLANT_010C_verify_error_with_negative_category_ID() {
        System.out.println("Running additional test: Negative category ID");

        adminPlantNegativeSteps.createPlantWithInvalidCategoryId(
                invalidCategoryIdNegative, token);

        adminPlantNegativeSteps.verifyInvalidCategoryIdError();
        adminPlantNegativeSteps.verifyNoPlantCreated();
    }

    @Test
    public void TC_API_ADMIN_PLANT_010D_verify_error_with_very_large_category_ID() {
        System.out.println("Running additional test: Very large category ID");

        adminPlantNegativeSteps.createPlantWithInvalidCategoryId(
                invalidCategoryIdLarge, token);

        adminPlantNegativeSteps.verifyInvalidCategoryIdError();
        adminPlantNegativeSteps.verifyNoPlantCreated();
    }
}