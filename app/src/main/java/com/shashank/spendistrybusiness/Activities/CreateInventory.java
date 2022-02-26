package com.shashank.spendistrybusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.spendistrybusiness.Adapters.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.ManualInvoiceViewModel;

public class CreateInventory extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_inventory);
        viewPager = findViewById(R.id.invoice_view_pager);
        tabLayout = findViewById(R.id.tabLayout);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 2);
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 1);
                        Toast.makeText(CreateInventory.this, "Please grant this permission to use this app", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Add Items" );
        tabLayout.getTabAt(1).setText("Scan & Add Items" );
    }
}