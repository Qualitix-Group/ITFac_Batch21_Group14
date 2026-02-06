package com.group14.qa.api.utils;

public class PlantUpdateTestData {

    public static class UpdatePlantRequest {
        private String name;
        private float price;
        private int quantity;
        private int categoryId;

        public UpdatePlantRequest(String name, float price, int quantity, int categoryId) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.categoryId = categoryId;
        }

        public String toJson() {
            return String.format(
                    "{\"name\": \"%s\", \"price\": %.2f, \"quantity\": %d, \"category\": {\"id\": %d}}",
                    name, price, quantity, categoryId
            );
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public float getPrice() { return price; }
        public void setPrice(float price) { this.price = price; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public int getCategoryId() { return categoryId; }
        public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    }

    public static UpdatePlantRequest getValidUpdateRequest() {
        String name = "Updated_Plant_" + System.currentTimeMillis();
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        return new UpdatePlantRequest(name, 150.0f, 25, 3);
    }

    public static UpdatePlantRequest getPriceUpdateRequest() {
        String name = "Price_Updated_" + System.currentTimeMillis();
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        return new UpdatePlantRequest(name, 199.99f, 50, 3);
    }

    public static UpdatePlantRequest getQuantityUpdateRequest() {
        String name = "Qty_Updated_" + System.currentTimeMillis();
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        return new UpdatePlantRequest(name, 100.0f, 100, 3);
    }
}