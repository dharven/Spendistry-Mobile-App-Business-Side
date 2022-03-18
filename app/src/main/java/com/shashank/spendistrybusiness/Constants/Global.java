package com.shashank.spendistrybusiness.Constants;

import android.app.Application;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;

import java.util.ArrayList;
import java.util.List;

public class Global extends Application {

    public static ArrayList<ItemPrices> itemPricesArrayList = new ArrayList<>();
    public static MutableLiveData<List<ItemPrices>> data = new MutableLiveData<>();
    public static String sendOTP(Context context, LinearLayout linearLayout, String email){
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            otp.append((int) (Math.random() * 10));
        }
        BackgroundMail.newBuilder(context)
                .withUsername("practicalcns@gmail.com")
                .withPassword("Test_123_practical")
                .withMailTo(email)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("OTP for Spendistry Business")
                .withBody("Your OTP for Spendistry Business is " + otp.toString())
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {

                    @Override
                    public void onSuccess() {
                        //do some magic
                        Snackbar snackbar = Snackbar.make(linearLayout, R.string.email_sent, Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(context,R.color.mainBlue));
                        snackbar.setTextColor(ContextCompat.getColor(context,R.color.material_white));
                        snackbar.show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                        Snackbar snackbar = Snackbar.make(linearLayout, R.string.some_thing_went_wrong, Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(context,R.color.mainBlue));
                        snackbar.setTextColor(ContextCompat.getColor(context,R.color.material_white));
                        snackbar.show();
                    }
                })
                .send();
        return otp.toString();
    }
}
