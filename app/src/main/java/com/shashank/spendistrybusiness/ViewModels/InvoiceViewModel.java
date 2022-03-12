package com.shashank.spendistrybusiness.ViewModels;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shashank.spendistrybusiness.Adapters.InventoryAdapter;
import com.shashank.spendistrybusiness.Constants.GlobalVariables;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessArray;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
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
    MutableLiveData<List<ItemPrices>> data = GlobalVariables.data;
    ArrayList<ItemPrices> itemPricesArrayList = GlobalVariables.itemPricesArrayList;

    public InvoiceViewModel(Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        inventoryRepository = new InventoryRepository(application);
    }



    public LiveData<List<ItemPrices>> getItemPrices(String email) {
        return inventoryRepository.getInventory(email);
    }

    public void setInvoice(ItemPrices itemPrices) {
        if (itemPrices.getPrice() != null) {
            itemPricesArrayList.add(itemPrices);
            data.setValue(itemPricesArrayList);
        }
        else {
            itemPricesArrayList.clear();
            data.setValue(itemPricesArrayList);
        }

    }

    public LiveData<List<ItemPrices>> getInvoice() {
        return data;
    }

    public void addInvoice(String email, String businessEmail, BusinessArray invoice) {
        invoiceRepository.addInvoice(email, businessEmail, invoice);
    }

}