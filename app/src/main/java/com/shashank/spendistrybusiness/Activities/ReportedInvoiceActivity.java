package com.shashank.spendistrybusiness.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shashank.spendistrybusiness.Adapters.ReportedInvoiceAdapter;
import com.shashank.spendistrybusiness.Models.Report;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistrybusiness.ViewModels.ReportViewModel;

import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

@SuppressWarnings("ALL")
public class ReportedInvoiceActivity extends AppCompatActivity {

    private ReportViewModel reportViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int recentPosition;
    private ReportedInvoiceAdapter reportedInvoiceAdapter;
    private RecyclerView recyclerView;
    private Dialog dialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_invoice);
        setSupportActionBar(findViewById(R.id.toolbar_reported));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("");
        }
        recyclerView = findViewById(R.id.reported_invoices);
        LinearLayout linearLayout = findViewById(R.id.reported_invoice_layout);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_report);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.mainBlue),ContextCompat.getColor(this,R.color.cardBlue), ContextCompat.getColor(this,R.color.windowBlue));
        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        String email = getIntent().getStringExtra("email");
        reportViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(), email)).get(ReportViewModel.class);
        loadData();




        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    recentPosition = viewHolder.getBindingAdapterPosition();
                    Report report = reportedInvoiceAdapter.recentRemove(viewHolder.getBindingAdapterPosition());
                    deleteDialog(report, linearLayout);

                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(ReportedInvoiceActivity.this, R.color.windowBlue))
                        .addSwipeLeftActionIcon(R.drawable.delete)
                        .addSwipeLeftLabel("DELETE")
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(ReportedInvoiceActivity.this, R.color.windowBlue))
                        .addSwipeRightActionIcon(R.drawable.edit)
                        .addSwipeRightLabel("EDIT")
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

    }
    private void loadData(){
        reportViewModel.getReportedInvoices().observe(this, reports -> {
            Collections.reverse(reports);
            reportedInvoiceAdapter = new ReportedInvoiceAdapter(reports, ReportedInvoiceActivity.this, ReportedInvoiceActivity.this, reportViewModel);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReportedInvoiceActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(reportedInvoiceAdapter);
            recyclerView.setHasFixedSize(true);
            swipeRefreshLayout.setRefreshing(false);
            dialog.dismiss();
        });
    }

    @SuppressLint("SetTextI18n")
    public void deleteDialog(Report report, LinearLayout linearLayout) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Button remove = dialog.findViewById(R.id.delete_btn);
        Button cancel = dialog.findViewById(R.id.cancel_delete_btn);
        TextView barcode = dialog.findViewById(R.id.barcode_delete);
        TextView itemName = dialog.findViewById(R.id.item_name_delete);
        TextView itemPrice = dialog.findViewById(R.id.item_price_delete);
        remove.setText("Remove");
        cancel.setText("Cancel");
        barcode.setText("Client: " + report.getClientEmail());
        itemName.setVisibility(View.GONE);
        itemPrice.setText("Reason: " + report.getReason());
        remove.setOnClickListener(v -> {
            reportViewModel.deleteReportRequest(linearLayout, report.getId());
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> {
            reportedInvoiceAdapter.undoRecent(recentPosition, report);
            dialog.dismiss();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();

    }
}