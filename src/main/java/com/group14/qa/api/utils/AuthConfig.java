package com.group14.qa.api.utils;

public class AuthConfig {

    // Admin token (shared across all API tests)
    public static final String TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzcwNDM4MjE3LCJleHAiOjE3NzA0NDE4MTd9.4ZaTraEUex0elQxV0feqYI2vOcxoT6ztIqmxVza7BlM";

    public static final String USER_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzA0MzY4NjcsImV4cCI6MTc3MDQ0MDQ2N30.IOoiuUqss-20zJJLrhjtqZyMOHFRofapfhi6BVjrGrQ";

    private AuthConfig() {
        // prevent object creation
    }
}
