package com.shashank.spendistrybusiness.Dao;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.shashank.spendistrybusiness.Models.ItemPrices;

import java.util.List;

@Dao
public interface InventoryDao {

    @Query("SELECT * FROM inventory")
    LiveData<List<ItemPrices>> getAllItems();

    @Query("DELETE FROM inventory")
    void deleteAll();

    @Query("DELETE FROM inventory WHERE id== :id")
    void deleteItem(String id);

    @Insert
    void addItems(List<ItemPrices> itemPrices);

    @Update
    void updateItem(ItemPrices itemPrices);

}
