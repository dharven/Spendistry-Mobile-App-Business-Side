package com.shashank.spendistrybusiness.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.shashank.spendistrybusiness.Dao.InventoryDao;
import com.shashank.spendistrybusiness.Models.ItemPrices;

@Database(entities = {ItemPrices.class}, version = 8, exportSchema = false)
public abstract class SpendistryBusinessDB extends RoomDatabase {
    private static final String DATABASE_NAME = "SpendistryBusinessDB";
    public abstract InventoryDao inventoryDao();
    private static volatile SpendistryBusinessDB INSTANCE;

    public static SpendistryBusinessDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SpendistryBusinessDB.class) {
                if (INSTANCE == null) {
                    INSTANCE= Room.databaseBuilder(context, SpendistryBusinessDB.class, DATABASE_NAME).addCallback(callback).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
    static Callback callback = new Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new AddItemsAsyncTask(INSTANCE);
        }
    };

    static class AddItemsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final InventoryDao inventoryDao;

        AddItemsAsyncTask(SpendistryBusinessDB businessDB) {
            inventoryDao = businessDB.inventoryDao();
            Log.i("Test", "PopulateAsyncTask: test");

        }

        @Override
        protected Void doInBackground(Void... voids) {
            inventoryDao.deleteAll();
            Log.w("main123", "doInBackground: bg working");
            return null;
        }
    }

}
