package com.group14.qa.api.runners;

import com.group14.qa.api.steps.GetPagedPlantsSteps;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.USER_TOKEN;

@RunWith(SerenityRunner.class)
public class GetPagedPlantsTest {

    @Steps
    GetPagedPlantsSteps getPagedPlantsSteps;

    @Test
    public void TC_API_USER_PLANT_010_verify_retrieve_plants_with_pagination() {

        System.out.println("=========================================================");
        System.out.println("TC_API_USER_PLANT_010: Retrieve Plants with Pagination");
        System.out.println("=========================================================");

        System.out.println("\n📋 TEST CASE DETAILS:");
        System.out.println("Description: GET /api/plants/paged");

        System.out.println("\n✅ PRECONDITIONS:");
        System.out.println("1. Application server is running");
        System.out.println("2. User is logged in as Authorized User");
        System.out.println("3. Plant data exists in the database");

        System.out.println("\n🔧 TEST STEPS:");
        System.out.println("1. Set request method to GET");
        System.out.println("2. Add query params: page=0, size=5");

        System.out.println("\n🎯 EXPECTED RESULTS:");
        System.out.println("1. Status code 200 OK");
        System.out.println("2. Response contains pagination metadata");
        System.out.println("3. Returned records ≤ page size");

        System.out.println("\n🚀 EXECUTING TEST...");

        getPagedPlantsSteps.getPlantsWithPagination(0, 5, USER_TOKEN);
        getPagedPlantsSteps.verifyPagedPlantsResponse(5);

        System.out.println("\n✅ TEST PASSED: Plants retrieved successfully with pagination");
    }
}
