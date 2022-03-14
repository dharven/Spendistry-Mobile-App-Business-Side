package com.shashank.spendistrybusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.shashank.spendistrybusiness.Adapters.PagerAdapter;
import com.shashank.spendistrybusiness.Constants.GlobalVariables;
import com.shashank.spendistrybusiness.Fragments.ManualInvoiceFragment;
import com.shashank.spendistrybusiness.Fragments.ScanInvoiceFragment;
import com.shashank.spendistrybusiness.R;

public class CreateInvoiceActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private FrameLayout frameLayout;
//    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        Intent intent = getIntent();
        final String ClientEmail = intent.getStringExtra("SCAN_RESULT");
        navigationView = findViewById(R.id.navigationViewInvoice);
        frameLayout = findViewById(R.id.frame);
        ManualInvoiceFragment manualInvoiceFragment = new ManualInvoiceFragment();
        ScanInvoiceFragment scanInvoiceFragment = new ScanInvoiceFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("SCAN_RESULT", ClientEmail);
        scanInvoiceFragment.setArguments(bundle);
        manualInvoiceFragment.setArguments(bundle);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.frame, manualInvoiceFragment).commit();

        }

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        fragmentManager.beginTransaction().replace(R.id.frame, manualInvoiceFragment).commit();

                        return true;
                    case R.id.page_2:
                        fragmentManager.beginTransaction().replace(R.id.frame, scanInvoiceFragment).addToBackStack("added").commit();

                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            navigationView.getMenu().getItem(0).setChecked(true);
        } else {
            super.onBackPressed();
        }
    }
}
