package com.shashank.spendistrybusiness.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Adapters.InventoryAdapter;
import com.shashank.spendistrybusiness.DialogFragment.DeleteDialog;
import com.shashank.spendistrybusiness.DialogFragment.EditDialog;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistrybusiness.ViewModels.InventoryViewModel;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ManualInventoryFragment extends Fragment implements EditDialog.OnEditConfirmationListener, DeleteDialog.OnDeleteDialogListener {
    private InventoryAdapter inventoryAdapter;
    private String email;
    private ItemPrices recentlyRemoved;
    private int recentPosition;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView itemList;
    private InventoryViewModel inventoryViewModel;
    private LinearLayoutManager layoutManager;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manual_inventory, container, false);
        EditText itemPrice = rootView.findViewById(R.id.itemPrice);
        EditText itemName = rootView.findViewById(R.id.itemName);
        Button addItem = rootView.findViewById(R.id.add_btn);
        itemList = rootView.findViewById(R.id.rv_inventory);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        sharedPreferences = requireActivity().getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");

        inventoryViewModel = new ViewModelProvider(requireActivity(), (ViewModelProvider.Factory) new ViewModelFactory(requireActivity().getApplication(), email)).get(InventoryViewModel.class);
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        LinearLayout linearLayout = rootView.findViewById(R.id.manual_invoice);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(),R.color.mainBlue),ContextCompat.getColor(requireContext(),R.color.cardBlue), ContextCompat.getColor(requireContext(),R.color.windowBlue));
        swipeRefreshLayout.setRefreshing(true);
        try {
            inventoryViewModel.setInventory(email, new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        inventoryViewModel.getInventory().observe(getViewLifecycleOwner(), itemPrices -> {
            inventoryAdapter = new InventoryAdapter(itemPrices, requireContext(), getActivity());
            itemList.setLayoutManager(new LinearLayoutManager(requireContext()));
            itemList.setAdapter(inventoryAdapter);
            itemList.setLayoutManager(layoutManager);
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            sharedPreferences.edit().putBoolean("hasData", true).apply();
            new Handler().postDelayed(() -> {
                inventoryViewModel.getInventory();
                swipeRefreshLayout.setRefreshing(false);
            },1500);

        });

        addItem.setOnClickListener(view -> {
            ObjectId id = new ObjectId();
            if (itemPrice.getText().toString().isEmpty() || itemName.getText().toString().isEmpty()) {
                Snackbar snackbar = Snackbar.make(linearLayout, "Please enter all details", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.red));
                snackbar.show();
            } else {
                ArrayList<ItemPrices> itemPrices = new ArrayList<>();
                ItemPrices item = new ItemPrices(id.toString(), email,"",itemName.getText().toString(), itemPrice.getText().toString());
                itemPrices.add(item);
                inventoryViewModel.setInventory(email, itemPrices);
                itemPrice.setText("");
                itemName.setText("");


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
                    //remove bg
                    deleteDialog.setCancelable(false);
                    deleteDialog.setArguments(bundle);
                    deleteDialog.setTargetFragment(ManualInventoryFragment.this, 1);
                    deleteDialog.show(getParentFragmentManager(), "DeleteDialog");
                } else if (direction == ItemTouchHelper.RIGHT) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", inventoryAdapter.getId(viewHolder.getBindingAdapterPosition()));
                    bundle.putString("barcode", inventoryAdapter.getBarcode(viewHolder.getBindingAdapterPosition()));
                    bundle.putString("name", inventoryAdapter.getItemName(viewHolder.getBindingAdapterPosition()));
                    bundle.putString("price", inventoryAdapter.getPrice(viewHolder.getBindingAdapterPosition()));
                    bundle.putString("frag", "manual");
                    bundle.putString("email", email);
                    recentPosition = viewHolder.getBindingAdapterPosition();
                    recentlyRemoved = inventoryAdapter.recentRemove(viewHolder.getBindingAdapterPosition());
                    EditDialog editDialog = new EditDialog();
                    editDialog.setCancelable(false);
                    editDialog.setArguments(bundle);
                    editDialog.setTargetFragment(ManualInventoryFragment.this, 1);
                    editDialog.show(getParentFragmentManager(), "EditDialog");
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.windowBlue))
                        .addSwipeLeftActionIcon(R.drawable.delete)
                        .addSwipeLeftLabel(getResources().getString(R.string.delete))
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.windowBlue))
                        .addSwipeRightActionIcon(R.drawable.edit)
                        .addSwipeRightLabel("EDIT")
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(itemList);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                itemList.getContext(),
                layoutManager.getOrientation()
        );

        dividerItemDecoration.setDrawable(
                Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.divider))
        );
        itemList.addItemDecoration(dividerItemDecoration);


        return rootView;
    }

    @Override
    public void sendDeleteConfirmation(boolean send) {
        if (send) {
            inventoryAdapter.undoRecent(recentPosition, recentlyRemoved);
        }
    }

    @Override
    public void sendEditConfirmation(boolean send) {
        if (send) {
            inventoryAdapter.undoRecent(recentPosition, recentlyRemoved);
        }
    }
}