package com.shashank.spendistrybusiness.Constants;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Models.ItemPrices;

import java.util.ArrayList;
import java.util.List;

public class Global extends Application {
    public static ArrayList<ItemPrices> itemPricesArrayList = new ArrayList<>();
    public static MutableLiveData<List<ItemPrices>> data = new MutableLiveData<>();
}
