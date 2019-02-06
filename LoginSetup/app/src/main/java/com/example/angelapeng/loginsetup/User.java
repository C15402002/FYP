package com.example.angelapeng.loginsetup;

class User {
    String Displayname;


    String email_input;

    long createdAt;

    public User (){};
    public User(String displayname,String email,long createdAt){
        this.Displayname=displayname;
        this.email_input=email;
        this.createdAt=createdAt;
    }


    public String getDisplayname() {
        return Displayname;
    }

    public String getEmail() {
        return email_input;
    }

    public long getCreatedAt() {
        return createdAt;
    }

}