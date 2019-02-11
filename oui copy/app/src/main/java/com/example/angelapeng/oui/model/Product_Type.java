package com.example.angelapeng.oui.model;

public class Product_Type {
    private String Name, Image;

    public Product_Type(){

    }

    public Product_Type(String pname, String pimage){
        Name = pname;
        Image = pimage;
    }

    public String getName(){
        return Name;
    }

    public void setName(String pname){
        Name = pname;
    }
    public String getImage(){
        return Image;
    }

    public void setImage(String pimage){
        Image = pimage;
    }

}
