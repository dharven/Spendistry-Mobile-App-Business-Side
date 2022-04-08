package com.shashank.spendistrybusiness.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shashank.spendistrybusiness.Adapters.BusinessInvoiceAdapter;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;

import java.util.Objects;

public class BusinessInvoicesActivity extends AppCompatActivity {
    private BusinessInvoiceAdapter invoiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_invoices);
        Toolbar toolbar = findViewById(R.id.toolbar_issued_invoices);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_issued);
        RecyclerView recyclerView = findViewById(R.id.issued_invoices);
        InvoiceViewModel invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.mainBlue),ContextCompat.getColor(this,R.color.cardBlue), ContextCompat.getColor(this,R.color.windowBlue));
        LinearLayout linearLayout = findViewById(R.id.business_invoices_layout);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        swipeRefreshLayout.setOnRefreshListener(() -> invoiceViewModel.getBusinessInvoices(email)
                .observe(this, businessInvoices -> {
                    dialog.dismiss();
                    ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
                    invoiceAdapter = new BusinessInvoiceAdapter(businessInvoices.getInvoices(), BusinessInvoicesActivity.this ,BusinessInvoicesActivity.this,BusinessInvoicesActivity.this, linearLayout, invoiceViewModel);
                    recyclerView.setAdapter(invoiceAdapter);
                    new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1500);
                }));


        invoiceViewModel.getBusinessInvoices(email).observe(this, businessInvoices -> {
            dialog.dismiss();
            ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
            invoiceAdapter = new BusinessInvoiceAdapter(businessInvoices.getInvoices(), BusinessInvoicesActivity.this, BusinessInvoicesActivity.this, BusinessInvoicesActivity.this, linearLayout, invoiceViewModel);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BusinessInvoicesActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(invoiceAdapter);
            recyclerView.setHasFixedSize(true);
        });
    }


}