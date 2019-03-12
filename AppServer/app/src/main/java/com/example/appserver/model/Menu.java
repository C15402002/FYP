package com.example.appserver.model;

public class Menu {
    private String fdName, fdDescription, fdImage, fdPrice, foodId;

    public Menu(){

    }

    public Menu(String name, String description, String image, String price, String fdId) {
        fdName = name;
        fdDescription = description;
        fdImage = image;
        fdPrice = price;
        foodId = fdId;
    }

    public String getDescription() {
        return fdDescription;
    }

    public void setDescription(String description) {
        fdDescription = description;
    }

    public String getImage() {
        return fdImage;
    }

    public void setImage(String image) {
        fdImage = image;
    }

    public String getName() {
        return fdName;
    }

    public void setName(String name) {
        fdName = name;
    }

    public String getPrice() {
        return fdPrice;
    }

    public void setPrice(String price) {
        fdPrice = price;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
