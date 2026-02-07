// src/test/java/com/group14/qa/testdata/TestUsers.java
package com.group14.qa.testdata;

public class TestUsers {

    public static class Admin {
        public static final String USERNAME = "admin";
        public static final String PASSWORD = "admin123";
        public static final String ROLE = "ROLE_ADMIN";
        public static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzA0NzAzNDYsImV4cCI6MTc3MDQ3Mzk0Nn0.ZDxAXcJIjD_7mdbwx8fpbOHPFU6fcu40Nn85-QMMOfo";
    }



    public static class RegularUser {
        public static final String USERNAME = "testuser";
        public static final String PASSWORD = "test123";
        public static final String ROLE = "ROLE_USER";

        public static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzA0NzAzNDYsImV4cCI6MTc3MDQ3Mzk0Nn0.ZDxAXcJIjD_7mdbwx8fpbOHPFU6fcu40Nn85-QMMOfo";
    }

    public static class InvalidUser {
        public static final String USERNAME = "invalid";
        public static final String PASSWORD = "wrongpass";
    }
}