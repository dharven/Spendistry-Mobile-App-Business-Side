package com.shashank.spendistrybusiness.Models;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

@Entity(tableName = "dashboard")
public class Dashboard {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @SerializedName("_id")
    @ColumnInfo(name = "email")
    @NonNull
    private String email;


    private long monthlyIncome;
    private long yearlyIncome;
    private long totalIncome;
    private long issuedInvoices;

    private ArrayList<Integer> roundoff;

    private Vendor vendorDetails;


    private int reportCount;


    public Dashboard(String email, long monthlyIncome, long yearlyIncome, long totalIncome, long issuedInvoices, ArrayList<Integer> roundoff, Vendor vendorDetails, int reportCount) {
        this.email = email;
        this.monthlyIncome = monthlyIncome;
        this.yearlyIncome = yearlyIncome;
        this.totalIncome = totalIncome;
        this.issuedInvoices = issuedInvoices;
        this.roundoff = roundoff;
        this.vendorDetails = vendorDetails;
        this.reportCount = reportCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    public long getYearlyIncome() {
        return yearlyIncome;
    }

    public long getTotalIncome() {
        return totalIncome;
    }

    public long getIssuedInvoices() {
        return issuedInvoices;
    }

    public ArrayList<Integer> getRoundoff() {
        return roundoff;
    }

    public Vendor getVendorDetails() {
        return vendorDetails;
    }

    public int getReportCount() {
        return reportCount;
    }
}
