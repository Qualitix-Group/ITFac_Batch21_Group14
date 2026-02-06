package com.group14.qa.api.runners;

import com.group14.qa.api.steps.GetPlantSummarySteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.USER_TOKEN;

@RunWith(SerenityRunner.class)
public class GetPlantSummaryTest {

    @Steps
    GetPlantSummarySteps getPlantSummarySteps;

    @Test
    public void TC_API_USER_PLANT_012_verify_get_plant_summary() {

        System.out.println("=========================================================");
        System.out.println("TC_API_USER_PLANT_012: Verify Get Plant Summary");
        System.out.println("=========================================================");

        System.out.println("\n📋 TEST CASE DETAILS:");
        System.out.println("Test ID: TC_API_USER_PLANT_012");
        System.out.println("Summary: Verify that Get Plant Summary");
        System.out.println("Description: GET /api/plants/summary");

        System.out.println("\n✅ PRECONDITIONS:");
        System.out.println("1. Application server is running");
        System.out.println("2. At least one plant record exists in the database");
        System.out.println("3. Authorization header is configured");

        System.out.println("\n🔧 TEST STEPS:");
        System.out.println("1. Set request method to GET");

        System.out.println("\n🎯 EXPECTED RESULTS:");
        System.out.println("1. Status code should be 200 OK");
        System.out.println("2. Response body contains totalPlants and lowStockPlants");
        System.out.println("3. Values should be non-negative integers");
        System.out.println("4. Response format should be JSON");

        System.out.println("\n🚀 EXECUTING TEST...");

        getPlantSummarySteps.getPlantSummary(USER_TOKEN);
        getPlantSummarySteps.verifyPlantSummaryResponse();

        System.out.println("\n✅ TEST PASSED: Plant summary retrieved successfully");
    }
}
