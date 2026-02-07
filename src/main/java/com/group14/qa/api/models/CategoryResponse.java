package com.group14.qa.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // <-- Ignore extra fields like "error" or others
public class CategoryResponse {

    private Long id;
    private String name;
    private String parent;
    private List<String> subCategories;
    private String status;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public List<String> getSubCategories() {
        return subCategories;
    }

    public String getStatus() {
        return status;
    }
}
