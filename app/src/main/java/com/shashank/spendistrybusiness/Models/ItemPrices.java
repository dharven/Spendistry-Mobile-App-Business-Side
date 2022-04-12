package com.shashank.spendistrybusiness.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

@SuppressWarnings("ALL")
@Entity(tableName = "inventory")
public class ItemPrices {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("_id")
    @NonNull
    private final String id ;

    private String email;

    @ColumnInfo(name = "barcode")
    private final String barcode;

    @ColumnInfo(name = "itemName")
    private final String itemName;

    @ColumnInfo(name = "price")
    private final String price;

    private String total;

    private String quantity;


    public ItemPrices(String id,String email,String barcode, String itemName, String price) {
        this.id = id;
        this.barcode = barcode;
        this.email = email;
        this.itemName = itemName;
        this.price = price;
        this.total = price;
    }

    @Ignore
    public ItemPrices(String barcode, String itemName, int quantity, String price) {
        this.id = new ObjectId().toString();
        this.barcode = barcode;
        this.itemName = itemName;
        this.price = price;
        this.total = String.valueOf(Integer.parseInt(price) * quantity);
        this.quantity = quantity + "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getTotal() {
        return total;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
