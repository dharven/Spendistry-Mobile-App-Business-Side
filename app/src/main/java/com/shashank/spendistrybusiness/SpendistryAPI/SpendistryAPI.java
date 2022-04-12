package com.shashank.spendistrybusiness.SpendistryAPI;

import com.shashank.spendistrybusiness.Models.Auth;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessInvoices;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.CreateInvoice.InvoicePatch;
import com.shashank.spendistrybusiness.Models.CreateInvoice.OTP;
import com.shashank.spendistrybusiness.Models.CreateInvoice.QR;
import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.ItemPricesArrayList;
import com.shashank.spendistrybusiness.Models.Report;
import com.shashank.spendistrybusiness.Models.User;
import com.shashank.spendistrybusiness.Models.Vendor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface SpendistryAPI {


    //GET
    @GET("itemsPrices/{email}")
    Call<ItemPricesArrayList> getInventory(@Path("email") String email);

    @GET("mvd/{email}")
    Call<Dashboard> getDashboardData(@Path("email") String email);

    @GET("invoice/vendor/{email}")
    Call<ArrayList<BusinessInvoices>> getBusinessInvoices(@Path("email") String email);

    @GET("report/reportTo/{email}")
    Call<List<Report>> getReportedInvoices(@Path("email") String email);

    @GET("invoice/findEle/{email}/{businessEmail}/{invoiceId}")
    Call<Invoice> getReportedInvoice(@Path("email") String email, @Path("businessEmail") String businessEmail, @Path("invoiceId") String invoiceId);

    @GET("pdf/{email}/{businessEmail}/{invoiceId}")
    Call<ResponseBody> getPDF(@Path("email") String email, @Path("businessEmail") String businessEmail, @Path("invoiceId") String invoiceId);

    @GET("return/vendorMail/{email}")
    Call<ArrayList<Invoice>> getReturnedInvoices(@Path("email") String email);

    @GET("user/{email}")
    Call<User> getUser(@Path("email") String email);

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

    @POST("otp/forgotPassword")
    Call<String> sendOTP(@Body OTP otp);

    @POST("otp/createAccount")
    Call<String> newOTP(@Body OTP otp);

    @POST("otp/verifyOTP")
    Call<String> verifyOTP(@Body OTP otp);

    @Multipart
    @POST("vendor/uploadImage/{email}")
    Call<okhttp3.ResponseBody> setNewProfilePic(@Path("email") String email, @Part MultipartBody.Part image  );

    @POST("invoice/decrypt")
    Call<String> verifyQR(@Body QR qr);

    //DELETE
    @DELETE("authBusiness/{email}")
    Call<Auth> deleteAccount(@Path("email") String email);

    @DELETE("itemsPrices/deleteItems/{email}/{itemId}")
    Call<ItemPricesArrayList> deleteElement(@Path("email") String email, @Path("itemId") String itemId);

    @DELETE("report/{id}")
    Call<Report> deleteReportRequest(@Path("id") String id);

    @DELETE("vendor/deleteImage/{id}")
    Call<String> deleteProfilePic(@Path("id") String id);


    //PATCH
    @PATCH("itemsPrices/addItems/{email}")
    Call<ItemPricesArrayList> updateInventory(@Path("email") String email, @Body ItemPricesArrayList itemPricesArrayList);

    @PATCH("itemsPrices/patchEle/{email}/{itemId}")
    Call<ItemPrices> updateElement(@Path("email") String email, @Path("itemId") String itemId, @Body ItemPrices itemPrices);

    @PATCH("authBusiness/{email}")
    Call<Auth> setNewPassword(@Path("email") String email, @Body Auth auth);

    @PATCH("vendor/{email}")
    Call<Vendor> updateProfile(@Path("email") String email, @Body Vendor vendor);

    @PATCH("invoice/patchEle/{userEmail}/{businessEmail}/{invoiceId}/{reportId}")
    Call<InvoicePatch> updateInvoice(@Path("userEmail") String userEmail, @Path("businessEmail") String businessEmail, @Path("invoiceId") String invoiceId,@Path("reportId") String reportId, @Body InvoicePatch invoice);

}
