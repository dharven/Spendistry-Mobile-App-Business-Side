package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.Repository.InventoryRepository;

import java.util.ArrayList;
import java.util.List;

public class InventoryViewModel extends AndroidViewModel {

    private final InventoryRepository inventoryRepository;
    private final LiveData<List<ItemPrices>> itemPricesList;
    public InventoryViewModel(@NonNull Application application,String email) {
        super(application);
        inventoryRepository = new InventoryRepository(application);
        itemPricesList = inventoryRepository.getInventory(email);
    }

    public void setInventory(String email, ArrayList<ItemPrices> itemPrices) {
       inventoryRepository.setInventory(email,itemPrices);
    }

    public LiveData<List<ItemPrices>> getInventory(){
        return itemPricesList;
    }

    public void deleteElement(String email, String itemId){
        inventoryRepository.deleteElement(email,itemId);
    }

    public void updateElement(String email, String itemId, ItemPrices itemPrices){
        inventoryRepository.updateElement(email,itemId,itemPrices);
    }

}



