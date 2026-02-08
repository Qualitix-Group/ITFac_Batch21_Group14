//package com.group14.qa.ui.steps;
//
//import com.group14.qa.testdata.TestUsers;
//import com.group14.qa.ui.pages.LoginPage;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import net.serenitybdd.annotations.Steps;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class CategoryPaginationSteps {
//
//    @Steps
//    LoginPage loginPage;
//
//    @Given("I am on the login page")
//    public void i_am_on_the_login_page() {
//        loginPage.openLoginPage();
//    }
//
//    @Given("the QA Training App is running")
//    public void the_qa_training_app_is_running() {
//        // App should be running
//    }
//
//    @When("I login as admin")
//    public void i_login_as_admin() {
//        loginPage.login(TestUsers.Admin.USERNAME, TestUsers.Admin.PASSWORD);
//    }
//
//    @When("I login as regular user")
//    public void i_login_as_regular_user() {
//        loginPage.login(TestUsers.RegularUser.USERNAME, TestUsers.RegularUser.PASSWORD);
//    }
//
//    @When("I login with username {string} and password {string}")
//    public void i_login_with_username_and_password(String username, String password) {
//        loginPage.login(username, password);
//    }
//
//    @Then("I should be redirected to the dashboard")
//    public void i_should_be_redirected_to_the_dashboard() {
//        loginPage.waitFor(2000);
//        assertThat(loginPage.isDashboardDisplayed())
//                .as("Dashboard should be displayed")
//                .isTrue();
//    }
//
//    @Then("I should see error message {string}")
//    public void i_should_see_error_message(String expectedMessage) {
//        loginPage.waitFor(1000);
//        assertThat(loginPage.isErrorMessageDisplayed())
//                .as("Error message should be displayed")
//                .isTrue();
//        assertThat(loginPage.getErrorMessage())
//                .as("Error message should contain: " + expectedMessage)
//                .containsIgnoringCase(expectedMessage);
//    }
//}