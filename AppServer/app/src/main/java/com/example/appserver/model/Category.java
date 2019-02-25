package com.example.appserver.model;

public class Category {
    private String pName, pImage;

    public Category(){

    }

    public Category(String name, String image){
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
