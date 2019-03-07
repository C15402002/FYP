package com.example.appserver.model;

import java.util.List;

public class MakeOrder {
    private String phone, email, table, name, total, status, notes, paymentProcess;
    private List<Order> listOfOrderPlaced;

    public MakeOrder() {

    }



    public MakeOrder(String phone, String name, String email, String table, String total, String notes, String paymentProcess, List<Order> listOfOrderPlaced) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.table = table;
        this.total = total;
        this.notes = notes;
        this.status = "0";
        this.paymentProcess = paymentProcess;
        this.listOfOrderPlaced = listOfOrderPlaced;
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
