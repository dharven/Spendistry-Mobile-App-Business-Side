package com.shashank.spendistrybusiness.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("ALL")
public class Auth{

    @SerializedName("_id")
    private String email;
    private String password;


    public Auth(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
