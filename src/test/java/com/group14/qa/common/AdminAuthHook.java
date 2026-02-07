//package com.group14.qa.common;
//
//import com.group14.qa.api.clients.AuthApiClient;
//import io.cucumber.java.Before;
//
//public class AdminAuthHook {
//
//    @Before("@admin")
//    public void loginAsAdmin() {
//
//        AuthApiClient client = new AuthApiClient();
//
//        var response = client.login("admin", "admin123");
//
//        String token = response.jsonPath().getString("token");
//        String tokenType = response.jsonPath().getString("tokenType");
//
//        AuthTokens.setAdminToken(tokenType + " " + token);
//    }
//}


package com.group14.qa.common;

import com.group14.qa.api.clients.AuthApiClient;
import io.cucumber.java.Before;

public class AdminAuthHook {

    @Before("@admin")
    public void loginAsAdmin() {
        AuthApiClient client = new AuthApiClient();
        var response = client.login("admin", "admin123");

        System.out.println("Admin Login Response Status: " + response.statusCode());

        if (response.statusCode() == 200) {
            String token = response.jsonPath().getString("token");
            String tokenType = response.jsonPath().getString("tokenType");

            // Debug output
            System.out.println("Admin Token: " + token);
            System.out.println("Admin Token Type: " + tokenType);

            if (token != null && tokenType != null) {
                AuthTokens.setAdminToken(tokenType + " " + token);
                System.out.println("Admin authentication successful!");
            } else {
                System.err.println("Failed to extract admin token from response");
                // Try alternative JSON paths
                token = response.jsonPath().getString("accessToken");
                tokenType = response.jsonPath().getString("token_type");

                if (token != null) {
                    String authHeader = (tokenType != null) ? tokenType + " " + token : token;
                    AuthTokens.setAdminToken(authHeader);
                    System.out.println("Admin authentication successful (alternative path)!");
                } else {
                    throw new RuntimeException("Admin authentication failed - no token found");
                }
            }
        } else {
            System.err.println("Admin login failed! Status: " + response.statusCode());
            System.err.println("Response: " + response.asString());
            throw new RuntimeException("Admin authentication failed with status: " + response.statusCode());
        }
    }
}