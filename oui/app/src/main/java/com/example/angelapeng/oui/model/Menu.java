package com.example.angelapeng.oui.model;

public class Menu {
    private String Name, Description, Image, Price, ProductTypeID;

    public Menu(){

    }

    public Menu(String name, String description, String image, String price, String productTypeId) {
        Name = name;
        Description = description;
        Image = image;
        Price = price;
        ProductTypeID = productTypeId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductTypeID() {
        return ProductTypeID;
    }

    public void setProductTypeID(String productTypeId) {
        ProductTypeID = productTypeId;
    }
}
