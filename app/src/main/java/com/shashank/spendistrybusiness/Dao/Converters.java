package com.shashank.spendistrybusiness.Dao;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.Vendor;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    //convert ArrayList<Integer> to String
    @TypeConverter
    public static String convertIntegers(ArrayList<Integer> integers) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : integers) {
            sb.append(i);
            sb.append(",");
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    //convert String to ArrayList<Integer>
    @TypeConverter
    public static ArrayList<Integer> convertStringToIntegers(String s) {
        ArrayList<Integer> list = new ArrayList<>();
        if (s != null && !s.equals("")) {
            String[] parts = s.split(",");
            for (String part : parts) {
                list.add(Integer.parseInt(part));
            }
        }
        return list;
    }

    //convert Vendor object to String
    @TypeConverter
    public static String convertVendor(Vendor vendor) {
        Gson gson = new Gson();
        return gson.toJson(vendor);
    }

    //convert String to Vendor object
    @TypeConverter
    public static Vendor convertStringToVendor(String s) {
        return new Gson().fromJson(s, Vendor.class);
    }

    //convert ArrayList<Invoices> object to String
    @TypeConverter
    public static String convertInvoices(ArrayList<Invoice> invoices) {
        Gson gson = new Gson();
        return gson.toJson(invoices);
    }

    //convert String to ArrayList<Invoice> object
    @TypeConverter
    public static ArrayList<Invoice> convertStringToInvoices(String s) {
        Type typeMyType = new TypeToken<ArrayList<Invoice>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(s, typeMyType);
    }


}
