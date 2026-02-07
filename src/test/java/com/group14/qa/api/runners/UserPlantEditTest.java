package com.group14.qa.api.runners;

import com.group14.qa.api.steps.UserPlantEditSteps;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.group14.qa.api.utils.AuthConfig.USER_TOKEN;

@RunWith(SerenityRunner.class)
public class UserPlantEditTest {

    @Steps
    UserPlantEditSteps steps;

    @Test
    public void should_return_403_when_user_edits_plant() {
        steps.userEditsPlant(5, USER_TOKEN);
        steps.verifyUserCannotEditPlant();
    }
}
