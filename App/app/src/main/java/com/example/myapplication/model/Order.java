package com.example.myapplication.model;

public class Order {
    private String UserPhone,ProductId, RestID, ProductName, Quantity, Price;

    public Order() {
    }

    public Order(String userPhone, String productId, String restID, String productName, String quantity, String price) {
        UserPhone = userPhone;
        ProductId = productId;
        RestID = restID;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getRestID() {
        return RestID;
    }

    public void setRestID(String restID) {
        RestID = restID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
