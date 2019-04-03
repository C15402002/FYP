package com.example.appserver.model;

import java.util.List;

public class MakeOrder {
    private String phone, email, table, name, total, status, notes, paymentProcess, restaurantId, paymentMethods;
    private List<Order> listOfOrderPlaced;

    public MakeOrder() {

    }

    public MakeOrder(String phone, String email, String table, String name, String total, String status, String notes, String paymentProcess, String restaurantId, String paymentMethods, List<Order> listOfOrderPlaced) {
        this.phone = phone;
        this.email = email;
        this.table = table;
        this.name = name;
        this.total = total;
        this.status = status;
        this.notes = notes;
        this.paymentProcess = paymentProcess;
        this.restaurantId = restaurantId;
        this.paymentMethods = paymentMethods;
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

    public String getPaymentProcess() {
        return paymentProcess;
    }

    public void setPaymentProcess(String paymentProcess) {
        this.paymentProcess = paymentProcess;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public List<Order> getListOfOrderPlaced() {
        return listOfOrderPlaced;
    }

    public void setListOfOrderPlaced(List<Order> listOfOrderPlaced) {
        this.listOfOrderPlaced = listOfOrderPlaced;
    }
}
