package com.shashank.spendistrybusiness.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shashank.spendistrybusiness.Adapters.BusinessInvoiceAdapter;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;
import com.shashank.spendistrybusiness.ViewModels.ReturnedInvoicesViewModel;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public class BusinessInvoicesActivity extends AppCompatActivity {
    private BusinessInvoiceAdapter invoiceAdapter;
    private TextView toolbarTitle;
    private List<Invoice> invoiceList;

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
        toolbarTitle = findViewById(R.id.toolbar_title);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_issued);
        RecyclerView recyclerView = findViewById(R.id.issued_invoices);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String activity = intent.getStringExtra("activity");
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.mainBlue),ContextCompat.getColor(this,R.color.cardBlue), ContextCompat.getColor(this,R.color.windowBlue));
        LinearLayout linearLayout = findViewById(R.id.business_invoices_layout);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (activity.equals("issued")) {
                getIssuedInvoices(email, recyclerView, swipeRefreshLayout, dialog, linearLayout);
            } else {
                getReturnedInvoices(email, recyclerView, swipeRefreshLayout, dialog, linearLayout);
            }
        });


        if (activity.equals("issued")) {
            getIssuedInvoices(email, recyclerView, swipeRefreshLayout, dialog, linearLayout);
        } else {
            getReturnedInvoices(email, recyclerView, swipeRefreshLayout, dialog, linearLayout);
        }
    }

    private void getReturnedInvoices(String email, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, Dialog dialog, LinearLayout linearLayout) {
        ReturnedInvoicesViewModel returnedInvoicesViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(), email)).get(ReturnedInvoicesViewModel.class);
        returnedInvoicesViewModel.getReturnedInvoices().observe(this, returnedInvoices -> {
            dialog.dismiss();
            ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
            invoiceAdapter = new BusinessInvoiceAdapter(returnedInvoices, BusinessInvoicesActivity.this, BusinessInvoicesActivity.this, BusinessInvoicesActivity.this, linearLayout, null, true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BusinessInvoicesActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(invoiceAdapter);
            recyclerView.setHasFixedSize(true);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void getIssuedInvoices(String email, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, Dialog dialog, LinearLayout linearLayout) {
        InvoiceViewModel invoiceViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(),email)).get(InvoiceViewModel.class);
        invoiceViewModel.getBusinessInvoices().observe(this, businessInvoices -> {
            dialog.dismiss();
            ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
            invoiceAdapter = new BusinessInvoiceAdapter(businessInvoices.getInvoices(), BusinessInvoicesActivity.this, BusinessInvoicesActivity.this, BusinessInvoicesActivity.this, linearLayout, invoiceViewModel, false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BusinessInvoicesActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(invoiceAdapter);
            recyclerView.setHasFixedSize(true);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invoice_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setOnQueryTextFocusChangeListener((view, b) -> {
            if (!b) {
               toolbarTitle.setVisibility(View.VISIBLE);
            } else {
                toolbarTitle.setVisibility(View.GONE);
                invoiceList = invoiceAdapter.getList();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                invoiceAdapter.searchQuery(newText, invoiceList);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}