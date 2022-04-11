package com.shashank.spendistrybusiness.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.DialogFragment.DeleteDialog;
import com.shashank.spendistrybusiness.DialogFragment.EditDialog;
import com.shashank.spendistrybusiness.Fragments.ManualInventoryFragment;
import com.shashank.spendistrybusiness.Models.CreateInvoice.Invoice;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.ResponseBody;

public class BusinessInvoiceAdapter extends RecyclerView.Adapter<BusinessInvoiceAdapter.MyViewHolder> {
    private List<Invoice> invoiceList;
    private Context context;
    private Activity activity;
    private InvoiceViewModel invoiceViewModel;
    private LinearLayout linearLayout;
    private LifecycleOwner lifecycleOwner;

    public BusinessInvoiceAdapter(List<Invoice> itemPricesList, Context context, Activity activity, LifecycleOwner lifecycleOwner, LinearLayout layout, InvoiceViewModel invoiceViewModel) {
        itemPricesList.sort(Comparator.comparingInt(Invoice::getInvoiceNumber).reversed());
        //convert to data
        for (Invoice invoice : itemPricesList) {
            Date time = new Date(Long.parseLong(invoice.getInvoiceTime()));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'Time: ' hh:mm:ss aaa", java.util.Locale.getDefault());
            String date = sdf.format(time);
            invoice.setDate(date);
        }
        this.invoiceList = itemPricesList;
        this.context = context;
        this.activity = activity;
        this.linearLayout = layout;
        this.lifecycleOwner = lifecycleOwner;
        this.invoiceViewModel = invoiceViewModel;
    }

    @NonNull
    @Override
    public BusinessInvoiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.invoice_layout, parent, false);
        return new BusinessInvoiceAdapter.MyViewHolder(view);
    }


    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull BusinessInvoiceAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Invoice invoice = invoiceList.get(position);
//            //setText
        String name = invoice.getTitle();
        //capitalize first letter
        if (name != null) {
            String firstLetter = name.substring(0, 1);
            String restOfWord = name.substring(1);

            String capitalizedFirstLetter = firstLetter.toUpperCase();
            String capitalizedRestOfWord = restOfWord.toLowerCase();
            holder.businessName.setText(capitalizedFirstLetter + capitalizedRestOfWord);
        }
        holder.businessEmail.setText(invoiceList.get(position).getSentBy());

        holder.businessPhone.setText(invoiceList.get(position).getBusinessContactNo());
        holder.customerEmail.setText(invoiceList.get(position).getSentTo());
        holder.invoiceTotal.setText("Total: ₹" + invoiceList.get(position).getTotal());
        holder.invoiceDiscount.setText("Discount: " + invoiceList.get(position).getDiscount() + "%");
        holder.invoiceIGST.setText("IGST: " + invoiceList.get(position).getIGST() + "%");
        holder.invoiceCGST.setText("CGST:" + invoiceList.get(position).getCGST() + "%");
        holder.invoiceSGST.setText("SGST: " + invoiceList.get(position).getSGST() + "%");
        holder.invoiceUTGST.setText("UTGST: " + invoiceList.get(position).getUTGST() + "%");
        holder.roundOff.setText("Net total: ₹" + invoiceList.get(position).getRoundOff() + "");
        holder.paymentMode.setText("Payment Method: " + invoiceList.get(position).getPaymentMode());
        holder.description.setText(invoiceList.get(position).getDescription());
        //string to date
//        Instant instant = Instant.ofEpochSecond( Long.parseLong(invoiceList.get(position).getInvoiceTime()));
        holder.invoiceTime.setText("Date:  " + invoiceList.get(position).getDate());
        holder.invoiceAddress.setText("BusinessAddress: " + invoiceList.get(position).getBusinessAddress());
        holder.notice.setText("SUBJECT TO " + invoiceList.get(position).getCity().toUpperCase() + " JURISDICTION ONLY");
        holder.gstNo.setText("GST No.: " + invoiceList.get(position).getGstNumber());
        holder.invoiceNo.setText("Invoice No.: " + invoiceList.get(position).getInvoiceNumber());
        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(invoiceList.get(position).getTotalItems(), context, activity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.subRecyclerView.setLayoutManager(layoutManager);
        holder.subRecyclerView.setAdapter(invoiceAdapter);

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                invoiceViewModel.getPDF(invoice.getSentTo(), invoice.getSentBy(), invoice.getInvoiceId()).observe(lifecycleOwner, new Observer<ResponseBody>() {
                    @Override
                    public void onChanged(ResponseBody responseBody) {
                        try {
                            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    "SpendistryBusiness");
                            if (!f.exists()) {
                                f.mkdirs();
                            }
                            File f1 = new File(f,  invoice.getTitle());
                            if (!f1.exists()) {
                                f1.mkdirs();
                            }
                                File file = new File(f1, invoice.getTitle() + "_"+invoice.getRoundOff()+"_"
                                        + Calendar.getInstance().getTime().getDate()
                                        +Calendar.getInstance().getTime().getHours()
                                        +Calendar.getInstance().getTime().getMinutes()
                                        +Calendar.getInstance().getTime().getSeconds()
                                        + ".pdf");
                                //write response to file
                                if (!file.exists()) {
                                    InputStream inputStream = responseBody.byteStream();
                                    FileOutputStream stream = new FileOutputStream(file);
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = inputStream.read(buffer)) != -1) {
                                        stream.write(buffer, 0, len);
                                    }
                                    Snackbar snackbar = Snackbar.make(linearLayout, "Invoice saved in DOWNLOADS", Snackbar.LENGTH_SHORT);
                                    snackbar.setTextColor(Color.WHITE);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.mainBlue));
                                    snackbar.show();
                                    stream.close();
                                    inputStream.close();
                                } else {
                                    Snackbar snackbar = Snackbar.make(linearLayout, "Already Downloaded", Snackbar.LENGTH_SHORT);
                                    snackbar.setTextColor(Color.WHITE);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.mainBlue));
                                    snackbar.show();
                                }

                        } catch (Exception e) {
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        holder.subRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        //on click
        holder.bind(invoice);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean expanded = invoice.isExpanded();
                invoice.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });


    }


    public boolean getExpanded(int position) {
        return invoiceList.get(position).isExpanded();
    }


    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public List<Invoice> getList(){
        return invoiceList;
    }

    public void searchQuery(String query, List<Invoice> pricesList) {
        List<Invoice> newList = new ArrayList<>();
        for (Invoice invoice : pricesList) {
            if (invoice.getTitle().toLowerCase().contains(query.toLowerCase())) {
                newList.add(invoice);
            } else if (invoice.getDate().toLowerCase().contains(query.toLowerCase())) {
                newList.add(invoice);
            }
        }
        this.invoiceList = newList;
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView businessName, invoiceTime, invoiceAddress, notice, gstNo, invoiceNo, businessEmail, businessPhone, customerEmail, invoiceTotal, invoiceDiscount, invoiceIGST, invoiceCGST, invoiceSGST, invoiceUTGST, roundOff, paymentMode, description;
        RecyclerView subRecyclerView;
        LinearLayout linearLayout;
        ImageButton download;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            businessName = itemView.findViewById(R.id.business_name_invoice1);
            invoiceTime = itemView.findViewById(R.id.invoice_time);
            invoiceAddress = itemView.findViewById(R.id.business_address);
            notice = itemView.findViewById(R.id.notice);
            gstNo = itemView.findViewById(R.id.gst_no);
            invoiceNo = itemView.findViewById(R.id.invoice_no);
            businessEmail = itemView.findViewById(R.id.business_email_invoice);
            businessPhone = itemView.findViewById(R.id.business_phone);
            customerEmail = itemView.findViewById(R.id.customer_email);
            invoiceTotal = itemView.findViewById(R.id.invoice_total);
            invoiceDiscount = itemView.findViewById(R.id.invoice_discount);
            invoiceIGST = itemView.findViewById(R.id.invoice_igst);
            invoiceCGST = itemView.findViewById(R.id.invoice_cgst);
            invoiceSGST = itemView.findViewById(R.id.invoice_sgst);
            invoiceUTGST = itemView.findViewById(R.id.invoice_utgst);
            roundOff = itemView.findViewById(R.id.round_off);
            paymentMode = itemView.findViewById(R.id.payment_mode);
            description = itemView.findViewById(R.id.description);
            subRecyclerView = itemView.findViewById(R.id.list);
            linearLayout = itemView.findViewById(R.id.title_layout);
            download = itemView.findViewById(R.id.download_btn);
        }

        @SuppressLint("SetTextI18n")
        private void bind(Invoice invoice) {

            boolean expandable = invoice.isExpanded();

            if (!expandable) {
                paymentMode.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                businessEmail.setVisibility(View.GONE);
                subRecyclerView.setVisibility(View.GONE);
                invoiceAddress.setVisibility(View.GONE);
                notice.setVisibility(View.GONE);
                gstNo.setVisibility(View.GONE);
                businessPhone.setVisibility(View.GONE);
                customerEmail.setVisibility(View.GONE);
                invoiceTotal.setVisibility(View.GONE);
                invoiceDiscount.setVisibility(View.GONE);
                invoiceIGST.setVisibility(View.GONE);
                invoiceCGST.setVisibility(View.GONE);
                invoiceSGST.setVisibility(View.GONE);
                invoiceUTGST.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
            } else {
                paymentMode.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                businessEmail.setVisibility(View.VISIBLE);
                subRecyclerView.setVisibility(View.VISIBLE);
                invoiceAddress.setVisibility(View.VISIBLE);
                notice.setVisibility(View.VISIBLE);
                gstNo.setVisibility(View.VISIBLE);
                businessPhone.setVisibility(View.VISIBLE);
                customerEmail.setVisibility(View.VISIBLE);
                invoiceTotal.setVisibility(View.VISIBLE);
                invoiceDiscount.setVisibility(View.VISIBLE);
                invoiceIGST.setVisibility(View.VISIBLE);
                invoiceCGST.setVisibility(View.VISIBLE);
                invoiceSGST.setVisibility(View.VISIBLE);
                invoiceUTGST.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }

        }

    }


}
