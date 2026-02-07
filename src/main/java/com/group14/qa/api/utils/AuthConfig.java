package com.group14.qa.api.utils;

public class AuthConfig {

    // Admin token (shared across all API tests)
    public static final String TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzcwNDE3MDI0LCJleHAiOjE3NzA0MjA2MjR9.KYeRqyXZj6hcwWQqyVs0zkMrKxVgnM07yn84OP-gyJ8";

    public static final String USER_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzA0MzI1OTksImV4cCI6MTc3MDQzNjE5OX0.G2OzJ1KoaQG_xd9xQkyDSG5JTqfG35nYVZB3o-rkx6U";

    private AuthConfig() {
        // prevent object creation
    }
}
