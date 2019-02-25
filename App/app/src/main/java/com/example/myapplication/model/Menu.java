package com.example.myapplication.model;

public class Menu {
    private String fdName, fdDescription, fdImage, fdPrice;

    public Menu(){

    }

    public Menu(String name, String description, String image, String price) {
        fdName = name;
        fdDescription = description;
        fdImage = image;
        fdPrice = price;
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

}
