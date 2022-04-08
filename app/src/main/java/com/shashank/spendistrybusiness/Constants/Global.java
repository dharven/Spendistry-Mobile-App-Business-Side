package com.shashank.spendistrybusiness.Constants;

import android.app.Application;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;

import java.util.ArrayList;
import java.util.List;

public class Global extends Application {

    public static ArrayList<ItemPrices> itemPricesArrayList = new ArrayList<>();
    public static MutableLiveData<List<ItemPrices>> data = new MutableLiveData<>();

}
