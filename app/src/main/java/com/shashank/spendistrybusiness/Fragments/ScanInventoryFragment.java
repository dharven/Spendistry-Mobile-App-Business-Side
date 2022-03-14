package com.shashank.spendistrybusiness.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Constants.GlobalVariables;
import com.shashank.spendistrybusiness.DialogFragment.EditDialog;
import com.shashank.spendistrybusiness.DialogFragment.ScanEntryDialog;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InventoryViewModel;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ScanInventoryFragment extends Fragment implements EditDialog.OnEditConfirmationListener {

    private String barcode;
    private DecoratedBarcodeView barcodeView;
    private String email;
    private List<ItemPrices> itemPricesList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scan_inventory, container, false);

        barcodeView = rootView.findViewById(R.id.barcodeScanner);
        //
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        //
        InventoryViewModel inventoryViewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
        inventoryViewModel.getInventory(email).observe(requireActivity(), new Observer<List<ItemPrices>>() {
            @Override
            public void onChanged(List<ItemPrices> itemPrices) {
                itemPricesList = itemPrices;
            }
        });
        Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ScanCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(requireActivity(), "Please grant this permission to use this app", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

        return rootView;
    }

    private void ScanCamera() {
        barcodeView.resume();
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                barcodeView.pause();
                barcode = result.getText();
                boolean exists = false;
                for (int i =0; i < itemPricesList.size(); i++) {
                    if (itemPricesList.get(i).getBarcode().equals(barcode)) {
                        Bundle bundle = new Bundle();
                        exists = true;
                        bundle.putString("barcode", barcode);
                        bundle.putString("email", email);
                        bundle.putString("name", itemPricesList.get(i).getItemName());
                        bundle.putString("price", itemPricesList.get(i).getPrice());
                        bundle.putString("frag", "scan");
                        bundle.putString("id", itemPricesList.get(i).getId());
                        EditDialog editDialog = new EditDialog();
                        editDialog.setArguments(bundle);
                        editDialog.setTargetFragment(ScanInventoryFragment.this, 1);
                        editDialog.show(getParentFragmentManager(), "EditDialog");
                    }
                }
                if (!exists) {
                    Bundle bundle = new Bundle();
                    bundle.putString("barcode", barcode);
                    bundle.putString("email", email);
                    ScanEntryDialog scanEntryDialog = new ScanEntryDialog();
                    scanEntryDialog.setArguments(bundle);
                    scanEntryDialog.setTargetFragment(ScanInventoryFragment.this, 1);
                    scanEntryDialog.show(getParentFragmentManager(), "scanEntryDialog");
                }
//                Toast.makeText(requireContext(), ""+barcode, Toast.LENGTH_SHORT).show();
//                barcodeText.setText("barcode number " +barcode);

            }
        });
        barcodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeView.resume();
            }
        });

        barcodeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                barcodeView.pause();
                barcodeView.resume();
                return true;
            }
        });
    }

    @Override
    public void sendEditConfirmation(boolean send) {
        if (send) {
            barcodeView.resume();
        }
    }
}