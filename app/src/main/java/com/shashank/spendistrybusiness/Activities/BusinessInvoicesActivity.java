package com.shashank.spendistrybusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.shashank.spendistrybusiness.Adapters.BusinessInvoiceAdapter;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessInvoices;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;

import java.util.List;
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
        RecyclerView recyclerView = findViewById(R.id.issued_invoices);
        InvoiceViewModel invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        invoiceViewModel.getBusinessInvoices(email).observe(this, new Observer<BusinessInvoices>() {
            @Override
            public void onChanged(BusinessInvoices businessInvoices) {
                ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
                invoiceAdapter = new BusinessInvoiceAdapter( businessInvoices.getInvoices(), BusinessInvoicesActivity.this, BusinessInvoicesActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BusinessInvoicesActivity.this){
                    /**
                     * @return true if {@link #getOrientation()} is {@link #VERTICAL}
                     */
                    @Override
                    public boolean canScrollVertically() {
                        return true;
                    }
                };
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(invoiceAdapter);
                recyclerView.setHasFixedSize(true);
            }
        });
    }


}