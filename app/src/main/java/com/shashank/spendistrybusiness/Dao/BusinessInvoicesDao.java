package com.shashank.spendistrybusiness.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessInvoices;
import com.shashank.spendistrybusiness.Models.Dashboard;

@Dao
public interface BusinessInvoicesDao {
    @Query("SELECT * FROM business_invoices WHERE email== :id")
    LiveData<BusinessInvoices> getBusinessInvoices(String id);

    @Insert
    void addBusinessInvoices(BusinessInvoices businessInvoices);

    @Query("DELETE FROM business_invoices")
    void deleteAll();
}
