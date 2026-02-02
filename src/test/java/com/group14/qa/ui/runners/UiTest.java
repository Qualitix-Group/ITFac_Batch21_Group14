
package com.group14.qa.ui.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/ui",
        glue = {"com.group14.qa.ui.steps"},
        plugin = {"pretty"},
        tags = "@Positive",
        monochrome = true
)
public class UiTest {
}

