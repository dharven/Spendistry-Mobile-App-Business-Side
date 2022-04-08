package com.shashank.spendistrybusiness.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.spendistrybusiness.Fragments.ManualInventoryFragment;
import com.shashank.spendistrybusiness.Fragments.ScanInventoryFragment;
import com.shashank.spendistrybusiness.R;

public class CreateInventoryActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private FragmentManager fragmentManager;
    private ManualInventoryFragment manualInventoryFragment;
    private ScanInventoryFragment scanInventoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_inventory);
        navigationView = findViewById(R.id.navigationView);
        Toolbar toolbar = findViewById(R.id.toolbar_inventory);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.cardBlue));
        fragmentManager = getSupportFragmentManager();
        manualInventoryFragment = new ManualInventoryFragment();
        scanInventoryFragment = new ScanInventoryFragment();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.frame, manualInventoryFragment).commit();
        }
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        navigationView.setOnItemSelectedListener(item -> {
                            switch (item.getItemId()) {
                                case R.id.page_1:
                                    fragmentManager.beginTransaction().replace(R.id.frame, manualInventoryFragment).commit();
                                    return true;
                                case R.id.page_2:
                                    fragmentManager.beginTransaction().remove(manualInventoryFragment).add(R.id.frame, scanInventoryFragment).commit();
                                    return true;
                                default:
                                    return false;
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        fragmentManager.beginTransaction().replace(R.id.frame, manualInventoryFragment).commit();
                        navigationView.getMenu().findItem(R.id.page_2).setVisible(false);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();


    }

    @Override
    public void onBackPressed() {
        if (manualInventoryFragment.isVisible()) {
            super.onBackPressed();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else {
            getSupportFragmentManager().beginTransaction().remove(scanInventoryFragment).add(R.id.frame, manualInventoryFragment).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }
}