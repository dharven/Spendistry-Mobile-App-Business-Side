package com.shashank.spendistrybusiness.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserInvoices {

    @SerializedName("_id")
    private String email;
    private ArrayList<Invoice> businessName;

    public UserInvoices(String email, ArrayList<Invoice> businessName) {
        this.email = email;
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Invoice> getBusinessName() {
        return businessName;
    }
}
