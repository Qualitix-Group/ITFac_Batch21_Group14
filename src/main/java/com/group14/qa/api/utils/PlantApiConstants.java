package com.group14.qa.api.utils;

public class PlantApiConstants {

    public static final String BASE_URL = "http://localhost:8080";

    public static final String CREATE_PLANT_WITH_CATEGORY =
            "/api/plants/category/{categoryId}";

    public static final String GET_ALL_PLANTS =
            "/api/plants";

    public static final String GET_PLANT_BY_ID =
            "/api/plants/{id}";

    public static final String GET_PAGED_PLANTS =
            "/api/plants/paged";

    public static final String GET_PLANT_SUMMARY =
            "/api/plants/summary";

    public static final String UPDATE_PLANT_BY_ID =
            "/api/plants/{id}";

}
