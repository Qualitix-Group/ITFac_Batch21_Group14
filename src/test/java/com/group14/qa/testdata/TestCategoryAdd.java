package com.group14.qa.testdata;

public class TestCategoryAdd {

    // Test data for category creation
    public static class CreateCategory {
        // Valid category names
        public static final String VALID_CATEGORY_NAME = "Summer Flowers";
        public static final String VALID_SHORT_NAME = "Test";
        public static final String VALID_MAX_LENGTH_NAME = "1234567890"; // 10 characters

        // Invalid category names
        public static final String LESS_THAN_3_CHARS = "Ab";
        public static final String EXACT_2_CHARS = "FL";
        public static final String SINGLE_CHAR = "A";
        public static final String EMPTY_NAME = "";

        // More than 10 characters
        public static final String MORE_THAN_10_CHARS = "Summer Flowers and Plants";
        public static final String EXACT_11_CHARS = "12345678901";
        public static final String VERY_LONG_NAME = "This is a very long category name that exceeds the character limit";

        // Parent categories
        public static final String PARENT_CATEGORY_ROSES = "Roses1";
        public static final String PARENT_CATEGORY_SPRING = "Spring1";
        public static final String NO_PARENT = "Main Category";

        // Special characters
        public static final String WITH_SPECIAL_CHARS = "Summer-Flowers";
        public static final String WITH_NUMBERS = "Flowers2024";
        public static final String WITH_SPACES = "  Summer Flowers  ";

        // Edge cases
        public static final String EXACT_3_CHARS = "ABC";
        public static final String EXACT_10_CHARS = "ABCDEFGHIJ";

        // Duplicate names (assuming these exist)
        public static final String DUPLICATE_NAME = "Existing Category";
    }

    // Error messages expected
    public static class ErrorMessages {
        public static final String LESS_THAN_3_ERROR = "Category name is required at least 3 characters";
        public static final String MORE_THAN_10_ERROR = "Category name can not be include more than 10 characters";
        public static final String REQUIRED_ERROR = "Category name is required";
        public static final String DUPLICATE_ERROR = "Category name already exists";

        // Success messages
        public static final String SUCCESS_CREATION = "Category created successfully";
        public static final String SUCCESS_UPDATE = "Category updated successfully";
        public static final String SUCCESS_DELETE = "Category deleted successfully";
    }

    // Page URLs
    public static class URLs {
        public static final String CATEGORIES_LIST = "/ui/categories";
        public static final String ADD_CATEGORY = "/ui/categories/add";
        public static final String EDIT_CATEGORY = "/ui/categories/edit/";
    }

    // Test priorities
    public static class Priority {
        public static final String HIGH = "High";
        public static final String MEDIUM = "Medium";
        public static final String LOW = "Low";
    }
}