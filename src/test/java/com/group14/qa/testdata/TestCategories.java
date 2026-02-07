package com.group14.qa.testdata;

public class TestCategories {

    // Category names for testing
    public static class Names {
        public static final String FLOWERING_PLANTS = "Flowering Plants";
        public static final String INDOOR_PLANTS = "Indoor Plants";
        public static final String OUTDOOR_PLANTS = "Outdoor Plants";
        public static final String SUCCULENTS = "Succulents";
        public static final String HERBS = "Herbs";

        // Get a random category name for testing
        public static String getRandomCategory() {
            String[] categories = {FLOWERING_PLANTS, INDOOR_PLANTS, OUTDOOR_PLANTS, SUCCULENTS, HERBS};
            return categories[(int) (Math.random() * categories.length)];
        }
    }

    // Category descriptions
    public static class Descriptions {
        public static final String FLOWERING_PLANTS_DESC = "Plants that produce flowers";
        public static final String INDOOR_PLANTS_DESC = "Plants suitable for indoor environments";
        public static final String OUTDOOR_PLANTS_DESC = "Plants for outdoor gardens";
    }

    // Test category for edit operations
    public static class EditTest {
        public static final String CATEGORY_TO_EDIT = "Test Category for Edit";
        public static final String UPDATED_NAME = "Updated Test Category";
        public static final String UPDATED_DESC = "Updated description for testing";
    }
}