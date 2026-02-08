package com.group14.qa.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantSummaryResponse {

    @JsonProperty("totalPlants")
    private int totalPlants;

    @JsonProperty("lowStockPlants")
    private int lowStockPlants;

    public int getTotalPlants() {
        return totalPlants;
    }

    public void setTotalPlants(int totalPlants) {
        this.totalPlants = totalPlants;
    }

    public int getLowStockPlants() {
        return lowStockPlants;
    }

    public void setLowStockPlants(int lowStockPlants) {
        this.lowStockPlants = lowStockPlants;
    }
}