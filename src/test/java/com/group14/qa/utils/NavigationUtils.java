package com.group14.qa.utils;

import com.group14.qa.ui.pages.LoginPage;
import com.group14.qa.testdata.TestUsers;

public class NavigationUtils {

    private final LoginPage loginPage;

    public NavigationUtils(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public void loginAsAdmin() {
        loginPage.openLoginPage();
        loginPage.login(TestUsers.Admin.USERNAME, TestUsers.Admin.PASSWORD);
    }

    public void loginAsUser() {
        loginPage.openLoginPage();
        loginPage.login(TestUsers.RegularUser.USERNAME, TestUsers.RegularUser.PASSWORD);
    }

    public void loginWithCredentials(String username, String password) {
        loginPage.openLoginPage();
        loginPage.login(username, password);
    }
}