package com.shashank.spendistrybusiness.Models.CreateInvoice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class UserInvoices {

    @SerializedName("_id")
    private String email;
    private ArrayList<BusinessInvoices> businessName;

    public UserInvoices(String email, ArrayList<BusinessInvoices> businessName) {
        this.email = email;
        this.businessName = businessName;
    }

    public UserInvoices(ArrayList<BusinessInvoices> businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<BusinessInvoices> getBusinessName() {
        return businessName;
    }
}
