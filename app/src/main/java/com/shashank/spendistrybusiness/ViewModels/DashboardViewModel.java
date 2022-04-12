package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.Repository.DashboardRepository;

public class DashboardViewModel extends AndroidViewModel {
    DashboardRepository dashboardRepository;
    LiveData<Dashboard> dashboard;

    public DashboardViewModel(@NonNull Application application,LinearLayout linearLayout, String email) {
        super(application);
        dashboardRepository = new DashboardRepository(application);
        dashboard = dashboardRepository.getDashboardData(linearLayout,email);
    }

    public LiveData<Dashboard> getDashboardData(){
        return dashboard;
    }

    public void deleteInventory(){
        dashboardRepository.deleteInventory();
    }

    public void deleteDashboard(){
        dashboardRepository.deleteDashboard();
    }
}
