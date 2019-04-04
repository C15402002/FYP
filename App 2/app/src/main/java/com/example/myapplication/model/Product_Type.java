package com.example.myapplication.model;

public class Product_Type {
    private String pName, pImage;

    public Product_Type(){

    }

    public Product_Type(String name, String image){
        pName = name;
        pImage = image;
    }

    public String getName(){
        return pName;
    }

    public void setName(String name){
        pName = name;
    }
    public String getImage(){
        return pImage;
    }

    public void setImage(String image){
        pImage = image;
    }
}
