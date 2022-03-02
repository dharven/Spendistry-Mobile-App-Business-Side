package com.shashank.spendistrybusiness.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Invoice {

    @SerializedName("_id")
    private String invoiceId;

    @SerializedName("invoiceTotalitems")
    private ArrayList<ItemPrices> totalItems;

    @SerializedName("invoiceNumber")
    private String invoiceNumber;

    @SerializedName("invoiceDate")
    private String Date;

    @SerializedName("invoiceAmount")
    private double Total;

    @SerializedName("invoiceTitle")
    private String Title;

    @SerializedName("invoiceDescription")
    private String Description;

    private double discount;

    @SerializedName("invoiceIGST")
    private double IGST;

    @SerializedName("invoiceCGST")
    private double CGST;

    @SerializedName("invoiceSGST")
    private double SGST;

    @SerializedName("invoiceUTGST")
    private double UTGST;

    @SerializedName("invoiceSentTo")
    private String SentTo;

    @SerializedName("invoiceSentBy")
    private String SentBy;

    @SerializedName("invoicePaymentMode")
    private String PaymentMode;

    @SerializedName("invoicePDF")
    private String pdfLink;

    @SerializedName("invoiceReport")
    private String Report;

    @SerializedName("roundoff")
    private double roundOff;

    private String city;

    public Invoice(String invoiceId, ArrayList<ItemPrices> totalItems, String invoiceNumber,
                   double total, String title, String description, double discount, double IGST,
                   double CGST, double SGST, double UTGST, String sentTo, String sentBy, String paymentMode,
                   double roundOff, String city) {
        this.invoiceId = invoiceId;
        this.totalItems = totalItems;
        this.invoiceNumber = invoiceNumber;
        Total = total;
        Title = title;
        Description = description;
        this.discount = discount;
        this.IGST = IGST;
        this.CGST = CGST;
        this.SGST = SGST;
        this.UTGST = UTGST;
        SentTo = sentTo;
        SentBy = sentBy;
        PaymentMode = paymentMode;
        this.roundOff = roundOff;
        this.city = city;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public ArrayList<ItemPrices> getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(ArrayList<ItemPrices> totalItems) {
        this.totalItems = totalItems;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getIGST() {
        return IGST;
    }

    public void setIGST(double IGST) {
        this.IGST = IGST;
    }

    public double getCGST() {
        return CGST;
    }

    public void setCGST(double CGST) {
        this.CGST = CGST;
    }

    public double getSGST() {
        return SGST;
    }

    public void setSGST(double SGST) {
        this.SGST = SGST;
    }

    public double getUTGST() {
        return UTGST;
    }

    public void setUTGST(double UTGST) {
        this.UTGST = UTGST;
    }

    public String getSentTo() {
        return SentTo;
    }

    public void setSentTo(String sentTo) {
        SentTo = sentTo;
    }

    public String getSentBy() {
        return SentBy;
    }

    public void setSentBy(String sentBy) {
        SentBy = sentBy;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public double getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(double roundOff) {
        this.roundOff = roundOff;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
