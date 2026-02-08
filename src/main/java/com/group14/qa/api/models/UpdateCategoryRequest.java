package com.group14.qa.api.models;

public class UpdateCategoryRequest {

    private String name;
    private Long parentId;

    public UpdateCategoryRequest() {}

    public UpdateCategoryRequest(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public Long getParentId() {
        return parentId;
    }
}
