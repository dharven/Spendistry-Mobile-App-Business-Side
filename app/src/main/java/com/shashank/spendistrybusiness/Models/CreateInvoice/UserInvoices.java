package com.shashank.spendistrybusiness.Models.CreateInvoice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserInvoices {

    @SerializedName("_id")
    private String email;
    private ArrayList<BusinessArray> businessName;

    public UserInvoices(String email, ArrayList<BusinessArray> businessName) {
        this.email = email;
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<BusinessArray> getBusinessName() {
        return businessName;
    }
}
