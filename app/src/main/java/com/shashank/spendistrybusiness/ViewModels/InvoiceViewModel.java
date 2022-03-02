package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.Repository.InventoryRepository;
import com.shashank.spendistrybusiness.Repository.InvoiceRepository;

import java.util.ArrayList;
import java.util.List;

public class InvoiceViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    InvoiceRepository invoiceRepository;
    InventoryRepository inventoryRepository;

    public InvoiceViewModel(Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        inventoryRepository = new InventoryRepository(application);
    }

    public LiveData<List<ItemPrices>> getItemPrices(String email) {
        return inventoryRepository.getInventory(email);
    }

    public LiveData<List<ItemPrices>> setInvoice(ItemPrices itemPrices) {
        return invoiceRepository.setInvoice(itemPrices);
    }




}