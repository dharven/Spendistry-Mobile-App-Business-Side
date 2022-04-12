package com.shashank.spendistrybusiness.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.spendistrybusiness.DialogFragment.EditDialog;
import com.shashank.spendistrybusiness.DialogFragment.ScanEntryDialog;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistrybusiness.ViewModels.InventoryViewModel;

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
        InventoryViewModel inventoryViewModel = new ViewModelProvider(requireActivity(), (ViewModelProvider.Factory) new ViewModelFactory(requireActivity().getApplication(), email)).get(InventoryViewModel.class);
        inventoryViewModel.getInventory().observe(requireActivity(), itemPrices -> itemPricesList = itemPrices);
        Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ScanCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Snackbar snackbar = Snackbar.make(barcodeView, "Please grant this permission!", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));
                        snackbar.show();                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
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
        barcodeView.decodeContinuous(result -> {
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

        });
        barcodeView.setOnClickListener(view -> barcodeView.resume());

        barcodeView.setOnLongClickListener(view -> {
            barcodeView.pause();
            barcodeView.resume();
            return true;
        });
    }

    @Override
    public void sendEditConfirmation(boolean send) {
        if (send) {
            barcodeView.resume();
        }
    }
}