package com.shashank.spendistrybusiness.Fragments;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InventoryViewModel;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class ScanInventoryFragment extends Fragment {

    private String barcode;
    private DecoratedBarcodeView barcodeView;
    private EditText itemName, itemPrice;
    private TextView barcodeText;
    private LinearLayout scan_inventory_layout;

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
        Button next = rootView.findViewById(R.id.btn_scan);
        itemName = rootView.findViewById(R.id.itemNameScan);
        itemPrice = rootView.findViewById(R.id.itemPriceScan);
        barcodeText = rootView.findViewById(R.id.barcode_tv);
        scan_inventory_layout = rootView.findViewById(R.id.scan_inventory_layout);
        //
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loggedIn", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        //
        InventoryViewModel inventoryViewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
        Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ScanCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(requireActivity(), "Please grant this permission to use this app", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectId id = new ObjectId();
                ArrayList<ItemPrices> itemPrices = new ArrayList<>();
                ItemPrices item = new ItemPrices(id.toString(),barcode, itemName.getText().toString() , itemPrice.getText().toString());
                itemPrices.add(item);
                inventoryViewModel.setInventory(email,itemPrices);
                barcodeText.setVisibility(View.GONE);
                barcodeView.setVisibility(View.VISIBLE);
                scan_inventory_layout.setVisibility(View.GONE);
            }
        });
        return rootView;
    }

    private void ScanCamera() {
        barcodeView.resume();
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                barcode = result.getText();
                Toast.makeText(requireContext(), ""+barcode, Toast.LENGTH_SHORT).show();
                barcodeText.setVisibility(View.VISIBLE);
                barcodeText.setText("barcode number " +barcode);
                barcodeView.setVisibility(View.GONE);
                scan_inventory_layout.setVisibility(View.VISIBLE);
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

}