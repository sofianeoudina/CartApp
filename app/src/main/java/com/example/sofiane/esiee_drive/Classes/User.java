package com.example.sofiane.esiee_drive.Classes;

/**
 * Created by Sofiane on 17/01/2017.
 */

public class User {
    String user_name;
    String user_family_name;
    String email;
    String user_id;

    public User(String user_name, String user_family_name, String email) {
        this.user_name = user_name;
        this.user_family_name = user_family_name;

        this.email = email;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail(){
        return email;
    }
    public String getUid(){
        return user_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public String getUser_family_name() {
        return user_family_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_family_name='" + user_family_name + '\'' +
                '}';
    }
}
