package com.shashank.spendistrybusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    private DecoratedBarcodeView barcodeView;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barcodeView = findViewById(R.id.barcodeView);
        logout = findViewById(R.id.logout);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ScanCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Please grant this permission to use this app", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    private void ScanCamera() {
        barcodeView.resume();
        barcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                barcodeView.pause();
                Intent intent = new Intent(MainActivity.this, CreateInventory.class);
                intent.putExtra("SCAN_RESULT", result.getText());
                startActivity(intent);
            }
        });
        barcodeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                barcodeView.pause();
                barcodeView.resume();
                return true;
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("loggedIn", false).apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getID(String id){
        String password = "Spendistryqasdertgbvcxz";
        String messageAfterDecrypt ="";
        try {
            messageAfterDecrypt = AESCrypt.decrypt(password, id);
        }catch (GeneralSecurityException e){
            //handle error - could be due to incorrect password or tampered encryptedMsg
        } catch (IllegalArgumentException e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return messageAfterDecrypt;
    }
}