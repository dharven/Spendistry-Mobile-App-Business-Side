package com.shashank.spendistrybusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;

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
        fragmentManager = getSupportFragmentManager();
        manualInventoryFragment = new ManualInventoryFragment();
        scanInventoryFragment = new ScanInventoryFragment();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.frame, manualInventoryFragment).commit();

        }
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                           navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                               @Override
                               public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                  switch (item.getItemId()){
                                      case R.id.page_1:
                                          fragmentManager.beginTransaction().replace(R.id.frame, manualInventoryFragment).commit();
                                          return true;
                                      case R.id.page_2:
                                          fragmentManager.beginTransaction().replace(R.id.frame, scanInventoryFragment).addToBackStack(null).commit();
                                          return true;
                                          default:
                                              return false;
                                  }
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
}