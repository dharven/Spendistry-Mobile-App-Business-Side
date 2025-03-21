package com.shashank.spendistrybusiness.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;


public class ScanInvoiceFragment extends Fragment {
    private InvoiceViewModel invoiceViewModel;
    private CodeScanner codeScanner;
    private CodeScannerView scannerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_invoice, container, false);

        scannerView = view.findViewById(R.id.scannerViewInvoice);
        codeScanner = new CodeScanner(requireContext(),scannerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email",null);
        invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
        invoiceViewModel.getItemPrices(email).observe(requireActivity(), itemPrices -> {
            codeScanner.startPreview();
            codeScanner.setDecodeCallback(result -> requireActivity().runOnUiThread(() -> {
                boolean exists = false;
                for (ItemPrices itemPrice : itemPrices) {
                    if (itemPrice.getBarcode().equals(result.getText())) {
                        Dialog dialog1 = new Dialog(requireContext());
                        dialog1.setContentView(R.layout.quantity_dialog);
                        dialog1.getWindow().setLayout(800, 1400);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.show();
                        final TextView itemName = dialog1.findViewById(R.id.item_name_quantity);
                        final EditText Price = dialog1.findViewById(R.id.price_quantity);
                        EditText itemQuantity = dialog1.findViewById(R.id.quantity);
                        final Button add = dialog1.findViewById(R.id.add_quantity);
                        final Button cancel = dialog1.findViewById(R.id.cancel_quantity);
                        itemName.setText(itemPrice.getItemName());
                        Price.setText(itemPrice.getPrice());
                        itemQuantity.setText("1");
                        cancel.setOnClickListener(view -> dialog1.dismiss());
                        add.setOnClickListener(view -> {
                            ItemPrices itemPrices1 = new ItemPrices(itemPrice.getBarcode(),itemPrice.getItemName(),Integer.parseInt(itemQuantity.getText().toString()), Price.getText().toString() );
                            invoiceViewModel.setInvoice(itemPrices1);
                            dialog1.dismiss();
                        });
                        exists = true;
                    }
                    codeScanner.stopPreview();
                }
                if (!exists) {
                    Snackbar snackbar = Snackbar.make(scannerView, "Item not found \nAdd it to the inventory", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));
                    snackbar.show();
                }
                new Handler().postDelayed(() -> codeScanner.startPreview(), 1000);
            }));
        });
    }
}