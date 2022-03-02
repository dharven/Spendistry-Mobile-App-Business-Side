package com.shashank.spendistrybusiness.Constants;

import android.app.Application;

import com.shashank.spendistrybusiness.Models.ItemPrices;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariables extends Application {

    public List<ItemPrices> itemPricesArrayList = new ArrayList<>();

    public List<ItemPrices> getItemPricesArrayList() {
        return itemPricesArrayList;
    }

    public void setItemPricesArrayList(List<ItemPrices> itemPricesArrayList) {
        this.itemPricesArrayList = itemPricesArrayList;
    }
}
