package com.example.myapplication.model;

import java.util.List;

public class MakeOrder {
    private String phone, email, name, total, status;
    private List<Order> listOfOrderPlaced;

    public MakeOrder() {

    }

    public MakeOrder(String phone, String email, String name, String total, List<Order> listOfOrderPlaced) {
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.total = total;
        this.listOfOrderPlaced = listOfOrderPlaced;
        this.status = "0";
        //0 for order placed, 1 for order cooking, 2 for order sent
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getListOfOrderPlaced() {
        return listOfOrderPlaced;
    }

    public void setListOfOrderPlaced(List<Order> listOfOrderPlaced) {
        this.listOfOrderPlaced = listOfOrderPlaced;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
