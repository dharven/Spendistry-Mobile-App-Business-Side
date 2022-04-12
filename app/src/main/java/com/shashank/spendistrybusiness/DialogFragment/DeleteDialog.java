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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InventoryViewModel;

@SuppressWarnings("ALL")
public class DeleteDialog extends DialogFragment {
    private static final String TAG = "DeleteDialog";
//    private final Bundle b = new Bundle();

    public interface OnDeleteDialogListener {
        void sendDeleteConfirmation(boolean send);
    }
    OnDeleteDialogListener mOnDeleteListener;

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        mOnDeleteListener.sendDeleteConfirmation(true);
    }



    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.delete_dialog,container, false);
        requireDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Bundle bundle = getArguments();
        //
        final String id = bundle.getString("id");
        final String barcodeString = bundle.getString("barcode");
        final String nameString = bundle.getString("name");
        final String priceString = bundle.getString("price");
        final String emailString = bundle.getString("email");
        //
        TextView barcode = rootView.findViewById(R.id.barcode_delete);
        TextView itemName = rootView.findViewById(R.id.item_name_delete);
        TextView itemPrice = rootView.findViewById(R.id.item_price_delete);
        Button delete = rootView.findViewById(R.id.delete_btn);
        Button cancel = rootView.findViewById(R.id.cancel_delete_btn);
        //
        barcode.setText("barcode: "+barcodeString);
        itemName.setText("Item name: "+nameString);
        itemPrice.setText("Item Price: "+priceString);
        //
        InventoryViewModel inventoryViewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
        //
        delete.setOnClickListener(view -> {
            inventoryViewModel.deleteElement(emailString, id);
            requireDialog().dismiss();
        });

        cancel.setOnClickListener(view -> {
            mOnDeleteListener.sendDeleteConfirmation(true);
            requireDialog().dismiss();
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnDeleteListener = (OnDeleteDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
