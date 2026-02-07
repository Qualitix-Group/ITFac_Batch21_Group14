package com.group14.qa.api.runners;

import com.group14.qa.api.steps.GetPlantWithoutAuthSteps;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class GetPlantWithoutAuthTest {

    @Steps
    GetPlantWithoutAuthSteps steps;

    @Test
    public void should_return_401_when_no_token_provided() {
        steps.getPlantWithoutToken(1);
        steps.verifyUnauthorizedResponse();
    }
}
