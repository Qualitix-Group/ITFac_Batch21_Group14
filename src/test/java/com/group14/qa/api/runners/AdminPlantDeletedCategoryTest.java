package com.group14.qa.api.runners;

import com.group14.qa.api.steps.AdminPlantDeletedCategorySteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;


import static com.group14.qa.api.utils.AuthConfig.ADMIN_TOKEN;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SerenityRunner.class)
public class AdminPlantDeletedCategoryTest {

    @Steps
    AdminPlantDeletedCategorySteps adminPlantDeletedCategorySteps;


    private final int deletedCategoryId = 99998;

    @Test
    public void should_not_allow_admin_to_add_plant_with_deleted_category_id() {

        // WHEN
        adminPlantDeletedCategorySteps.createPlantWithDeletedCategoryId(
                deletedCategoryId,  ADMIN_TOKEN
        );

        // THEN
        int statusCode = adminPlantDeletedCategorySteps.getResponse().getStatusCode();

        assertThat(statusCode)
                .as("Expected client error when using deleted category ID")
                .isBetween(400, 499);

        adminPlantDeletedCategorySteps.verifyNoPlantCreated();
    }
}
