package com.example.myapplication.model;

public class Review {
    private String userPhone, menuId, rate, comment;

    public Review() {

    }

    public Review(String userPhone, String menuId, String rate, String comment) {
        this.userPhone = userPhone;
        this.menuId = menuId;
        this.rate = rate;
        this.comment = comment;
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
}
