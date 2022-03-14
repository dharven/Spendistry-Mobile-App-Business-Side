package com.shashank.spendistrybusiness.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.spendistrybusiness.Activities.MainActivity;
import com.shashank.spendistrybusiness.Adapters.InvoiceAdapter;
import com.shashank.spendistrybusiness.Adapters.SearchableSpinnerAdapter;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessArray;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class ManualInvoiceFragment extends Fragment {

    private Dialog dialog;
    private SearchableSpinnerAdapter adapter;
    private RecyclerView invoiceList;
    private List<ItemPrices> itemPricesListFilter;
    private List<ItemPrices> itemPricesListTemp;
    private ListView listView;
    private InvoiceViewModel invoiceViewModel;
    private InvoiceAdapter invoiceAdapter;
    private SharedPreferences sharedPreferences;
    private ItemPrices recentlyRemoved;
    private int recentPosition;
    private String businessEmail;
    private LinearLayoutManager layoutManager;
    private TextView searchItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manual_invoice, container, false);
        searchItem = rootView.findViewById(R.id.dropDown);
        invoiceList = rootView.findViewById(R.id.invoiceList);
        Button cancel = rootView.findViewById(R.id.cancel_invoice);
        Button send = rootView.findViewById(R.id.send_invoice);
        EditText itemName = rootView.findViewById(R.id.itemName_invoice);
        EditText itemPrice = rootView.findViewById(R.id.itemPrice_invoice);
        EditText itemQuantity = rootView.findViewById(R.id.quantity_invoice);
        itemQuantity.setText("1");
        Button addItem = rootView.findViewById(R.id.add_btn_invoice);
        sharedPreferences = requireContext().getSharedPreferences("loggedIn", MODE_PRIVATE);
        Bundle bundle = getArguments();
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        String email = bundle.getString("SCAN_RESULT");
//        sharedPreferences.edit().putBoolean("hasData", true).apply();
        invoiceViewModel.getInvoice().observe(requireActivity(), new Observer<List<ItemPrices>>() {

            @Override
            public void onChanged(List<ItemPrices> itemPrices) {


                invoiceAdapter = new InvoiceAdapter(itemPrices, requireContext(), requireActivity());
                invoiceList.setLayoutManager(new LinearLayoutManager(requireContext()));
                invoiceList.setLayoutManager(layoutManager);
                invoiceList.setAdapter(invoiceAdapter);
            }
        });

        SearchItemDialog();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    recentPosition = viewHolder.getBindingAdapterPosition();
                    recentlyRemoved = invoiceAdapter.recentRemove(viewHolder.getBindingAdapterPosition());
                    deleteDialog();

                } else if (direction == ItemTouchHelper.RIGHT) {
                    recentPosition = viewHolder.getBindingAdapterPosition();
                    recentlyRemoved = invoiceAdapter.recentRemove(viewHolder.getBindingAdapterPosition());
                    editDialog();

                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal_200))
                        .addSwipeLeftActionIcon(R.drawable.delete)
                        .addSwipeLeftLabel("DELETE")
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .addSwipeRightActionIcon(R.drawable.edit)
                        .addSwipeRightLabel("EDIT")
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(invoiceList);


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!itemPrice.getText().toString().equals("") && !itemName.getText().toString().equals("") && !itemQuantity.getText().toString().equals("")) {
                    ItemPrices itemPrices = new ItemPrices( "", itemName.getText().toString(), Integer.parseInt(itemQuantity.getText().toString()),itemPrice.getText().toString());
                    invoiceViewModel.setInvoice(itemPrices);
                    itemName.setText("");
                    itemPrice.setText("");
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double total = 0;
                for (int i = 0; i < invoiceAdapter.getItemCount(); i++) {
                    total += Double.parseDouble(invoiceAdapter.getTotal(i));
                }

                double finalPrice = total - (total * (10.0 / 100.0));

                Toast.makeText(requireContext(), "total: " + total + "\n final: " + finalPrice, Toast.LENGTH_SHORT).show();
                Invoice invoice = new Invoice(invoiceAdapter.getItemPricesList(), "1", total, "amul anand",
                        "lorem impsum", 10, finalPrice, 0.0, 0.0, 0.0, 0.0, email,
                        businessEmail, "cash", Math.round(finalPrice), "anand");
                ArrayList<Invoice> invoiceList = new ArrayList<>();
                invoiceList.add(invoice);
                BusinessArray businessArray = new BusinessArray(businessEmail, invoiceList);
                invoiceViewModel.addInvoice(email, businessEmail, businessArray);
                invoiceViewModel.setInvoice(new ItemPrices("", null, null, null));
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Cancel Invoice");
                builder.setMessage("Are you sure you want to cancel this invoice?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        return rootView;
    }

    private void SearchItemDialog() {
        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.searchable_spinner);
                dialog.getWindow().setLayout(800, 1400);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText editText = dialog.findViewById(R.id.search_box);
                listView = dialog.findViewById(R.id.list_view);
//                Button close = dialog.findViewById(R.id.choose_close);

//                close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        adapter = new SearchableSpinnerAdapter(requireContext(), (ArrayList<ItemPrices>) itemPricesListTemp);
//                        dialog.dismiss();
//                    }
//                });

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        adapter = new SearchableSpinnerAdapter(requireContext(), (ArrayList<ItemPrices>) itemPricesListTemp);
                    }
                });
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ArrayList<ItemPrices> filteredList = new ArrayList<>();
                        for (int i = 0; i < itemPricesListFilter.size(); i++) {
                            if (itemPricesListFilter.get(i).getItemName().toLowerCase().contains(s.toString().toLowerCase())) {
                                filteredList.add(itemPricesListFilter.get(i));
                            }
                        }
                        adapter = new SearchableSpinnerAdapter(requireContext(), filteredList);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ItemPrices itemPrices = (ItemPrices) parent.getAdapter().getItem(position);
                        Dialog dialog1 = new Dialog(requireContext());
                        dialog1.setContentView(R.layout.quantity_dialog);
                        dialog1.getWindow().setLayout(800, 1400);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.show();
                        final TextView itemName = dialog1.findViewById(R.id.item_name_quantity);
                        final EditText itemPrice = dialog1.findViewById(R.id.price_quantity);
                        EditText itemQuantity = dialog1.findViewById(R.id.quantity);
                        final Button add = dialog1.findViewById(R.id.add_quantity);
                        final Button cancel = dialog1.findViewById(R.id.cancel_quantity);
                        itemName.setText(itemPrices.getItemName());
                        itemPrice.setText(itemPrices.getPrice());
                        itemQuantity.setText("1");
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapter = new SearchableSpinnerAdapter(requireContext(), (ArrayList<ItemPrices>) itemPricesListTemp);
                                ItemPrices itemPrices1 = new ItemPrices(itemPrices.getBarcode(),itemPrices.getItemName(),Integer.parseInt(itemQuantity.getText().toString()), itemPrice.getText().toString() );
                                invoiceViewModel.setInvoice(itemPrices1);
                                dialog1.dismiss();
                            }
                        });
                        dialog.dismiss();
                    }
                });
            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        businessEmail = sharedPreferences.getString("email", "");
        invoiceViewModel.getItemPrices(businessEmail).observe(requireActivity(), new Observer<List<ItemPrices>>() {
            @Override
            public void onChanged(List<ItemPrices> itemPrices) {

                adapter = new SearchableSpinnerAdapter(requireContext(), (ArrayList<ItemPrices>) itemPrices);
                itemPricesListFilter = itemPrices;
                itemPricesListTemp = itemPrices;
            }
        });

    }

    public void deleteDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setLayout(800, 1400);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Button remove = dialog.findViewById(R.id.delete_btn);
        Button cancel = dialog.findViewById(R.id.cancel_delete_btn);
        TextView barcode = dialog.findViewById(R.id.barcode_delete);
        TextView itemName = dialog.findViewById(R.id.item_name_delete);
        TextView itemPrice = dialog.findViewById(R.id.item_price_delete);
        remove.setText("Remove");
        cancel.setText("Cancel");
        barcode.setText("Barcode: " + recentlyRemoved.getBarcode());
        itemName.setText("Item Name: " + recentlyRemoved.getItemName());
        itemPrice.setText("Item Price: " + recentlyRemoved.getPrice());
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceAdapter.undoRecent(recentPosition, recentlyRemoved);
                dialog.dismiss();
            }
        });
    }

    public void editDialog() {

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.edit_dialog);
        dialog.getWindow().setLayout(800, 1400);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        Button edit = dialog.findViewById(R.id.update_btn);
        Button cancel = dialog.findViewById(R.id.cancel_btn);
        final TextView barcode = dialog.findViewById(R.id.barcode_edit);
        final EditText itemName = dialog.findViewById(R.id.item_name_edit);
        final EditText itemPrice = dialog.findViewById(R.id.price_edit);
        final EditText itemQuantity = dialog.findViewById(R.id.edit_quantity);
        itemQuantity.setVisibility(View.VISIBLE);
        edit.setText("Edit");
        cancel.setText("Cancel");
        barcode.setText("Barcode: " + recentlyRemoved.getBarcode());
        itemName.setText(recentlyRemoved.getItemName());
        itemPrice.setText(recentlyRemoved.getPrice());
        itemQuantity.setText(String.valueOf(recentlyRemoved.getQuantity()));
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItemName = itemName.getText().toString();
                String newItemPrice = itemPrice.getText().toString();
                int newItemQuantity = Integer.parseInt(itemQuantity.getText().toString());
                if (newItemName.isEmpty() && newItemPrice.isEmpty() && itemQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    invoiceAdapter.updateItem(recentPosition,new ItemPrices(recentlyRemoved.getBarcode(),newItemName, newItemQuantity, newItemPrice));
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceAdapter.undoRecent(recentPosition, recentlyRemoved);
                dialog.dismiss();
            }
        });
    }
}