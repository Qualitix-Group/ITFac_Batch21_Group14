package com.group14.qa.api.runners;

import com.group14.qa.api.steps.GetPlantWithoutAuthSteps;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class GetPlantWithoutAuthTest {

    @Steps
    GetPlantWithoutAuthSteps getPlantWithoutAuthSteps;

    @Test
    public void TC_API_USER_PLANT_007_verify_unauthorized_access_without_token() {

        System.out.println("=========================================================");
        System.out.println("TC_API_USER_PLANT_007: Retrieve Plant Without Authentication");
        System.out.println("=========================================================");

        System.out.println("Description: GET /api/plants/{id} without Bearer token");

        System.out.println("PRECONDITIONS:");
        System.out.println("1. API server is running");
        System.out.println("2. User is not authenticated");
        System.out.println("3. Valid plant ID exists");

        System.out.println("TEST STEPS:");
        System.out.println("1. Send GET request without Authorization header");

        System.out.println("EXPECTED RESULTS:");
        System.out.println("1. Status code 401 Unauthorized");
        System.out.println("2. Authentication error message returned");
        System.out.println("3. No plant details in response");

        getPlantWithoutAuthSteps.getPlantWithoutToken(1);
        getPlantWithoutAuthSteps.verifyUnauthorizedResponse();

        System.out.println("TEST PASSED: Unauthorized access correctly blocked");
    }
}
