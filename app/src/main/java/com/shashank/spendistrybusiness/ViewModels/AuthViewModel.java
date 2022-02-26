package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Models.Auth;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.Repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public MutableLiveData<String> getAuth(String email, String password) {
        return authRepository.getAuth(email, password);
    }

    public MutableLiveData<Auth> createAccount(String email, String password) {
        return authRepository.createAccount(email, password);
    }

    public MutableLiveData<Vendor> createVendorData(Vendor vendor) {
        return authRepository.createVendorData(vendor);
    }

    public MutableLiveData<Auth> deleteAccount(String email) {
        return authRepository.deleteAccount(email);
    }

    public MutableLiveData<ItemPricesArrayList> CreateInventory(String email) {
        return authRepository.CreateInventory(email);
    }
}
