package com.group14.qa.api.runners;

import com.group14.qa.api.steps.AdminPlantNegativeSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;


import static com.group14.qa.api.utils.AuthConfig.TOKEN;

@RunWith(SerenityRunner.class)
public class AdminPlantInvalidCategoryTestRunner {

    @Steps
    AdminPlantNegativeSteps adminPlantNegativeSteps;


    @Test
    public void should_reject_invalid_category_ids() {

        int[] invalidCategoryIds = {0, -1, 999999999};

        for (int categoryId : invalidCategoryIds) {
            adminPlantNegativeSteps.createPlantWithInvalidCategoryId(categoryId,  TOKEN);
            adminPlantNegativeSteps.verifyClientError();
            adminPlantNegativeSteps.verifyNoPlantCreated();
        }
    }
}
