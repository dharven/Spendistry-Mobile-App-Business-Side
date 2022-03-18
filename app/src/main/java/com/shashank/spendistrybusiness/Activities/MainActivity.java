package com.shashank.spendistrybusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.scottyab.aescrypt.AESCrypt;
import com.shashank.spendistrybusiness.R;

import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    //    private DecoratedBarcodeView barcodeView;
    private CodeScannerView scannerView;
    private CodeScanner codeScanner;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scannerView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, scannerView);
        linearLayout = findViewById(R.id.main_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
    }

    private void ScanCamera() {
        codeScanner.startPreview();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String decrypted = getID(result.getText());
                        if (!decrypted.equals("")) {
                            Intent intent = new Intent(MainActivity.this, CreateInvoiceActivity.class);
                            intent.putExtra("SCAN_RESULT", decrypted);
                            startActivity(intent);
                        } else {
                                codeScanner.startPreview();
                        }
                    }
                });
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
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public String getID(String id) {
        String password = "Spendistryqasdertgbvcxz";
        String messageAfterDecrypt = "";
        try {
            messageAfterDecrypt = AESCrypt.decrypt(password, id);
        } catch (GeneralSecurityException e) {
            //handle error - could be due to incorrect password or tampered encryptedMsg
            messageAfterDecrypt = "";
            Snackbar snackbar = Snackbar.make(linearLayout, "Wrong QR Code", Snackbar.LENGTH_SHORT);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(ContextCompat.getColor(MainActivity.this,R.color.red));
            snackbar.show();
        } catch (IllegalArgumentException e) {
            Snackbar snackbar = Snackbar.make(linearLayout, "QR Code Only", Snackbar.LENGTH_SHORT);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(ContextCompat.getColor(MainActivity.this,R.color.red));
            snackbar.show();
        }
        return messageAfterDecrypt;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
            SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("loggedIn", false).apply();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
            case R.id.inventory:
            Intent intent1 = new Intent(MainActivity.this, CreateInventoryActivity.class);
            startActivity(intent1);
            return true;
            case R.id.temp:
                Intent intent2 = new Intent(MainActivity.this, CreateInvoiceActivity.class);
                intent2.putExtra("SCAN_RESULT", "shashank@gmail.com");
                startActivity(intent2);
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        snackbar.setTextColor(Color.RED);
                        snackbar.setBackgroundTint(ContextCompat.getColor(MainActivity.this,R.color.mainBlue));
                        snackbar.show();                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
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