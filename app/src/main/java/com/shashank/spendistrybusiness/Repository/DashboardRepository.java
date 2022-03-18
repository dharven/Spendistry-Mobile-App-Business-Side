package com.shashank.spendistrybusiness.Repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Dao.InventoryDao;
import com.shashank.spendistrybusiness.Dao.dashboardDao;
import com.shashank.spendistrybusiness.Database.SpendistryBusinessDB;
import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.SpendistryAPI.SpendistryAPI;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardRepository {
    private Application application;
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).build();
    SpendistryAPI api = retrofit.create(SpendistryAPI.class);
    MutableLiveData<Dashboard> dashboardData = new MutableLiveData<>();
    private SpendistryBusinessDB businessDB;
    private SharedPreferences sharedPreferences ;

    public DashboardRepository(Application application) {
        this.application = application;
        businessDB = SpendistryBusinessDB.getInstance(application);
        sharedPreferences = application.getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
    }

    public LiveData<Dashboard> getDashboardData(LinearLayout linearLayout,String email){
        Call<Dashboard> call = api.getDashboardData(email);
            call.enqueue(new Callback<Dashboard>() {
                @Override
                public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
                    if (!response.isSuccessful()) {
                        dashboardData.setValue(null);
                        Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new addDashboardData(application, businessDB).execute(response.body());
                    dashboardData.setValue(response.body());
                }

                @Override
                public void onFailure(Call<Dashboard> call, Throwable t){
                    //only execute code if internet is not available
                    if (Objects.requireNonNull(t.getMessage()).startsWith("Unable to resolve host")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar snackbar = Snackbar.make(linearLayout, "Internet is not available", Snackbar.LENGTH_SHORT);
                                snackbar.setTextColor(Color.WHITE);
                                snackbar.setBackgroundTint(application.getColor(R.color.red));
                                snackbar.show();
                            }
                        },500);

                        businessDB.dashboardDao().getDashboardData(email).observeForever(dashboard -> {
                            dashboardData.setValue(dashboard);
                        });
                    }
                }
            });
        return dashboardData;
    }

    public LiveData<Dashboard> getDashBoardFromDB(String email) {
        return businessDB.dashboardDao().getDashboardData(email);
    }

    static class addDashboardData extends AsyncTask<Dashboard, Void, Void> {
        private final dashboardDao dashboardDao;
        private final Application application;

        addDashboardData(Application application, SpendistryBusinessDB businessDB) {
            this.application = application;
            dashboardDao = businessDB.dashboardDao();
        }

        @Override
        protected Void doInBackground(Dashboard... dashboard) {
            dashboardDao.deleteAll();
            dashboardDao.addDashboardData(dashboard[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(application, "dashboard added", Toast.LENGTH_SHORT).show();
        }
    }
}


