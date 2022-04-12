package com.shashank.spendistrybusiness.DialogFragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

@SuppressWarnings("ALL")
public class EditDialog extends DialogFragment {

    private static final String TAG = "EditDialog";

    public interface OnEditConfirmationListener {
        void sendEditConfirmation(boolean send);
    }
    OnEditConfirmationListener mOnSentListener;

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        mOnSentListener.sendEditConfirmation(true);
    }



    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_dialog,container, false);
        Bundle bundle = getArguments();
        requireDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //
        final String id = bundle.getString("id");
        final String barcodeString = bundle.getString("barcode");
        final String nameString = bundle.getString("name");
        final String priceString = bundle.getString("price");
        final String emailString = bundle.getString("email");
        final String frag = bundle.getString("frag");
        //
        TextView barcode = rootView.findViewById(R.id.barcode_edit);
        EditText itemName = rootView.findViewById(R.id.item_name_edit);
        EditText itemPrice = rootView.findViewById(R.id.price_edit);
        Button update = rootView.findViewById(R.id.update_btn);
        Button cancel = rootView.findViewById(R.id.cancel_btn);
        //
        barcode.setText("barcode: "+barcodeString);
        itemName.setText(nameString);
        itemPrice.setText(priceString);
        //
        InventoryViewModel inventoryViewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
        //
        update.setOnClickListener(view -> {
            if (frag.equals("scan")) {
                mOnSentListener.sendEditConfirmation(true);
            }
            inventoryViewModel.updateElement(emailString, id, new ItemPrices(id,barcodeString, emailString,itemName.getText().toString(), itemPrice.getText().toString()));
            requireDialog().dismiss();
        });

        cancel.setOnClickListener(view -> {
            mOnSentListener.sendEditConfirmation(true);
            requireDialog().dismiss();
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnSentListener = (OnEditConfirmationListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
