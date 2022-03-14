package com.shashank.spendistrybusiness.Dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.Vendor;

import java.util.List;

import com.shashank.spendistrybusiness.Models.Dashboard;

@Dao
public interface dashboardDao {
    @Query("SELECT * FROM dashboard WHERE email== :id")
    LiveData<Dashboard> getDashboardData(String id);

    @Insert
    void addDashboardData(Dashboard dashboard);

    @Query("DELETE FROM dashboard")
    void deleteAll();
}
