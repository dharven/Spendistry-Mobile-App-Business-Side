package com.shashank.spendistrybusiness.Constants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Models.ItemPrices;

import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String URL_API ="https://cdbd-18-212-22-122.ngrok.io/";

    public static MutableLiveData<List<ItemPrices>> itemPricesArrayList = new MutableLiveData<>();
}
