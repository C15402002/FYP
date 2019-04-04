package com.example.myapplication.model;

public class Review {
    private String userPhone, menuId, rate, comment, restaurantId;

    public Review() {

    }

    public Review(String userPhone, String menuId, String rate, String comment, String restaurantId) {
        this.userPhone = userPhone;
        this.menuId = menuId;
        this.rate = rate;
        this.comment = comment;
        this.restaurantId = restaurantId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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
