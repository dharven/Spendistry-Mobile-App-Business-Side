package com.shashank.spendistrybusiness.Repository;

import android.app.Application;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Dao.BusinessInvoicesDao;
import com.shashank.spendistrybusiness.Dao.dashboardDao;
import com.shashank.spendistrybusiness.Database.SpendistryBusinessDB;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessInvoices;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.SpendistryAPI.SpendistryAPI;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoiceRepository {
    private Application application;
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).build();
    SpendistryAPI api = retrofit.create(SpendistryAPI.class);
    private SpendistryBusinessDB businessDB;
    private MutableLiveData<BusinessInvoices> mutableLiveData;



    public InvoiceRepository(Application application) {
        this.application = application;
        businessDB = SpendistryBusinessDB.getInstance(application);
    }





    public void addInvoice(String email, String businessEmail, BusinessInvoices invoice) {

        Call<BusinessInvoices> call = api.addInvoice(email,businessEmail,invoice);
        call.enqueue(new Callback<BusinessInvoices>() {
            @Override
            public void onResponse(Call<BusinessInvoices> call, Response<BusinessInvoices> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, ""+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<BusinessInvoices> call, Throwable t) {

            }
        });
    }

    public LiveData<BusinessInvoices> getBusinessInvoices(String email){
        mutableLiveData = new MutableLiveData<>();
        Call<ArrayList<BusinessInvoices>> call = api.getBusinessInvoices(email);
        call.enqueue(new Callback<ArrayList<BusinessInvoices>>() {

            @Override
            public void onResponse(Call<ArrayList<BusinessInvoices>> call, Response<ArrayList<BusinessInvoices>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, ""+response.code(), Toast.LENGTH_SHORT).show();
                    mutableLiveData.setValue(null);
                    return;
                }
                if (response.body().get(0) != null) {
                    mutableLiveData.setValue(response.body().get(0));
                    new addInvoicesToDB(application,businessDB).execute(response.body().get(0));
                } else {
                    ArrayList<Invoice> businessInvoices = new ArrayList<>();
                    mutableLiveData.setValue(new BusinessInvoices(email,businessInvoices));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BusinessInvoices>> call, Throwable t) {
                if (Objects.requireNonNull(t.getMessage()).startsWith("Unable to resolve host")) {
                    businessDB.businessInvoicesDao().getBusinessInvoices(email).observeForever(new Observer<BusinessInvoices>() {
                        @Override
                        public void onChanged(BusinessInvoices businessInvoices) {
                            mutableLiveData.setValue(businessInvoices);
                        }
                    });
                }
            }
        });
        return mutableLiveData;
    }
    static class addInvoicesToDB extends AsyncTask<BusinessInvoices, Void, Void> {
        private BusinessInvoicesDao businessInvoicesDao;
        private Application application;

        addInvoicesToDB(Application application, SpendistryBusinessDB businessDB) {
            this.application = application;
            businessInvoicesDao = businessDB.businessInvoicesDao();
        }

        @Override
        protected Void doInBackground(BusinessInvoices... invoices) {
            businessInvoicesDao.deleteAll();
            businessInvoicesDao.addBusinessInvoices(invoices[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(application, "dashboard added", Toast.LENGTH_SHORT).show();
        }
    }


}

