package com.group14.qa.api.utils;

public class AuthConfig {

    // Admin token (shared across all API tests)
    public static final String TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzcwNDE3MDI0LCJleHAiOjE3NzA0MjA2MjR9.KYeRqyXZj6hcwWQqyVs0zkMrKxVgnM07yn84OP-gyJ8";

    public static final String USER_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzA0MTc1MDksImV4cCI6MTc3MDQyMTEwOX0.tQ-csVHmV6xVjBiQ6fMo78XtOZH4k56DPZj0FIpe-ro";

    private AuthConfig() {
        // prevent object creation
    }
}
