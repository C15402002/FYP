package com.example.angelapeng.oui.model;

public class User {
    String Firstname;


    String email_input;


    public User (){};
    public User(String displayfname, String email){
        this.Firstname=displayfname;
        this.email_input=email;
    }


    public String getDisplayfname() {
        return Firstname;
    }

    public String getEmail() {
        return email_input;
    }


}
