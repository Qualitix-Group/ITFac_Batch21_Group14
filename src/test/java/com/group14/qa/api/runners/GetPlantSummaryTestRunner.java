package com.group14.qa.api.runners;

import com.group14.qa.api.steps.GetPlantSummarySteps;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.USER_TOKEN;

@RunWith(SerenityRunner.class)
public class GetPlantSummaryTestRunner {

    @Steps
    GetPlantSummarySteps steps;

    @Test
    public void should_get_plant_summary() {
        steps.getPlantSummary(USER_TOKEN);
        steps.verifyPlantSummaryResponse();
    }
}
