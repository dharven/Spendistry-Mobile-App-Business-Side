package com.shashank.spendistrybusiness.Models;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity
public class ItemPricesArrayList {

    @SerializedName("_id")
    String email;

    @SerializedName("ItemsPrices")
    ArrayList<ItemPrices> itemPrices;

    public ItemPricesArrayList(String email) {
        this.email = email;
    }

    public ItemPricesArrayList(ArrayList<ItemPrices> itemPrices) {
        this.itemPrices = itemPrices;
    }

    public ItemPricesArrayList(String email, ArrayList<ItemPrices> itemPrices) {
        this.email = email;
        this.itemPrices = itemPrices;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<ItemPrices> getItemPrices() {
        return itemPrices;
    }
}
