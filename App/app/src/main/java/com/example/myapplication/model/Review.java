package com.example.myapplication.model;

public class Review {
    private String name, menuId, rate, comment, restaurantId;

    public Review() {

    }

    public Review(String name, String menuId, String rate, String comment, String restaurantId) {
        this.name = name;
        this.menuId = menuId;
        this.rate = rate;
        this.comment = comment;
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
