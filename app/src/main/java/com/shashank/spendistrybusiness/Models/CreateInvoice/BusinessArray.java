package com.shashank.spendistrybusiness.Models.CreateInvoice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BusinessArray {

    @SerializedName("_id")
    private String email;
    private ArrayList<Invoice> invoices;

    public BusinessArray(String email, ArrayList<Invoice> invoices) {
        this.email = email;
        this.invoices = invoices;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }
}
