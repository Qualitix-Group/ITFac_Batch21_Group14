package com.group14.qa.api.runners;

import com.group14.qa.api.steps.AdminPlantUpdateSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.TOKEN;

@RunWith(SerenityRunner.class)
public class AdminPlantUpdateTestRunner {

    @Steps
    AdminPlantUpdateSteps steps;

    private int plantId;
    private final int subCategoryId = 4;

    private String updatedName;
    private final float updatedPrice = 150.0f;
    private final int updatedQuantity = 25;

    @Before
    public void setUp() {
        updatedName = "UpdatedPlant_" + System.currentTimeMillis();
        if (updatedName.length() > 25) {
            updatedName = updatedName.substring(0, 25);
        }

        plantId = steps.createPlantForUpdate(TOKEN, subCategoryId);
    }

    @Test
    public void TC_API_ADMIN_PLANT_012_verify_plant_details_updated_successfully_using_valid_plant_ID() {

        steps.updatePlant(
                plantId,
                TOKEN,
                updatedName,
                updatedPrice,
                updatedQuantity
        );

        steps.verifyPlantUpdatedSuccessfully(
                updatedName,
                updatedPrice,
                updatedQuantity
        );
    }
}
