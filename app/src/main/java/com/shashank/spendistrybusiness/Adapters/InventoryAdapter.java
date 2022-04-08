package com.shashank.spendistrybusiness.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyViewHolder> {

    private List<ItemPrices> itemPricesList;
    private Context context;
    private Activity activity;

    public InventoryAdapter(List<ItemPrices> itemPricesList, Context context, Activity activity) {
        this.itemPricesList = itemPricesList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public InventoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.items_recyclerview, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull InventoryAdapter.MyViewHolder holder, int position) {
            holder.itemPrice.setText("₹"+itemPricesList.get(position).getPrice());
            holder.itemName.setText(itemPricesList.get(position).getItemName());
    }

    @Override
    public int getItemCount() {
        return itemPricesList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemPrice;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameTv);
            itemPrice = itemView.findViewById(R.id.PriceTv);
        }
    }


    public String getId(int position){
        return itemPricesList.get(position).getId();
    }

    public String getBarcode(int position){
        return itemPricesList.get(position).getBarcode();
    }

    public String getItemName(int position){
        return itemPricesList.get(position).getItemName();
    }

    public String getPrice(int position){
        return itemPricesList.get(position).getPrice();
    }

    public ArrayList<ItemPrices> getItemPricesList() {
        return (ArrayList<ItemPrices>) itemPricesList;
    }

    public ItemPrices updateItem(int position,String id,String barcode,String email ,String itemName, String price){
        ItemPrices itemPrices = new ItemPrices(id,barcode, email ,itemName, price);
        itemPricesList.add(position, itemPrices);
        notifyItemInserted(position);
        return itemPrices;
    }
    public ItemPrices recentRemove(int position){
        ItemPrices itemPrices = itemPricesList.get(position);
        itemPricesList.remove(position);
        notifyItemRemoved(position);
        return itemPrices;
    }

    public void undoRecent(int position, ItemPrices itemPrices){
        itemPricesList.add(position, itemPrices);
        notifyItemInserted(position);
    }
}
