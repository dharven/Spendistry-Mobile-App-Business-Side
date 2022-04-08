package com.shashank.spendistrybusiness.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shashank.spendistrybusiness.Activities.CreateInvoiceActivity;
import com.shashank.spendistrybusiness.Models.ItemPrices;
import com.shashank.spendistrybusiness.Models.Report;
import com.shashank.spendistrybusiness.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportedInvoiceAdapter extends RecyclerView.Adapter<ReportedInvoiceAdapter.ViewHolder> {

    private List<Report> reportInvoiceList;
    private Context context;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    public ReportedInvoiceAdapter(List<Report> reportInvoiceList, Context context, Activity activity) {
        this.reportInvoiceList = reportInvoiceList;
        this.context = context;
        this.activity = activity;
        sharedPreferences = context.getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ReportedInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.reported_invoices, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReportedInvoiceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText("Client: "+reportInvoiceList.get(position).getClientName());
        holder.email.setText("Email: "+reportInvoiceList.get(position).getClientEmail());
        holder.contact.setText("Phone: "+reportInvoiceList.get(position).getClientPhone());
        Date time = new Date(Long.parseLong(reportInvoiceList.get(position).getDate()));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'Time: ' hh:mm:ss aaa");
        String date = sdf.format(time);
        holder.date.setText("Date: "+date);
        holder.reason.setText("Reason: "+reportInvoiceList.get(position).getReason());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean("report",true).apply();
                Intent intent = new Intent(activity, CreateInvoiceActivity.class);
                intent.putExtra("invoiceId",reportInvoiceList.get(position).getInvoiceID());
                intent.putExtra("SCAN_RESULT",reportInvoiceList.get(position).getClientEmail());
                intent.putExtra("reportId",reportInvoiceList.get(position).getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return reportInvoiceList.size();
    }

    public Report recentRemove(int position){
        Report report = reportInvoiceList.get(position);
        reportInvoiceList.remove(position);
        notifyItemRemoved(position);
        return report;
    }

    public void undoRecent(int position, Report report){
        reportInvoiceList.add(position, report);
        notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView contact;
        TextView reason;
        TextView date;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.client_name);
            email = itemView.findViewById(R.id.client_email);
            contact = itemView.findViewById(R.id.client_phone);
            reason = itemView.findViewById(R.id.client_reason);
            date = itemView.findViewById(R.id.generated_date);
        }
    }

}
