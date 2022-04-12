package com.shashank.spendistrybusiness.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Activities.DashboardActivity;
import com.shashank.spendistrybusiness.Activities.MainActivity;
import com.shashank.spendistrybusiness.Adapters.InvoiceAdapter;
import com.shashank.spendistrybusiness.Adapters.SearchableSpinnerAdapter;
import com.shashank.spendistrybusiness.Constants.Global;
import com.shashank.spendistrybusiness.Models.CreateInvoice.BusinessInvoices;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

@SuppressWarnings("ALL")
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
    private String businessEmail, businessName, description, city, gstin, address, contact, email,invoiceId, reportId;
    private int invoiceNumber;
    private LinearLayoutManager layoutManager;
    private TextView searchItem;
    private LinearLayout linearLayout;

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
        linearLayout = rootView.findViewById(R.id.manual_invoice);
        itemQuantity.setText("1");
        Button addItem = rootView.findViewById(R.id.add_btn_invoice);
        Bundle bundle = getArguments();
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        if (bundle != null) {
            email = bundle.getString("SCAN_RESULT");
        }

        if (sharedPreferences.getBoolean("report", false)) {
            send.setText("Change Data");
            cancel.setText("Reject");
            cancel.setVisibility(View.GONE);
            if (bundle != null) {
                invoiceId = bundle.getString("invoiceId");
                reportId = bundle.getString("reportId");
            }
            invoiceViewModel.getReportedInvoice(email, businessEmail, invoiceId).observe(requireActivity(), itemPrices -> invoiceViewModel.setInvoiceList(itemPrices));
        } else {
            send.setText("Send");
            cancel.setText("Cancel");
        }
        invoiceViewModel.getInvoice().observe(requireActivity(), itemPrices -> {
            invoiceAdapter = new InvoiceAdapter(itemPrices, requireContext(), requireActivity());
            invoiceList.setLayoutManager(new LinearLayoutManager(requireContext()));
            invoiceList.setLayoutManager(layoutManager);
            invoiceList.setAdapter(invoiceAdapter);
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
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.windowBlue))
                        .addSwipeLeftActionIcon(R.drawable.delete)
                        .addSwipeLeftLabel("DELETE")
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.windowBlue))
                        .addSwipeRightActionIcon(R.drawable.edit)
                        .addSwipeRightLabel("EDIT")
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(invoiceList);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                invoiceList.getContext(),
                layoutManager.getOrientation()
        );

        dividerItemDecoration.setDrawable(
                Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.divider))
        );
        invoiceList.addItemDecoration(dividerItemDecoration);


        addItem.setOnClickListener(view -> {
            if (!itemPrice.getText().toString().equals("") && !itemName.getText().toString().equals("") && !itemQuantity.getText().toString().equals("")) {
                ItemPrices itemPrices = new ItemPrices("", itemName.getText().toString(), Integer.parseInt(itemQuantity.getText().toString()), itemPrice.getText().toString());
                invoiceViewModel.setInvoice(itemPrices);
                itemName.setText("");
                itemPrice.setText("");
            }
        });


        send.setOnClickListener(view -> {

                if (invoiceAdapter != null) {
                    if (invoiceAdapter.getItemCount() > 0) {
                        Dialog dialog = new Dialog(requireContext());
                        dialog.setContentView(R.layout.final_invoice_dialog);
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);
                        setDialog(dialog, send);
                    } else {
                        Snackbar snackbar = Snackbar.make(linearLayout, "Add items first", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(linearLayout, "Add items first", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                    snackbar.show();

                }

        });


        cancel.setOnClickListener(view -> {
            if (cancel.getText().toString().equalsIgnoreCase("cancel")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Cancel Invoice");
                builder.setMessage("Are you sure you want to cancel this invoice?");
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();
            }
        });
        return rootView;
    }

    @SuppressWarnings("deprecation")
    private void setDialog(Dialog dialog, Button send_btn) {
        EditText discount = dialog.findViewById(R.id.invoice_discount_input);
        EditText cgst = dialog.findViewById(R.id.invoice_cgst_input);
        EditText sgst = dialog.findViewById(R.id.invoice_sgst_input);
        EditText igst = dialog.findViewById(R.id.invoice_igst_input);
        EditText utgst = dialog.findViewById(R.id.invoice_utgst_input);
        Spinner spinner = dialog.findViewById(R.id.invoice_payment_method);
        Button send = dialog.findViewById(R.id.invoice_send_button);
        dialog.show();
        send.setOnClickListener(view -> {
            if (discount.getText().toString().isEmpty()) {
                discount.setError("Enter discount");
                new Handler().postDelayed(() -> discount.setError(null), 1500);
            } else if (cgst.getText().toString().isEmpty()) {
                cgst.setError("Enter CGST");
                new Handler().postDelayed(() -> cgst.setError(null), 1500);
            } else if (sgst.getText().toString().isEmpty()) {
                sgst.setError("Enter SGST");
                new Handler().postDelayed(() -> sgst.setError(null), 1500);
            } else if (igst.getText().toString().isEmpty()) {
                igst.setError("Enter IGST");
                new Handler().postDelayed(() -> igst.setError(null), 1500);
            } else if (utgst.getText().toString().isEmpty()) {
                utgst.setError("Enter UTGST");
                new Handler().postDelayed(() -> utgst.setError(null), 1500);
            } else {
                String discount_value = discount.getText().toString();
                String cgst_value = cgst.getText().toString();
                String sgst_value = sgst.getText().toString();
                String igst_value = igst.getText().toString();
                String utgst_value = utgst.getText().toString();
                String payment_method = spinner.getSelectedItem().toString();
                double total = 0;
                for (int i = 0; i < invoiceAdapter.getItemCount(); i++) {
                    total += Double.parseDouble(invoiceAdapter.getTotal(i));
                }
                double discount_amount = Double.parseDouble(discount_value);
                double cgst_amount = Double.parseDouble(cgst_value);
                double sgst_amount = Double.parseDouble(sgst_value);
                double igst_amount = Double.parseDouble(igst_value);
                double utgst_amount = Double.parseDouble(utgst_value);

                double discount_total = total - (total * (discount_amount / 100.0));
                double cgst_total = (discount_total * (cgst_amount / 100.0));
                double sgst_total = (discount_total * (sgst_amount / 100.0));
                double igst_total = (discount_total * (igst_amount / 100.0));
                double utgst_total = (discount_total * (utgst_amount / 100.0));
                double final_total = discount_total + utgst_total + cgst_total + sgst_total + igst_total;

                if (send_btn.getText().toString().equalsIgnoreCase("send")) {
                    Invoice invoice = new Invoice(invoiceAdapter.getItemPricesList(), invoiceNumber, total, businessName, description
                            , Double.parseDouble(discount_value), final_total, igst_amount, cgst_amount, sgst_amount, utgst_amount, email,
                            businessEmail, payment_method, Math.round(final_total), city, address, gstin, contact);
                    ArrayList<Invoice> invoiceList = new ArrayList<>();
                    invoiceList.add(invoice);
                    BusinessInvoices businessArray = new BusinessInvoices(businessEmail, invoiceList);
                    if (invoice.getTotalItems().size() > 0) {
                        invoiceViewModel.addInvoice(email, businessEmail, businessArray);
                        invoiceViewModel.setInvoice(new ItemPrices("", null, null, null, null));
                        invoiceViewModel.updateInvoiceNumber(businessEmail, invoiceNumber + 1).observe(requireActivity(), s -> {
                            invoiceNumber = s;
                            dialog.dismiss();
                            startActivity(new Intent(requireActivity(), DashboardActivity.class));
                            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        });
                    } else {
                        Snackbar snackbar = Snackbar.make(linearLayout, "Invoice has no Data", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(getResources().getColor(R.color.red));
                        snackbar.show();
                        dialog.dismiss();
                    }
                } else {
                    Invoice invoice = new Invoice(invoiceAdapter.getItemPricesList(), total, discount_amount, igst_amount, cgst_amount,sgst_amount,utgst_amount,payment_method, final_total,Math.round(final_total));
                    invoiceViewModel.updateInvoice(email,businessEmail,invoiceId,reportId, invoice);
                    Global.itemPricesArrayList.clear();
                    dialog.dismiss();
                    startActivity(new Intent(requireActivity(), DashboardActivity.class));
                    requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }
        });
    }

    private void SearchItemDialog() {
        searchItem.setOnClickListener(view -> {
            dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.searchable_spinner);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            EditText editText = dialog.findViewById(R.id.search_box);
            listView = dialog.findViewById(R.id.list_view);
            invoiceViewModel.getItemPrices(businessEmail).observe(requireActivity(), itemPrices -> {
                adapter = new SearchableSpinnerAdapter(requireContext(), (ArrayList<ItemPrices>) itemPrices);
                itemPricesListFilter = itemPrices;
                itemPricesListTemp = itemPrices;
                listView.setAdapter(adapter);
            });

            dialog.setOnCancelListener(dialogInterface -> adapter = new SearchableSpinnerAdapter(requireContext(), (ArrayList<ItemPrices>) itemPricesListTemp));

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


            listView.setOnItemClickListener((parent, view1, position, id) -> {
                ItemPrices itemPrices = (ItemPrices) parent.getAdapter().getItem(position);
                Dialog dialog1 = new Dialog(requireContext());
                dialog1.setContentView(R.layout.quantity_dialog);
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
                cancel.setOnClickListener(view11 -> dialog1.dismiss());
                add.setOnClickListener(view112 -> {
                    adapter = new SearchableSpinnerAdapter(requireContext(), (ArrayList<ItemPrices>) itemPricesListTemp);
                    ItemPrices itemPrices1 = new ItemPrices(itemPrices.getBarcode(), itemPrices.getItemName(), Integer.parseInt(itemQuantity.getText().toString()), itemPrice.getText().toString());
                    invoiceViewModel.setInvoice(itemPrices1);
                    dialog1.dismiss();
                });
                dialog.dismiss();
            });
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sharedPreferences = requireContext().getSharedPreferences("loggedIn", MODE_PRIVATE);
        businessEmail = sharedPreferences.getString("email", "");
        invoiceViewModel = new ViewModelProvider(this,(ViewModelProvider.Factory) new ViewModelFactory(requireActivity().getApplication(), email)).get(InvoiceViewModel.class);
        invoiceViewModel.setInventory(businessEmail, new ArrayList<>());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        invoiceViewModel.getDashBoardFromDB(businessEmail).observe(getViewLifecycleOwner(), dashboard -> {
            businessName = dashboard.getVendorDetails().getBusinessName();
            city = dashboard.getVendorDetails().getCity();
            description = dashboard.getVendorDetails().getDescription();
            gstin = dashboard.getVendorDetails().getGstNumber();
            address = dashboard.getVendorDetails().getAddress();
            contact = dashboard.getVendorDetails().getMobileNumber();
            invoiceNumber = dashboard.getVendorDetails().getCurrentInvoiceNumber();
        });

    }

    public void deleteDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
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
        remove.setOnClickListener(v -> dialog.dismiss());
        cancel.setOnClickListener(v -> {
            invoiceAdapter.undoRecent(recentPosition, recentlyRemoved);
            dialog.dismiss();
        });
    }

    public void editDialog() {

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.edit_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
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
        edit.setOnClickListener(v -> {
            String newItemName = itemName.getText().toString();
            String newItemPrice = itemPrice.getText().toString();
            int newItemQuantity = Integer.parseInt(itemQuantity.getText().toString());
            if (newItemName.isEmpty() && newItemPrice.isEmpty() && itemQuantity.getText().toString().isEmpty()) {
                Snackbar snackbar = Snackbar.make(linearLayout, "Please fill all the fields", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red));
                snackbar.show();
            } else {
                invoiceAdapter.updateItem(recentPosition, new ItemPrices(recentlyRemoved.getBarcode(), newItemName, newItemQuantity, newItemPrice));
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(v -> {
            invoiceAdapter.undoRecent(recentPosition, recentlyRemoved);
            dialog.dismiss();
        });
    }
}