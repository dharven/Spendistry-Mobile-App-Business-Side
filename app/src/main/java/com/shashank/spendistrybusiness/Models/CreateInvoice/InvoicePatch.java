package com.shashank.spendistrybusiness.Models.CreateInvoice;

import com.google.gson.annotations.SerializedName;

public class InvoicePatch {

    @SerializedName("invoices")
    private Invoice invoices;

    public InvoicePatch(Invoice invoices) {
        this.invoices = invoices;
    }

    public Invoice getInvoices() {
        return invoices;
    }

    public void setInvoices(Invoice invoices) {
        this.invoices = invoices;
    }
}
