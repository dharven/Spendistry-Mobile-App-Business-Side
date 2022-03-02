package com.shashank.spendistrybusiness.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InventoryViewModel;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class ScanEntryDialog extends DialogFragment {
    private static final String TAG = "ScanEntryDialog";
    private final Bundle b = new Bundle();

    public interface OnScanEntryDialogListener {
        void sendEntryConfirmation(boolean send);
    }
    OnScanEntryDialogListener mOnScanEntryDialogListener;

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }



    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scan_entry_dialog,container, false);
        Bundle bundle = getArguments();
        //
        final String barcodeString = bundle.getString("barcode");
        final String emailString = bundle.getString("email");
        //
        TextView barcode = rootView.findViewById(R.id.barcode_scan);
        EditText itemName = rootView.findViewById(R.id.item_name_scan);
        EditText itemPrice = rootView.findViewById(R.id.price_scan);
        Button add = rootView.findViewById(R.id.add_btn_scan);
        Button cancel = rootView.findViewById(R.id.cancel_btn_scan);
        //
        barcode.setText("barcode: "+barcodeString);
        //


        InventoryViewModel inventoryViewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
        //
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectId id = new ObjectId();
                ArrayList<ItemPrices> itemPrices = new ArrayList<>();
                ItemPrices item = new ItemPrices(id.toString(),barcodeString, itemName.getText().toString() , itemPrice.getText().toString());
                itemPrices.add(item);
                inventoryViewModel.setInventory(emailString,itemPrices);
                requireDialog().dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireDialog().dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnScanEntryDialogListener = (OnScanEntryDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
