package com.group14.qa.api.models;

public class CreateCategoryRequest {

    private String name;
    private Long parentId;

    public CreateCategoryRequest() {}

    public CreateCategoryRequest(String name, Long parentId) {
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
