package com.group14.qa.api.runners;

import com.group14.qa.api.steps.UserPlantEditSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.USER_TOKEN;

@RunWith(SerenityRunner.class)
public class UserPlantEditTest {

    @Steps
    UserPlantEditSteps userPlantEditSteps;

    int plantId = 5; // use a valid existing plant ID

    @Test
    public void TC_API_USER_PLANT_009_verify_user_cannot_edit_plant() {

        System.out.println("=========================================================");
        System.out.println("TC_API_USER_PLANT_009: Verify User Cannot Edit Plant");
        System.out.println("=========================================================");

        System.out.println("\n📋 TEST CASE DETAILS:");
        System.out.println("Test ID: TC_API_USER_PLANT_009");
        System.out.println("Summary: Verify User Cannot Edit Plant");
        System.out.println("Description: PUT /api/plants/{id}");

        System.out.println("\n✅ PRECONDITIONS:");
        System.out.println("1. User is logged in as Normal User");

        System.out.println("\n🔧 TEST STEPS:");
        System.out.println("1. Send PUT request");
        System.out.println("2. Replace {id} with valid ID: " + plantId);
        System.out.println("3. Click Send");

        System.out.println("\n🎯 EXPECTED RESULT:");
        System.out.println("1. Status code should be 403 Forbidden");
        System.out.println("2. Plant should NOT be updated");

        System.out.println("\n🚀 EXECUTING TEST...");

        System.out.println("\nStep 1: User sending PUT request to edit plant...");
        userPlantEditSteps.userEditsPlant(plantId, USER_TOKEN);

        System.out.println("\nStep 2: Verifying response...");
        userPlantEditSteps.verifyUserCannotEditPlant();

        System.out.println("\n✅ TEST SUMMARY:");
        System.out.println("Result: Normal user correctly cannot edit plant (403 Forbidden)");
    }
}
