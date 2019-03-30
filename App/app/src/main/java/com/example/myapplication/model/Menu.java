package com.example.myapplication.model;

public class Menu {

    private String Name, Image, Description, Price, FoodId;

    public Menu() {
    }

    public Menu(String name, String image, String description, String price, String foodId) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        FoodId = foodId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

//    public String getMenuId() {
//        return MenuId;
//    }
//
//    public void setMenuId(String menuId) {
//        MenuId = menuId;
//    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }
}
