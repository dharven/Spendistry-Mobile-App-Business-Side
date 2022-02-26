package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.Repository.InvoiceRepository;

import java.util.ArrayList;

public class ManualInvoiceViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    InvoiceRepository invoiceRepository;

    public ManualInvoiceViewModel(Application application) {
        invoiceRepository = new InvoiceRepository(application);
    }






}