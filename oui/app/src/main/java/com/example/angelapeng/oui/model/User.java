package com.example.angelapeng.oui.model;

public class User {
    private String fName;
    private String email_input;
    private String password_input;


    public User (){};

    public User(String name, String email, String password){
       fName=name;
       email_input=email;
      password_input = password;

    }

    public String getfName() {
        return fName;
    }
    public void setfName(String name) {
        fName = name;
    }
    public String getEmail() {
        return email_input;
    }
    public void setEmail_input(String email) {
        email_input = email;
    }

    public String getPassword_input() {
        return password_input;
    }

    public void setPassword_input(String password_input) {
        this.password_input = password_input;
    }
}
