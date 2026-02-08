package com.group14.qa.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryResponse {

    private Long id;
    private String name;

    private String parent;

    private List<CategoryResponse> subCategories;

    private String status;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getParent() { return parent; }
    public List<CategoryResponse> getSubCategories() { return subCategories; }
    public String getStatus() { return status; }
}