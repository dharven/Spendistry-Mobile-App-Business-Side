package com.shashank.spendistrybusiness.Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.SpendistryAPI.SpendistryAPI;

import java.util.ArrayList;

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

//    public MutableLiveData<ItemPricesArrayList> addItemPrices(String email, ItemPricesArrayList itemPricesLists) {
//        MutableLiveData<ItemPricesArrayList> itemPricesListMutableLiveData = new MutableLiveData<>();
//        Call<ItemPricesArrayList> call = api.addItemPrices(email, itemPricesLists);
//        call.enqueue(new Callback<ItemPricesArrayList>() {
//            @Override
//            public void onResponse(Call<ItemPricesArrayList> call, Response<ItemPricesArrayList> response) {
//                if (!response.isSuccessful()) {
//                    Toast.makeText(application, "notWorking: item creation: " + response.code(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//            }
//
//
//            @Override
//            public void onFailure(Call<ItemPricesArrayList> call, Throwable t) {
//
//            }
//        });
//        return itemPricesListMutableLiveData;
//    }



}

