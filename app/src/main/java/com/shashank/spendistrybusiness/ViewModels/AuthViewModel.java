package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Models.Auth;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.Repository.AuthRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

@SuppressWarnings("ALL")
public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void setNewProfilePic(RelativeLayout layout, String email, MultipartBody.Part part){
        authRepository.setNewProfilePic(layout, email,part);
    }

    public void deleteProfilePic(RelativeLayout layout,String id){
        authRepository.deleteProfilePic(layout,id);
    }

    public void setNewPassword(Context context, LinearLayout linearLayout, String email, String newPassword){
        authRepository.setNewPassword(context,linearLayout,email,newPassword);
    }

    public MutableLiveData<String> getAuth(Context context,LinearLayout linearLayout,String email, String password) {
        return authRepository.getAuth(context, linearLayout, email, password);
    }

    public MutableLiveData<Auth> createAccount(LinearLayout linearLayout,String email, String password) {
        return authRepository.createAccount(linearLayout,email, password);
    }

    public MutableLiveData<Vendor> createVendorData(Vendor vendor) {
        return authRepository.createVendorData(vendor);
    }

    public void newOTP(LinearLayout linearLayout,String email){
        authRepository.newOTP(linearLayout,email);
    }

    public MutableLiveData<Auth> deleteAccount(String email) {
        return authRepository.deleteAccount(email);
    }

    public MutableLiveData<ItemPricesArrayList> CreateInventory(String email) {
        return authRepository.CreateInventory(email);
    }

    public void sendOTP(LinearLayout linearLayout,String email) {
         authRepository.sendOTP(linearLayout,email);
    }

    public MutableLiveData<String> verifyOTP(LinearLayout linearLayout,String email, int otp) {
        return authRepository.verifyOTP(linearLayout,email,otp);
    }

    public MutableLiveData<Vendor> updateProfile(RelativeLayout layout,String email, Vendor vendor) {
        return authRepository.updateProfile(layout,email, vendor);
    }
}
