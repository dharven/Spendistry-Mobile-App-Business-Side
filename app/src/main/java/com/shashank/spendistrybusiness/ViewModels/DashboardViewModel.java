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

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashboardRepository = new DashboardRepository(application);
    }

    public LiveData<Dashboard> getDashboardData(LinearLayout linearLayout, String email){
        return dashboardRepository.getDashboardData(linearLayout,email);
    }
}
