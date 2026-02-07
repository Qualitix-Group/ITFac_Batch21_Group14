//package com.group14.qa.common;
//
//import com.group14.qa.api.clients.AuthApiClient;
//import io.cucumber.java.Before;
//
//public class UserAuthHook {
//
//    @Before("@user")
//    public void loginAsUser() {
//        AuthApiClient client = new AuthApiClient();
//        var response = client.login("testuser", "test123");
//
//        // Debug: Print response to see actual structure
//        System.out.println("Login Response: " + response.asString());
//
//        // Check if login was successful
//        if (response.statusCode() == 200) {
//            // Extract token and tokenType properly
//            String token = response.path("token");  // Using path() instead of jsonPath()
//            String tokenType = response.path("tokenType");
//
//            // Check if we got the values
//            System.out.println("Token: " + token);
//            System.out.println("TokenType: " + tokenType);
//
//            if (token != null && tokenType != null) {
//                AuthTokens.setUserToken(tokenType + " " + token);
//                System.out.println("Full Auth Header: " + tokenType + " " + token);
//            } else {
//                System.err.println("Token or tokenType is null!");
//            }
//        } else {
//            System.err.println("Login failed with status: " + response.statusCode());
//            System.err.println("Response: " + response.asString());
//        }
//    }
//}

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

            // Debug output
            System.out.println("User Token: " + token);
            System.out.println("User Token Type: " + tokenType);

            if (token != null && tokenType != null) {
                AuthTokens.setUserToken(tokenType + " " + token);
                System.out.println("User authentication successful!");
            } else {
                System.err.println("Failed to extract user token from response");
                // Try alternative JSON paths
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