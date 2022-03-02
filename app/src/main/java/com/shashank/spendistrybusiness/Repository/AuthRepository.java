package com.shashank.spendistrybusiness.Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Models.Auth;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.SpendistryAPI.SpendistryAPI;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository {
    private Application application;
    private Gson gson = new GsonBuilder().setLenient().create();
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create(gson)).build();
    SpendistryAPI api = retrofit.create(SpendistryAPI.class);

    public AuthRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<String> getAuth(String email, String password) {
        MutableLiveData<String> auth = new MutableLiveData<>();
        Auth auth1 = new Auth(email, password);
        Call<String> call = api.getAuth(auth1);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "notWorking: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.setValue(response.headers().get("auth-token-vendor"));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(application, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return auth;
    }

    public MutableLiveData<Auth> createAccount(String email, String password) {
        MutableLiveData<Auth> auth = new MutableLiveData<>();
        Auth auth1 = new Auth(email, password);
        Call<Auth> call = api.createAccount(auth1);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "account not created " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText(application, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return auth;
    }

    public MutableLiveData<Vendor> createVendorData(Vendor vendor) {
        MutableLiveData<Vendor> vendor1 = new MutableLiveData<>();
        Call<Vendor> call = api.createUserData(vendor);
        call.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "error in creation of vendor data: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                vendor1.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                Toast.makeText(application, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return vendor1;
    }

    public MutableLiveData<Auth> deleteAccount(String email){
        MutableLiveData<Auth> auth = new MutableLiveData<>();
        Call<Auth> call = api.deleteAccount(email);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "notWorking: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                auth.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {

            }
        });
        return auth;
    }

    public MutableLiveData<ItemPricesArrayList> CreateInventory (String email) {
        MutableLiveData<ItemPricesArrayList> itemPricesListMutableLiveData = new MutableLiveData<>();
        ArrayList<ItemPrices> itemPricesArrayList = new ArrayList<>();
        Call<ItemPricesArrayList> call = api.CreateInventory(new ItemPricesArrayList(email, itemPricesArrayList));
        call.enqueue(new Callback<ItemPricesArrayList>() {

            @Override
            public void onResponse(Call<ItemPricesArrayList> call, Response<ItemPricesArrayList> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "error in creation of inventory" + response.body() + " \n"+ response.message()+ " \n"+ response.code() + " \n"+ response.errorBody(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<ItemPricesArrayList> call, Throwable t) {

            }
        });
        return itemPricesListMutableLiveData;
    }
}
