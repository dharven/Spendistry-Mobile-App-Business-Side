package com.shashank.spendistrybusiness.SpendistryAPI;

import com.shashank.spendistrybusiness.Models.Auth;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessInvoices;
import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.Models.Vendor;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SpendistryAPI {


    //GET
    @GET("itemsPrices/{email}")
    Call<ItemPricesArrayList> getInventory(@Path("email") String email);

    @GET("mvd/{email}")
    Call<Dashboard> getDashboardData(@Path("email") String email);

    @GET("invoice/vendor/{email}")
    Call<ArrayList<BusinessInvoices>> getBusinessInvoices(@Path("email") String email);


    //POST
    @POST("authBusiness/vendorLogin")
    Call<String> getAuth(@Body Auth auth);

    @POST("authBusiness")
    Call<Auth> createAccount(@Body Auth auth);

    @POST("vendor")
    Call<Vendor> createUserData(@Body Vendor vendor);

    @POST("itemsPrices")
    Call<ItemPricesArrayList> CreateInventory(@Body ItemPricesArrayList itemPricesList);

    @POST("invoice/addEle/{email}/{businessEmail}")
    Call<BusinessInvoices> addInvoice(@Path("email") String email, @Path("businessEmail") String businessEmail , @Body BusinessInvoices invoice);


    //DELETE
    @DELETE("authBusiness/{email}")
    Call<Auth> deleteAccount(@Path("email") String email);

    @DELETE("itemsPrices/deleteItems/{email}/{itemId}")
    Call<ItemPricesArrayList> deleteElement(@Path("email") String email, @Path("itemId") String itemId);


    //PATCH
    @PATCH("itemsPrices/addItems/{email}")
    Call<ItemPricesArrayList> updateInventory(@Path("email") String email, @Body ItemPricesArrayList itemPricesArrayList);

    @PATCH("itemsPrices/patchEle/{email}/{itemId}")
    Call<ItemPrices> updateElement(@Path("email") String email, @Path("itemId") String itemId, @Body ItemPrices itemPrices);

    @PATCH("authBusiness/{email}")
    Call<Auth> setNewPassword(@Path("email") String email, @Body Auth auth);



}
