package com.shashank.spendistrybusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.shashank.spendistrybusiness.Fragments.ManualInvoiceFragment;
import com.shashank.spendistrybusiness.Fragments.ScanInvoiceFragment;
import com.shashank.spendistrybusiness.R;

public class CreateInvoiceActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private FrameLayout frameLayout;
    private ManualInvoiceFragment manualInvoiceFragment;
    private ScanInvoiceFragment scanInvoiceFragment;
//    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        Toolbar toolbar = findViewById(R.id.toolbar_invoice);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        Intent intent = getIntent();
        final String ClientEmail = intent.getStringExtra("SCAN_RESULT");
        navigationView = findViewById(R.id.navigationViewInvoice);
        frameLayout = findViewById(R.id.frame);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.cardBlue));
        manualInvoiceFragment = new ManualInvoiceFragment();
        scanInvoiceFragment = new ScanInvoiceFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("SCAN_RESULT", ClientEmail);
        scanInvoiceFragment.setArguments(bundle);
        manualInvoiceFragment.setArguments(bundle);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.frame, manualInvoiceFragment).commit();
        }

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        fragmentManager.beginTransaction().replace(R.id.frame, manualInvoiceFragment).commit();

                        return true;
                    case R.id.page_2:
                        fragmentManager.beginTransaction().remove(manualInvoiceFragment).add(R.id.frame, scanInvoiceFragment).addToBackStack("added").commit();

                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (manualInvoiceFragment.isVisible()) {
            super.onBackPressed();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        } else {
            getSupportFragmentManager().beginTransaction().remove(scanInvoiceFragment).add(R.id.frame, manualInvoiceFragment).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }
}
