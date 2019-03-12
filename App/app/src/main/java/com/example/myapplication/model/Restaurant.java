package com.example.myapplication.model;

public class Restaurant {
    private String restaurantId, image, name;

    public Restaurant() {
    }

    public Restaurant(String restaurantId, String image, String name) {
        this.restaurantId = restaurantId;
        this.image = image;
        this.name = name;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
