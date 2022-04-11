package com.shashank.spendistrybusiness.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;

public class MainActivity extends AppCompatActivity {
    private CodeScanner codeScanner;
    private LinearLayout linearLayout;
    private InvoiceViewModel invoiceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //    private DecoratedBarcodeView barcodeView;
        CodeScannerView scannerView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, scannerView);
        linearLayout = findViewById(R.id.main_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
    }

    private void ScanCamera() {
        codeScanner.startPreview();
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> invoiceViewModel.verifyQR(linearLayout,result.getText()).observe(MainActivity.this, s -> {
                Intent intent2 = new Intent(MainActivity.this, CreateInvoiceActivity.class);
                intent2.putExtra("SCAN_RESULT", s);
                startActivity(intent2);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        })));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ScanCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Snackbar snackbar = Snackbar.make(linearLayout, "Please grant this permission to use this application", Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(ContextCompat.getColor(MainActivity.this,R.color.cardBlue));
                        snackbar.setAction("Grant", view -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        });
                        snackbar.show();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}