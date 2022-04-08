package com.shashank.spendistrybusiness.Repository;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Constants.Global;
import com.shashank.spendistrybusiness.Dao.BusinessInvoicesDao;
import com.shashank.spendistrybusiness.Dao.dashboardDao;
import com.shashank.spendistrybusiness.Database.SpendistryBusinessDB;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessInvoices;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.CreateInvoice.InvoicePatch;
import com.shashank.spendistrybusiness.Models.CreateInvoice.QR;
import com.shashank.spendistrybusiness.Models.CreateInvoice.UserInvoices;
import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.Report;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.SpendistryAPI.SpendistryAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
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
        Call<BusinessInvoices> call = api.addInvoice(email, businessEmail, invoice);
        call.enqueue(new Callback<BusinessInvoices>() {
            @Override
            public void onResponse(Call<BusinessInvoices> call, Response<BusinessInvoices> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<BusinessInvoices> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Report>> getReportedInvoices(String email) {
        MutableLiveData<List<Report>> mutableLiveData = new MutableLiveData<>();
        Call<List<Report>> call = api.getReportedInvoices(email);
        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<BusinessInvoices> getBusinessInvoices(String email) {
        mutableLiveData = new MutableLiveData<>();
        Call<ArrayList<BusinessInvoices>> call = api.getBusinessInvoices(email);
        call.enqueue(new Callback<ArrayList<BusinessInvoices>>() {

            @Override
            public void onResponse(Call<ArrayList<BusinessInvoices>> call, Response<ArrayList<BusinessInvoices>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    mutableLiveData.setValue(null);
                    return;
                }
                if (response.body().size() == 0) {
                    mutableLiveData.setValue(new BusinessInvoices("", new ArrayList<Invoice>()));
                } else {
                    if (response.body().get(0) != null) {
                        mutableLiveData.setValue(response.body().get(0));
                        new addInvoicesToDB(application, businessDB).execute(response.body().get(0));
                    } else {
                        ArrayList<Invoice> businessInvoices = new ArrayList<>();
                        mutableLiveData.setValue(new BusinessInvoices(email, businessInvoices));
                    }
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


    public void deleteReportRequest(LinearLayout linearLayout, String email) {
        Call<Report> call = api.deleteReportRequest(email);
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {

            }
        });
    }



    public void updateInvoice(String email, String businessEmail, String invoiceId,String reportId, Invoice invoice) {
        InvoicePatch invoice1 = new InvoicePatch(invoice);
        Call<InvoicePatch> call = api.updateInvoice(email, businessEmail, invoiceId, reportId, invoice1);
        call.enqueue(new Callback<InvoicePatch>() {
            @Override
            public void onResponse(Call<InvoicePatch> call, Response<InvoicePatch> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;

                }
//                deleteReportRequest(reportId);
                Global.itemPricesArrayList.clear();
            }

            @Override
            public void onFailure(Call<InvoicePatch> call, Throwable t) {
                Toast.makeText(application, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//showing items in reassigned invoice
    public MutableLiveData<List<ItemPrices>> getReportedInvoice(String email, String businessEmail, String invoiceId) {
        MutableLiveData<List<ItemPrices>> mutableLiveData = new MutableLiveData<>();
        Call<Invoice> call = api.getReportedInvoice(email, businessEmail, invoiceId);
        call.enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body() != null) {
                    if (response.body().getTotalItems() != null) {
                        mutableLiveData.setValue(response.body().getTotalItems());
                    }
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<Integer> updateInvoiceNumber(String email, int invoiceNumber) {
        MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
        Call<Vendor> call = api.updateProfile(email, new Vendor(invoiceNumber));
        call.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body() != null) {
                    mutableLiveData.setValue(response.body().getCurrentInvoiceNumber());
//                    new updateCurrentInvoiceNumber(application, businessDB).execute(new Dashboard(new Vendor(response.body().getCurrentInvoiceNumber())));
                } else {
                    mutableLiveData.setValue(0);
                }
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {

            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<ResponseBody> getPDF(String email, String businessEmail, String invoiceId) {
        //download pdf file with retrofit 2
        MutableLiveData<ResponseBody> mutableLiveData = new MutableLiveData<>();
        Call<ResponseBody> call = api.getPDF(email, businessEmail, invoiceId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
              //set data
                if (response.body() != null) {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<String> verifyQR(LinearLayout linearLayout,String qr){
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        Call<String> call = api.verifyQR(new QR(qr));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 500) {
                        Snackbar snackbar = Snackbar.make(linearLayout, "Something went wrong!!", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(application.getResources().getColor(R.color.red));
                        snackbar.show();
                    }
                    return;
                }
                if (response.body() != null) {
                    if (response.body().equals("404")) {
                        Snackbar snackbar = Snackbar.make(linearLayout, "Wrong QR!!", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(application.getResources().getColor(R.color.red));
                        snackbar.show();
                    } else {
                        mutableLiveData.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    static class updateCurrentInvoiceNumber extends AsyncTask<Dashboard, Void, Void> {
        private Application application;
        private dashboardDao dashboardDao;

        updateCurrentInvoiceNumber(Application application, SpendistryBusinessDB businessDB) {
            this.application = application;
            this.dashboardDao = businessDB.dashboardDao();
        }

        @Override
        protected Void doInBackground(Dashboard... dashboards) {
            Dashboard dashboard = dashboards[0];
            dashboardDao.updateCurrentInvoiceNumber(dashboard);
            return null;
        }


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

