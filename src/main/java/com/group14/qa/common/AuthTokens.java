package com.group14.qa.common;

public class AuthTokens {

    private static String adminToken;
    private static String userToken;

    public static void setAdminToken(String token) {
        adminToken = token;
    }

    public static String getAdminToken() {
        return adminToken;
    }

    public static void setUserToken(String token) {
        userToken = token;
    }

    public static String getUserToken() {
        return userToken;
    }
}
