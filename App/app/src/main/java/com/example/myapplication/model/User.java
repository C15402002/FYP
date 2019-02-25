package com.example.myapplication.model;

public class User {
    private String Name, Email, Phone, Password, IsStaff;


//    String phone,
    public User (){};

    public User(String name, String email, String password){
       Name=name;
       Email=email;
//       Phone = phone;
       Password = password;
       IsStaff = "false";

    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
