package com.shashank.spendistrybusiness.Fragments;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;

import org.bson.types.ObjectId;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shashank.spendistrybusiness.Adapters.InventoryAdapter;
import com.shashank.spendistrybusiness.DialogFragment.DeleteDialog;
import com.shashank.spendistrybusiness.DialogFragment.EditDialog;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InventoryViewModel;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ManualInventoryFragment extends Fragment implements EditDialog.OnEditConfirmationListener, DeleteDialog.OnDeleteDialogListener {
    private InventoryAdapter inventoryAdapter;
    private String email;
    private ItemPrices recentlyRemoved;
    private int recentPosition;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manual_inventory, container, false);
        EditText itemPrice = rootView.findViewById(R.id.itemPrice);
        EditText itemName = rootView.findViewById(R.id.itemName);
        Button addItem = rootView.findViewById(R.id.add_btn);
        RecyclerView itemList = rootView.findViewById(R.id.rv_inventory);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loggedIn", getActivity().MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        itemList.setLayoutManager(layoutManager);

        InventoryViewModel inventoryViewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);


        inventoryViewModel.getInventory(email).observe(requireActivity(), new Observer<List<ItemPrices>>() {

            @Override
            public void onChanged(List<ItemPrices> itemPrices) {
                inventoryAdapter = new InventoryAdapter(itemPrices, requireContext(), getActivity());
                itemList.setLayoutManager(new LinearLayoutManager(requireContext()));
                itemList.setAdapter(inventoryAdapter);
                itemList.animate().alpha(1.0f);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectId id = new ObjectId();
                if (itemPrice.getText().toString().isEmpty() || itemName.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter all the details", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<ItemPrices> itemPrices = new ArrayList<>();
                    ItemPrices item = new ItemPrices(id.toString(), "", itemName.getText().toString(), itemPrice.getText().toString());
                    itemPrices.add(item);
                    inventoryViewModel.setInventory(email, itemPrices);
                    itemPrice.setText("");
                    itemName.setText("");

                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
              if (direction == ItemTouchHelper.LEFT) {
                  Bundle bundle = new Bundle();
                  bundle.putString("id", inventoryAdapter.getId(viewHolder.getBindingAdapterPosition()));
                  bundle.putString("barcode", inventoryAdapter.getBarcode(viewHolder.getBindingAdapterPosition()));
                  bundle.putString("name", inventoryAdapter.getItemName(viewHolder.getBindingAdapterPosition()));
                  bundle.putString("price", inventoryAdapter.getPrice(viewHolder.getBindingAdapterPosition()));
                  bundle.putString("email", email);
                  recentPosition = viewHolder.getBindingAdapterPosition();
                  recentlyRemoved = inventoryAdapter.recentRemove(viewHolder.getBindingAdapterPosition());
                  DeleteDialog deleteDialog = new DeleteDialog();
                  deleteDialog.setArguments(bundle);
                  deleteDialog.setTargetFragment(ManualInventoryFragment.this, 1);
                  deleteDialog.show(getParentFragmentManager(), "DeleteDialog");
              } else if (direction == ItemTouchHelper.RIGHT) {

                  Bundle bundle = new Bundle();
                  bundle.putString("id", inventoryAdapter.getId(viewHolder.getBindingAdapterPosition()));
                  bundle.putString("barcode", inventoryAdapter.getBarcode(viewHolder.getBindingAdapterPosition()));
                  bundle.putString("name", inventoryAdapter.getItemName(viewHolder.getBindingAdapterPosition()));
                  bundle.putString("price", inventoryAdapter.getPrice(viewHolder.getBindingAdapterPosition()));
                  bundle.putString("email", email);
                  recentPosition = viewHolder.getBindingAdapterPosition();
                  recentlyRemoved = inventoryAdapter.recentRemove(viewHolder.getBindingAdapterPosition());
                  EditDialog editDialog = new EditDialog();
                  editDialog.setArguments(bundle);
                  editDialog.setTargetFragment(ManualInventoryFragment.this, 1);
                  editDialog.show(getParentFragmentManager(), "EditDialog");
              }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal_200))
                        .addSwipeLeftActionIcon(R.drawable.close)
                        .addSwipeLeftLabel("DELETE")
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .addSwipeRightActionIcon(R.drawable.edit)
                        .addSwipeRightLabel("EDIT")
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(itemList);


            return rootView;
    }

    @Override
    public void sendDeleteConfirmation(boolean send) {
        if (send) {
            inventoryAdapter.undoRecent(recentPosition,recentlyRemoved);
        }
    }

    @Override
    public void sendEditConfirmation(boolean send) {
        if (send) {
            inventoryAdapter.undoRecent(recentPosition,recentlyRemoved);
        }
    }
}