package com.example.myapplication.model;

import java.util.List;

public class MakeOrder {
    private String phone, email, table, name, total, status, notes;
    private List<Order> listOfOrderPlaced;

    public MakeOrder() {
    }

    public MakeOrder(String phone, String email, String table, String name, String total, String notes, List<Order> listOfOrderPlaced) {
        this.phone = phone;
        this.email = email;
        this.table = table;
        this.name = name;
        this.total = total;
        this.status = "0";
        this.notes = notes;
        this.listOfOrderPlaced = listOfOrderPlaced;
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Order> getListOfOrderPlaced() {
        return listOfOrderPlaced;
    }

    public void setListOfOrderPlaced(List<Order> listOfOrderPlaced) {
        this.listOfOrderPlaced = listOfOrderPlaced;
    }
}
