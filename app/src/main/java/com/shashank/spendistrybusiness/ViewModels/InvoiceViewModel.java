package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Constants.Global;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessInvoices;
import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Repository.DashboardRepository;
import com.shashank.spendistrybusiness.Repository.InventoryRepository;
import com.shashank.spendistrybusiness.Repository.InvoiceRepository;

import java.util.ArrayList;
import java.util.List;

public class InvoiceViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    InvoiceRepository invoiceRepository;
    InventoryRepository inventoryRepository;
    MutableLiveData<List<ItemPrices>> data = Global.data;
    ArrayList<ItemPrices> itemPricesArrayList = Global.itemPricesArrayList;
    DashboardRepository dashboardRepository;

    public InvoiceViewModel(Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        inventoryRepository = new InventoryRepository(application);
        dashboardRepository = new DashboardRepository(application);
    }

    public LiveData<Dashboard> getDashBoardFromDB(String email){
        return dashboardRepository.getDashBoardFromDB(email);
    }


    public LiveData<List<ItemPrices>> getItemPrices(String email) {
        return inventoryRepository.getInventory(email);
    }

    public LiveData<BusinessInvoices> getBusinessInvoices(String email){
        return invoiceRepository.getBusinessInvoices(email);
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

    public void addInvoice(String email, String businessEmail, BusinessInvoices invoice) {
        invoiceRepository.addInvoice(email, businessEmail, invoice);
    }

}