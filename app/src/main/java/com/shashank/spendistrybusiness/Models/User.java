package com.shashank.spendistrybusiness.Models;

@SuppressWarnings("ALL")
public class User {
    private String _id;
    private String fname;
    private String lname;
    private String mobileNumber;

    public User(String _id, String fname, String lname, String mobileNumber) {
        this._id = _id;
        this.fname = fname;
        this.lname = lname;
        this.mobileNumber = mobileNumber;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
