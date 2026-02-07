package com.group14.qa.api.runners;

import com.group14.qa.api.steps.UserPlantSteps;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.USER_TOKEN;

@RunWith(SerenityRunner.class)
public class UserPlantTestRunner {

    @Steps
    UserPlantSteps steps;

    @Test
    public void should_return_403_when_user_creates_plant() {
        steps.userCreatesPlant(3, USER_TOKEN);
        steps.verifyUserCannotAddPlant();
    }
}
