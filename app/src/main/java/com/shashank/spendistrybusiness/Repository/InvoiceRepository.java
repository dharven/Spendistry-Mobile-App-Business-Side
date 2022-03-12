package com.shashank.spendistrybusiness.Repository;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Adapters.InventoryAdapter;
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessArray;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.CreateInvoice.UserInvoices;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.SpendistryAPI.SpendistryAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoiceRepository {
    private Application application;
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).build();
    SpendistryAPI api = retrofit.create(SpendistryAPI.class);



    public InvoiceRepository(Application application) {
        this.application = application;
    }





    public void addInvoice(String email, String businessEmail, BusinessArray invoice) {

        Call<BusinessArray> call = api.addInvoice(email,businessEmail,invoice);
        call.enqueue(new Callback<BusinessArray>() {
            @Override
            public void onResponse(Call<BusinessArray> call, Response<BusinessArray> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, ""+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<BusinessArray> call, Throwable t) {

            }
        });
    }



}

