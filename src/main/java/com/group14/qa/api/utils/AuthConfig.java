package com.group14.qa.api.utils;

public class AuthConfig {

    // Admin token (shared across all API tests)
    public static final String TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzcwNDUzODM5LCJleHAiOjE3NzA0NTc0Mzl9.TabjOWs6FlBVCvJlu3XFal9xLMQgsy70HITCiAY3Loo";

    public static final String USER_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzA0NTQzOTUsImV4cCI6MTc3MDQ1Nzk5NX0.QGJDIQUckKkeftXZsaOyVthrEMZsr3Hkl58gqk-hfj8";

    private AuthConfig() {
        // prevent object creation
    }
}
