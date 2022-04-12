package com.shashank.spendistrybusiness.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.spendistrybusiness.Activities.CreateInvoiceActivity;
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Models.Report;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.ReportViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressLint("ALL")
public class ReportedInvoiceAdapter extends RecyclerView.Adapter<ReportedInvoiceAdapter.ViewHolder> {

    private final List<Report> reportInvoiceList;
    private final Activity activity;
    private final SharedPreferences sharedPreferences;
    private final ReportViewModel reportViewModel;

    public ReportedInvoiceAdapter(List<Report> reportInvoiceList, Context context, Activity activity, ReportViewModel reportViewModel) {
        this.reportInvoiceList = reportInvoiceList;
        this.activity = activity;
        sharedPreferences = context.getSharedPreferences("loggedIn", Context.MODE_PRIVATE);
        this.reportViewModel = reportViewModel;
    }

    @NonNull
    @Override
    public ReportedInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.reported_invoices, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReportedInvoiceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //underline email
        holder.email.setPaintFlags(holder.email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.email.setText("Email: " + reportInvoiceList.get(position).getClientEmail());
        Date time = new Date(Long.parseLong(reportInvoiceList.get(position).getDate()));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'Time: ' hh:mm:ss aaa");
        String date = sdf.format(time);
        holder.date.setText("Date: " + date);
        holder.reason.setText("Reason: " + reportInvoiceList.get(position).getReason());

        holder.itemView.setOnClickListener(view -> {
            sharedPreferences.edit().putBoolean("report", true).apply();
            Intent intent = new Intent(activity, CreateInvoiceActivity.class);
            intent.putExtra("invoiceId", reportInvoiceList.get(position).getInvoiceID());
            intent.putExtra("SCAN_RESULT", reportInvoiceList.get(position).getClientEmail());
            intent.putExtra("reportId", reportInvoiceList.get(position).getId());
            activity.startActivity(intent);
            activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        holder.email.setOnClickListener(view -> {
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.user_profile);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView name = dialog.findViewById(R.id.user_profile_name);
            TextView email = dialog.findViewById(R.id.user_profile_email);
            TextView phone = dialog.findViewById(R.id.user_profile_contact);
            //underline email and phone textView
            email.setPaintFlags(email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            phone.setPaintFlags(phone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            //
            RoundedImageView image = dialog.findViewById(R.id.user_profile_image);
            dialog.show();

            email.setOnClickListener(view12 -> {
                //open email
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{reportInvoiceList.get(position).getClientEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Reported Invoice");
                activity.startActivity(Intent.createChooser(intent, "Send Email"));
            });

            phone.setOnClickListener(view1 -> {
                //open phone
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + reportInvoiceList.get(position).getClientPhone()));
                activity.startActivity(intent);
            });

            reportViewModel.getUser(reportInvoiceList.get(position).getClientEmail()).observe((LifecycleOwner) activity, user -> {
                name.setText("Name: " + user.getFname() + " " + user.getLname());
                email.setText("Email: " + user.get_id());
                phone.setText("Mobile: " + user.getMobileNumber());
                Glide.with(activity).load(Constants.URL_API + "userProfile/" + user.get_id() + ".jpeg")
                        .placeholder(R.drawable.loading).error(R.drawable.no_profile).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions.skipMemoryCacheOf(true)).into(image);
            });

        });

        holder.itemView.setOnLongClickListener(view -> {
            Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.reported_invoice_layout), "Swipe left to take action", Snackbar.LENGTH_SHORT);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(activity.getResources().getColor(R.color.mainBlue));
            snackbar.show();
            return true;
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

    public Report recentRemove(int position) {
        Report report = reportInvoiceList.get(position);
        reportInvoiceList.remove(position);
        notifyItemRemoved(position);
        return report;
    }

    public void undoRecent(int position, Report report) {
        reportInvoiceList.add(position, report);
        notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView email;
        TextView reason;
        TextView date;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.client_email);
            reason = itemView.findViewById(R.id.client_reason);
            date = itemView.findViewById(R.id.generated_date);
        }
    }

}
