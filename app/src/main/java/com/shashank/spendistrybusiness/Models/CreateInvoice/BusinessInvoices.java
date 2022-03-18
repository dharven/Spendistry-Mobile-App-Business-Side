package com.shashank.spendistrybusiness.Models.CreateInvoice;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "business_invoices")
public class BusinessInvoices {
    @PrimaryKey
    @SerializedName("_id")
    @NotNull
    private String email;

    private ArrayList<Invoice> invoices;



    public BusinessInvoices(String email, ArrayList<Invoice> invoices) {
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
