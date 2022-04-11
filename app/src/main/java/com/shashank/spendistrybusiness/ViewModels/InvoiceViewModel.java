package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Constants.Global;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessInvoices;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.Report;
import com.shashank.spendistrybusiness.Repository.DashboardRepository;
import com.shashank.spendistrybusiness.Repository.InventoryRepository;
import com.shashank.spendistrybusiness.Repository.InvoiceRepository;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

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

    public MutableLiveData<Integer> updateInvoiceNumber(String email,int invoiceNumber){
        return invoiceRepository.updateInvoiceNumber(email,invoiceNumber);
    }


    public LiveData<List<ItemPrices>> getItemPrices(String email) {
        return inventoryRepository.getInventory(email);
    }
    public void setInventory(String email,ArrayList<ItemPrices> itemPricesList) {
        inventoryRepository.setInventory(email,itemPricesList);
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
     public void setInvoiceList(List<ItemPrices> itemPricesList){
        itemPricesArrayList.clear();
        itemPricesArrayList.addAll(itemPricesList);
        data.setValue(itemPricesArrayList);
    }

    public LiveData<List<ItemPrices>> getInvoice() {
        return data;
    }

    public void addInvoice(String email, String businessEmail, BusinessInvoices invoice) {
        invoiceRepository.addInvoice(email, businessEmail, invoice);
    }

    public MutableLiveData<List<Report>> getReportedInvoices(String email){
        return invoiceRepository.getReportedInvoices(email);
    }

    public void deleteReportRequest(LinearLayout linearLayout, String email) {
        invoiceRepository.deleteReportRequest(linearLayout, email);
    }

    public MutableLiveData<List<ItemPrices>> getReportedInvoice(String email, String businessEmail,String invoiceNumber){
        return invoiceRepository.getReportedInvoice(email,businessEmail,invoiceNumber);
    }

    public void updateInvoice(String email,String businessEmail,String invoiceId,String reportId,Invoice invoice){
        invoiceRepository.updateInvoice(email,businessEmail,invoiceId,reportId,invoice);
    }

    public MutableLiveData<ResponseBody> getPDF( String email, String businessEmail, String invoiceNumber){
        return invoiceRepository.getPDF(email,businessEmail,invoiceNumber);
    }

    public MutableLiveData<String> verifyQR(LinearLayout linearLayout,String QR){
        return invoiceRepository.verifyQR(linearLayout,QR);
    }
}