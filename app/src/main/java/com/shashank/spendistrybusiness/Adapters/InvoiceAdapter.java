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

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {

    private final List<ItemPrices> itemPricesList;
    private Context context;
    private final Activity activity;

    public InvoiceAdapter(List<ItemPrices> itemPricesList, Context context, Activity activity) {
        this.itemPricesList = itemPricesList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public InvoiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.invoice_recyclerview, parent, false);
        return new MyViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull InvoiceAdapter.MyViewHolder holder, int position) {
        holder.itemPrice.setText("₹"+itemPricesList.get(position).getPrice());
        holder.itemName.setText(itemPricesList.get(position).getItemName());
        if (itemPricesList.get(position).getQuantity()!=null){
            holder.itemQuantity.setText(itemPricesList.get(position).getQuantity());
            holder.itemTotal.setText("₹"+ Integer.parseInt(itemPricesList.get(position).getPrice()) * Integer.parseInt(itemPricesList.get(position).getQuantity()));

        } else {
            holder.itemQuantity.setText("1");
            holder.itemTotal.setText("₹"+itemPricesList.get(position).getPrice());
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.manual_invoice), "Swipe left to take action", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(activity.getResources().getColor(R.color.mainBlue));
                snackbar.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemPricesList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemPrice;
        TextView itemQuantity;
        TextView itemTotal;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameInvoice);
            itemPrice = itemView.findViewById(R.id.PriceInvoice);
            itemQuantity = itemView.findViewById(R.id.QuantityInvoice);
            itemTotal = itemView.findViewById(R.id.TotalInvoice);
        }
    }


    public String getId(int position){
        return itemPricesList.get(position).getId();
    }

    public String getBarcode(int position){
        return itemPricesList.get(position).getBarcode();
    }

    public String getPrice(int position){
        return itemPricesList.get(position).getPrice();
    }

    public String getTotal(int position){
        return itemPricesList.get(position).getTotal();
    }

    public ArrayList<ItemPrices> getItemPricesList() {
        return (ArrayList<ItemPrices>) itemPricesList;
    }

    public void updateItem(int position, ItemPrices itemPrices){
        itemPricesList.add(position, itemPrices);
        notifyItemInserted(position);
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
