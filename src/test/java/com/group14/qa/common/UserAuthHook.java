package com.group14.qa.common;

import com.group14.qa.api.clients.AuthApiClient;
import io.cucumber.java.Before;

public class UserAuthHook {

    @Before("@user")
    public void loginAsUser() {
        AuthApiClient client = new AuthApiClient();
        var response = client.login("testuser", "test123");

        System.out.println("User Login Response Status: " + response.statusCode());

        if (response.statusCode() == 200) {
            String token = response.jsonPath().getString("token");
            String tokenType = response.jsonPath().getString("tokenType");

            System.out.println("User Token: " + token);
            System.out.println("User Token Type: " + tokenType);

            if (token != null && tokenType != null) {
                AuthTokens.setUserToken(tokenType + " " + token);
                System.out.println("User authentication successful!");
            } else {
                System.err.println("Failed to extract user token from response");
                token = response.jsonPath().getString("accessToken");
                tokenType = response.jsonPath().getString("token_type");

                if (token != null) {
                    String authHeader = (tokenType != null) ? tokenType + " " + token : token;
                    AuthTokens.setUserToken(authHeader);
                    System.out.println("User authentication successful (alternative path)!");
                } else {
                    throw new RuntimeException("User authentication failed - no token found");
                }
            }
        } else {
            System.err.println("User login failed! Status: " + response.statusCode());
            System.err.println("Response: " + response.asString());
            throw new RuntimeException("User authentication failed with status: " + response.statusCode());
        }
    }
}