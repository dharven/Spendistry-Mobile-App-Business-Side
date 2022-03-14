package com.shashank.spendistrybusiness.Models;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class Vendor {

    @SerializedName("fname")
    private String firstName;

    @SerializedName("lname")
    private String lastName;
    private String email;

    @SerializedName("_id")
    private String vendorId;

    @SerializedName("vendorName")
    private String businessName;

    private String mobileNumber;
    private String currentInvoiceNumber;
    private String panNumber;
    private String gstNumber;
    private String address;
    private String city;
    private String state;
    private String tollFreeNumber;
    private String website;
    private String lat;
    private String lng;

    @Ignore
    public Vendor() {
    }

    public Vendor(String firstName, String lastName, String email, String businessName, String mobileNumber, String panNumber, String gstNumber, String address, String city, String state, String tollFreeNumber, String website) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.businessName = businessName;
        this.mobileNumber = mobileNumber;
        this.panNumber = panNumber;
        this.gstNumber = gstNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.tollFreeNumber = tollFreeNumber;
        this.website = website;
        this.vendorId = email;
    }



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getCurrentInvoiceNumber() {
        return currentInvoiceNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getTollFreeNumber() {
        return tollFreeNumber;
    }

    public String getWebsite() {
        return website;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getVendorId() {
        return vendorId;
    }
}
