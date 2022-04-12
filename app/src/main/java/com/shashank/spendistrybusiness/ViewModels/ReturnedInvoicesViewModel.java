package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Repository.InvoiceRepository;

import java.util.ArrayList;

public class ReturnedInvoicesViewModel extends AndroidViewModel {
    InvoiceRepository invoiceRepository;
    MutableLiveData<ArrayList<Invoice>> returnedInvoices;
    public ReturnedInvoicesViewModel(@NonNull Application application, String businessEmail) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        returnedInvoices = invoiceRepository.getReturnedInvoices(businessEmail);
    }

    public MutableLiveData<ArrayList<Invoice>> getReturnedInvoices() {
        return returnedInvoices;
    }
}
