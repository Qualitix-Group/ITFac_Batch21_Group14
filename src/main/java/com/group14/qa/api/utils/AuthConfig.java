package com.group14.qa.api.utils;

public class AuthConfig {

    // Admin token (shared across all API tests)
    public static final String TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzcwNDE3MDI0LCJleHAiOjE3NzA0MjA2MjR9.KYeRqyXZj6hcwWQqyVs0zkMrKxVgnM07yn84OP-gyJ8";

    public static final String USER_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzA0MzY4NjcsImV4cCI6MTc3MDQ0MDQ2N30.IOoiuUqss-20zJJLrhjtqZyMOHFRofapfhi6BVjrGrQ";

    private AuthConfig() {
        // prevent object creation
    }
}
