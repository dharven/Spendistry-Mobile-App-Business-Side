package com.shashank.spendistrybusiness.Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Constants.Constants;
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

    public LiveData<List<ItemPrices>> setInvoice(ItemPrices itemPrices) {
        MutableLiveData<List<ItemPrices>> data = new MutableLiveData<>();
        ArrayList<ItemPrices> itemPricesArrayList = new ArrayList<>();
        itemPricesArrayList.add(itemPrices);
        data.setValue(itemPricesArrayList);
        return data;
    }



}

