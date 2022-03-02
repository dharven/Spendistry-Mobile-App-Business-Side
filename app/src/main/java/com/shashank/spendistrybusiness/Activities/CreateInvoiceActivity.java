package com.shashank.spendistrybusiness.Activities;

import static android.view.View.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shashank.spendistrybusiness.Adapters.InventoryAdapter;
import com.shashank.spendistrybusiness.Adapters.SearchableSpinnerAdapter;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreateInvoiceActivity extends AppCompatActivity {

    private TextView searchItem;
    private Dialog dialog;
    private SearchableSpinnerAdapter adapter;
    private RecyclerView invoiceList;
    private List<ItemPrices> itemPricesList;
    private List<ItemPrices> itemPricesListFilter;
    private List<ItemPrices> itemPricesListTemp;
    private ListView listView;

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        searchItem = findViewById(R.id.dropDown);
        invoiceList = findViewById(R.id.invoiceList);
        Intent intent = getIntent();
        itemPricesList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        InvoiceViewModel invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
        invoiceViewModel.getItemPrices(intent.getStringExtra("SCAN_RESULT")).observe(this, new Observer<List<ItemPrices>>() {
            @Override
            public void onChanged(List<ItemPrices> itemPrices) {
                adapter=new SearchableSpinnerAdapter(CreateInvoiceActivity.this,(ArrayList<ItemPrices>) itemPrices);
                itemPricesListFilter = itemPrices;
                itemPricesListTemp = itemPrices;
            }
        });

        searchItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
// Initialize dialog
                dialog=new Dialog(CreateInvoiceActivity.this);

                // set custom dialog
                dialog.setContentView(R.layout.searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout(800,1400);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText=dialog.findViewById(R.id.search_box);
                listView=dialog.findViewById(R.id.list_view);
                Button close = dialog.findViewById(R.id.choose_close);

                close.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                // Initialize array adapter
//                ArrayAdapter<String> adapter=new ArrayAdapter<>(CreateInvoiceActivity.this, android.R.layout.simple_list_item_1,arrayList);
                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ArrayList<ItemPrices> filteredList=new ArrayList<>();
                        for(int i = 0; i< itemPricesListFilter.size(); i++){
                            if(itemPricesListFilter.get(i).getItemName().toLowerCase().contains(s.toString().toLowerCase())){
                                filteredList.add(itemPricesListFilter.get(i));
                            }
                        }
                        adapter = new SearchableSpinnerAdapter(CreateInvoiceActivity.this,filteredList);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        ItemPrices itemPrices = (ItemPrices) parent.getAdapter().getItem(position);
                        itemPricesList.add(itemPrices);
                        InventoryAdapter inventoryAdapter = new InventoryAdapter(itemPricesList, CreateInvoiceActivity.this, CreateInvoiceActivity.this);
                        invoiceList.setLayoutManager(new LinearLayoutManager(CreateInvoiceActivity.this));
                        invoiceList.setAdapter(inventoryAdapter);
                        invoiceList.setLayoutManager(layoutManager);
                        adapter = new SearchableSpinnerAdapter(CreateInvoiceActivity.this,(ArrayList<ItemPrices>) itemPricesListTemp);
                        listView.setAdapter(adapter);
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });

            }
            });
        }
    }
