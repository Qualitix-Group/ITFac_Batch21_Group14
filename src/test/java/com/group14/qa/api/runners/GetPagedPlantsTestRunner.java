package com.group14.qa.api.runners;

import com.group14.qa.api.steps.GetPagedPlantsSteps;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.USER_TOKEN;

@RunWith(SerenityRunner.class)
public class GetPagedPlantsTestRunner {

    @Steps
    GetPagedPlantsSteps steps;

    @Test
    public void should_retrieve_plants_with_pagination() {
        steps.getPlantsWithPagination(0, 5, USER_TOKEN);
        steps.verifyPagedPlantsResponse(5);
    }
}
