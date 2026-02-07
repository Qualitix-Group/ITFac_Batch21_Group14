package com.group14.qa.api.utils;

public class PlantApiConstants {

    public static final String BASE_URL = "http://localhost:8080";

    /* ---------- CREATE ---------- */

    // POST - Create plant under category
    public static final String CREATE_PLANT_WITH_CATEGORY =
            "/api/plants/category/{categoryId}";

    /* ---------- READ ---------- */

    // GET - All plants
    public static final String GET_ALL_PLANTS =
            "/api/plants";

    // GET - Plant by ID
    public static final String GET_PLANT_BY_ID =
            "/api/plants/{id}";

    // GET - Paged plants
    public static final String GET_PAGED_PLANTS =
            "/api/plants/paged";

    // GET - Plant summary
    public static final String GET_PLANT_SUMMARY =
            "/api/plants/summary";

    /* ---------- UPDATE ---------- */

    // PUT - Update plant by ID
    public static final String UPDATE_PLANT_BY_ID =
            "/api/plants/{id}";

    /* ---------- DELETE ---------- */

    // DELETE - Delete plant by ID
    public static final String DELETE_PLANT_BY_ID =
            "/api/plants/{id}";
}
