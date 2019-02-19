package com.example.angelapeng.oui.model;

public class User {
    private String Firstname;
    private String email_input;
    private String password_input;


    public User (){};
    public User(String name, String email, String password){
        this.Firstname=name;
        this.email_input=email;
        this.password_input = password;

    }


    public String getDisplayfname() {
        return Firstname;
    }

    public String getEmail() {
        return email_input;
    }

    public String getPassword_input() {
        return password_input;
    }
}
