package com.shashank.spendistrybusiness.Repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Dao.InventoryDao;
import com.shashank.spendistrybusiness.Database.SpendistryBusinessDB;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.SpendistryAPI.SpendistryAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class InventoryRepository {
    private Application application;
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_API).addConverterFactory(GsonConverterFactory.create()).build();
    SpendistryAPI api = retrofit.create(SpendistryAPI.class);
    private SpendistryBusinessDB businessDB;
    private MutableLiveData<List<ItemPrices>> mutableLiveData = new MutableLiveData<>();

    public InventoryRepository(Application application) {
        this.application = application;
        businessDB = SpendistryBusinessDB.getInstance(application);
    }

    public void insertItemPrices(List<ItemPrices> itemPrices) {
        new InsertAsyncTask(businessDB).execute(itemPrices);
    }

    public void setInventory(String email, ArrayList<ItemPrices> prices){
        if (prices.size() > 0) {
            Call<ItemPricesArrayList> call = api.updateInventory(email, new ItemPricesArrayList(prices));
            call.enqueue(new Callback<ItemPricesArrayList>() {
                @Override
                public void onResponse(Call<ItemPricesArrayList> call, Response<ItemPricesArrayList> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(application, "notWorking: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (prices.get(0).getItemName() != null && prices.get(0).getPrice() != null) {
                        insertItemPrices(prices);
                    }
                }

                @Override
                public void onFailure(Call<ItemPricesArrayList> call, Throwable t) {
                    Toast.makeText(application, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
                    Call<ItemPricesArrayList> call = api.getInventory(email);
                    call.enqueue(new Callback<ItemPricesArrayList>() {
                        @Override
                        public void onResponse(Call<ItemPricesArrayList> call, Response<ItemPricesArrayList> response) {
                            if (response.body() != null) {
                                new AddAllDataAsyncTask(businessDB).execute(response.body().getItemPrices());
                            }
                        }

                        @Override
                        public void onFailure(Call<ItemPricesArrayList> call, Throwable t) {
                            if (t.getMessage().contains("Unable to resolve host")) {
                                Toast.makeText(application, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    public LiveData<List<ItemPrices>> getInventory(String email) {
        return businessDB.inventoryDao().getAllItems();
    }

    public void deleteElement(String email, String itemId) {
        Call<ItemPricesArrayList> call = api.deleteElement(email, itemId);
        call.enqueue(new Callback<ItemPricesArrayList>() {
            @Override
            public void onResponse(Call<ItemPricesArrayList> call, Response<ItemPricesArrayList> response) {
                if (!response.isSuccessful()) {

                    return;
                }
               new DeleteElementAsyncTask(application,businessDB).execute(itemId);
            }

            @Override
            public void onFailure(Call<ItemPricesArrayList> call, Throwable t) {
                Toast.makeText(application, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateElement(String email, String id,ItemPrices itemPrices) {
        Call<ItemPrices> call = api.updateElement(email, id, itemPrices);
        call.enqueue(new Callback<ItemPrices>() {
            @Override
            public void onResponse(Call<ItemPrices> call, Response<ItemPrices> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "notWorking: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                new UpdateElementAsyncTask(application,businessDB).execute(itemPrices);
            }

            @Override
            public void onFailure(Call<ItemPrices> call, Throwable t) {
                Toast.makeText(application, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class UpdateElementAsyncTask extends AsyncTask<ItemPrices, Void, Void> {
        private final InventoryDao inventoryDao;
        private final Application application;

        UpdateElementAsyncTask(Application application, SpendistryBusinessDB businessDB) {
            this.application = application;
            inventoryDao = businessDB.inventoryDao();
        }

        @Override
        protected Void doInBackground(ItemPrices... itemPrices) {
            inventoryDao.updateItem(itemPrices[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(application, "Item updated", Toast.LENGTH_SHORT).show();
        }
    }

    static class DeleteElementAsyncTask extends AsyncTask<String, Void, Void>{
        private final InventoryDao inventoryDao;
        private final Application application;
        DeleteElementAsyncTask(Application application,SpendistryBusinessDB businessDB) {
            inventoryDao = businessDB.inventoryDao();
            this.application = application;
        }

        @Override
        protected Void doInBackground(String... strings) {
            inventoryDao.deleteItem(strings[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(application, "Item deleted", Toast.LENGTH_SHORT).show();
        }
    }

    static class InsertAsyncTask extends AsyncTask<List<ItemPrices>, Void, Void> {
        private final InventoryDao inventoryDao;

        InsertAsyncTask(SpendistryBusinessDB businessDB) {
            inventoryDao = businessDB.inventoryDao();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<ItemPrices>... lists) {
            inventoryDao.addItems(lists[0]);
            return null;
        }
    }

    static class AddAllDataAsyncTask extends AsyncTask<List<ItemPrices>, Void, Void> {
        private final InventoryDao inventoryDao;

        AddAllDataAsyncTask(SpendistryBusinessDB businessDB) {
            inventoryDao = businessDB.inventoryDao();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<ItemPrices>... lists) {
            inventoryDao.deleteAll();
            inventoryDao.addItems(lists[0]);
            return null;
        }
    }

}
