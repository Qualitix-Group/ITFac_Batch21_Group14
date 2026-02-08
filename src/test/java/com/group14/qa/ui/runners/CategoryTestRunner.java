package com.group14.qa.ui.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = {
                //"src/test/resources/features/ui/categories/category_add_button_visibility.feature",
                //"src/test/resources/features/ui/categories/category_edit_botton_visibility.feature",
                //"src/test/resources/features/ui/categories/category_add.feature",
                //"src/test/resources/features/ui/categories/category_edit.feature",
                //"src/test/resources/features/ui/categories/categories_pagination.feature",
                "src/test/resources/features/ui/categories/empty_state.feature"

        },
        glue = {"com.group14.qa.ui.steps"},
        tags = "@Smoke or @Admin or @User or @Regression or @Integration or @pagination and @categories",
        plugin = {"pretty"}
)
public class CategoryTestRunner {
}