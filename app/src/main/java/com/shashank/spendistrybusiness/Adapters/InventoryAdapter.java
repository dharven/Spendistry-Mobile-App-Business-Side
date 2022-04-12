package com.shashank.spendistrybusiness.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;

import java.util.List;


@SuppressWarnings("ALL")
public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyViewHolder> {

    private final List<ItemPrices> itemPricesList;
    private Context context;
    private final Activity activity;

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


    @SuppressWarnings("deprecation")
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull InventoryAdapter.MyViewHolder holder, int position) {
            holder.itemPrice.setText("₹"+itemPricesList.get(position).getPrice());
            holder.itemName.setText(itemPricesList.get(position).getItemName());

        holder.itemView.setOnLongClickListener(view -> {
            Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.manual_invoice), "Swipe Left/Right to take action", Snackbar.LENGTH_SHORT);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(activity.getResources().getColor(R.color.mainBlue));
            snackbar.show();
            return true;
        });

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
