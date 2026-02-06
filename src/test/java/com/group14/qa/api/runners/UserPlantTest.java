package com.group14.qa.api.runners;

import com.group14.qa.api.steps.UserPlantSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.USER_TOKEN;

@RunWith(SerenityRunner.class)
public class UserPlantTest {

    @Steps
    UserPlantSteps userPlantSteps;



    // Category ID - can be valid or invalid as per test case
    int categoryId = 3; // Use appropriate category ID

    @Test
    public void TC_API_USER_PLANT_008_verify_user_cannot_add_plant() {
        System.out.println("=========================================================");
        System.out.println("TC_API_USER_PLANT_008: Verify User Cannot Add Plant");
        System.out.println("=========================================================");

        System.out.println("\n📋 TEST CASE DETAILS:");
        System.out.println("Test ID: TC_API_USER_PLANT_008");
        System.out.println("Summary: Verify User Cannot Add Plant");
        System.out.println("Description: POST /api/plants/category/{categoryId}");

        System.out.println("\n✅ PRECONDITIONS:");
        System.out.println("1. User is logged in as Normal User");

        System.out.println("\n🔧 TEST STEPS:");
        System.out.println("1. Send POST request");
        System.out.println("2. Replace {categoryId} with ID: " + categoryId);
        System.out.println("3. Click Send");

        System.out.println("\n🎯 EXPECTED RESULT:");
        System.out.println("1. Status code should be 403 Forbidden");
        System.out.println("2. Plant should NOT be created");

        System.out.println("\n🚀 EXECUTING TEST...");

        // Step 1: Send POST request as user
        System.out.println("\nStep 1: User sending POST request to create plant...");
        userPlantSteps.userCreatesPlant(categoryId, USER_TOKEN);

        // Step 2: Verify response
        System.out.println("\nStep 2: Verifying response...");
        userPlantSteps.verifyUserCannotAddPlant();

        System.out.println("\n✅ TEST SUMMARY:");
        System.out.println("Test: TC_API_USER_PLANT_008 - PASSED");
        System.out.println("Result: Normal user correctly cannot add plant (403 Forbidden)");
    }
}