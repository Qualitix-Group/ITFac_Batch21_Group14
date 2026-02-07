package com.group14.qa.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantResponse {

    private Long id;
    private String name;
    private Double price;
    private Integer quantity;

    @JsonProperty("category")
    private CategoryInfo category;

    @JsonProperty("categoryId")
    private Long categoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CategoryInfo getCategory() {
        return category;
    }

    public void setCategory(CategoryInfo category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getEffectiveCategoryId() {
        if (category != null && category.getId() != null) {
            return category.getId();
        }
        return categoryId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CategoryInfo {
        private Long id;
        private String name;
        private List<String> subCategories;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getSubCategories() {
            return subCategories;
        }

        public void setSubCategories(List<String> subCategories) {
            this.subCategories = subCategories;
        }
    }
}