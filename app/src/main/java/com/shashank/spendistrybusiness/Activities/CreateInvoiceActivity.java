package com.shashank.spendistrybusiness.Activities;

import static android.view.View.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.spendistrybusiness.Adapters.InventoryAdapter;
import com.shashank.spendistrybusiness.Adapters.PagerAdapter;
import com.shashank.spendistrybusiness.Adapters.SearchableSpinnerAdapter;
import com.shashank.spendistrybusiness.Constants.GlobalVariables;
import com.shashank.spendistrybusiness.Fragments.ManualInvoiceFragment;
import com.shashank.spendistrybusiness.Fragments.ScanInvoiceFragment;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessArray;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreateInvoiceActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        Intent intent = getIntent();
        final String ClientEmail = intent.getStringExtra("SCAN_RESULT");
        navigationView = findViewById(R.id.navigationViewInvoice);
        viewPager = findViewById(R.id.InvoicePager);
        ManualInvoiceFragment manualInvoiceFragment = new ManualInvoiceFragment();
        ScanInvoiceFragment scanInvoiceFragment = new ScanInvoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SCAN_RESULT", ClientEmail);
        scanInvoiceFragment.setArguments(bundle);
        manualInvoiceFragment.setArguments(bundle);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 2, manualInvoiceFragment, scanInvoiceFragment);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                navigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 1, manualInvoiceFragment, scanInvoiceFragment);
                        viewPager.setAdapter(pagerAdapter);
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);

                        return true;
                    case R.id.page_2:
                        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 2, manualInvoiceFragment, scanInvoiceFragment);
                        viewPager.setAdapter(pagerAdapter);
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1){
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        } else {
            GlobalVariables.itemPricesArrayList.clear();
            super.onBackPressed();
        }
    }
}
