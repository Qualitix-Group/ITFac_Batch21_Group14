package com.group14.qa.api.runners;

import com.group14.qa.api.steps.AdminPlantUpdateSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(SerenityRunner.class)
public class AdminPlantUpdateTest {

    @Steps
    AdminPlantUpdateSteps adminPlantUpdateSteps;

    private final String token =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzcwNDEwODMyLCJleHAiOjE3NzA0MTQ0MzJ9.j7ADgpGSoXokJNBRZQf-O5gq4SaXFtdBG4LJ56mrxxE";

    @Test
    public void should_update_existing_plant_successfully() {

        int categoryId = 3;

        int plantId = adminPlantUpdateSteps.createPlantForUpdate(token, categoryId);

        adminPlantUpdateSteps.updatePlant(
                plantId,
                token,
                "UpdatedPlant",
                150.0f,
                25,
                categoryId
        );

        adminPlantUpdateSteps.verifyPlantUpdateSuccessful();
    }
}
