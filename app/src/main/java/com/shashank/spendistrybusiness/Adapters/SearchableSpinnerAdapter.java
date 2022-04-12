package com.shashank.spendistrybusiness.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class SearchableSpinnerAdapter extends ArrayAdapter<ItemPrices> {

        private ArrayList<ItemPrices> items;

        public SearchableSpinnerAdapter(@NonNull Context context, @NonNull ArrayList<ItemPrices> objects) {
            super(context, 0, objects);
            items = objects;
        }

        private static class ViewHolder {
            TextView itemName;
            TextView itemPrice;
        }

        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String itemNameString = getItem(position).getItemName();
            String itemPriceString = getItem(position).getPrice();
            ViewHolder holder;
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.searchable_listview, parent, false);
                holder = new SearchableSpinnerAdapter.ViewHolder();
                holder.itemName = convertView.findViewById(R.id.choose_item);
                holder.itemPrice = convertView.findViewById(R.id.choose_price);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.itemName.setText(itemNameString);
            holder.itemPrice.setText("₹"+itemPriceString);
            return convertView;
        }


    }


