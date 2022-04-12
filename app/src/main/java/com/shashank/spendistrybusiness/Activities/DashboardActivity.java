package com.shashank.spendistrybusiness.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.broooapps.graphview.CurveGraphConfig;
import com.broooapps.graphview.CurveGraphView;
import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.PointMap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistrybusiness.ViewModels.DashboardViewModel;

import java.util.ArrayList;
import java.util.Collections;

import ru.nikartm.support.ImageBadgeView;

@SuppressWarnings("ALL")
public class DashboardActivity extends AppCompatActivity {
    ImageBadgeView imageBadgeView;
    private CurveGraphView curveGraphView;
    private String email, yearlyIncome, monthlyIncome, totalIncome, issuedInvoices, roundoff;
    private int reportCount;
    private Vendor vendorDetails;
    private TextView monthlyIncomeTextView, yearlyIncomeTextView, issuedInvoicesTextView, nameTextView, emailTextView;
    private SharedPreferences sharedPreferences;
    private DashboardViewModel dashboardViewModel;
    private PointMap pointMap;
    private LinearLayout linearLayout;

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
        dashboardViewModel.getDashboardData();
    }


    /**
     * This is the fragment-orientated version of {@link #onResume()} that you
     * can override to perform operations in the Activity at the same point
     * where its fragments are resumed.  Be sure to always call through to
     * the super-class.
     */
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        sharedPreferences.edit().putBoolean("report",false).apply();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Toolbar toolbar = findViewById(R.id.toolbar_dashboard);
        monthlyIncomeTextView = findViewById(R.id.monthly_income);
        yearlyIncomeTextView = findViewById(R.id.yearly_income);
        issuedInvoicesTextView = findViewById(R.id.issued_invoices);
        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        TextView dashboardTextView = findViewById(R.id.textView_dashboard_title);
        TextView monthlyTextView = findViewById(R.id.monthlyTextView);
        TextView issueTextView = findViewById(R.id.issueTextView);
        setSupportActionBar(toolbar);
        linearLayout = findViewById(R.id.dashboard_layout);
        CardView cardView = findViewById(R.id.issued_invoices_card);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthDp = displayMetrics.densityDpi;

        //
            if (widthDp <= 350) {
                dashboardTextView.setTextSize(32);
                monthlyTextView.setTextSize(18);
                issueTextView.setTextSize(18);
                monthlyIncomeTextView.setTextSize(18);
                yearlyIncomeTextView.setTextSize(16);
                issuedInvoicesTextView.setTextSize(20);
                nameTextView.setTextSize(14);
                emailTextView.setTextSize(14);

            }

        ImageButton imageButton = findViewById(R.id.edit_profile);
        RoundedImageView roundedImageView = findViewById(R.id.profile);
        curveGraphView = findViewById(R.id.cgv);
        imageButton.setEnabled(false);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
            intent.putExtra("vendorDetails", vendorDetails);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
        pointMap = new PointMap();
        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        Glide.with(this).load(Constants.URL_API+"vendorProfile/"+email+".jpeg")
                .placeholder(R.drawable.loading).error(R.drawable.no_profile) .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true)).into(roundedImageView);
        dashboardViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(), linearLayout,email)).get(DashboardViewModel.class);
        dashboardViewModel.getDashboardData().observe(this, dashboardData -> {
            if (dashboardData != null) {
                dialog.dismiss();
                imageButton.setEnabled(true);

                monthlyIncome = String.valueOf(dashboardData.getMonthlyIncome());
                yearlyIncome = String.valueOf(dashboardData.getYearlyIncome());
                totalIncome = String.valueOf(dashboardData.getTotalIncome());
                issuedInvoices = String.valueOf(dashboardData.getIssuedInvoices());
                roundoff = String.valueOf(dashboardData.getRoundoff());
                vendorDetails = dashboardData.getVendorDetails();
                reportCount = dashboardData.getReportCount();
                imageBadgeView.setBadgeValue(reportCount);
                nameTextView.setText(vendorDetails.getFirstName()+ " " + vendorDetails.getLastName());
                emailTextView.setText(vendorDetails.getVendorId());
                monthlyIncomeTextView.setText("₹"+monthlyIncome);
                yearlyIncomeTextView.setText("₹"+yearlyIncome);
                issuedInvoicesTextView.setText(issuedInvoices);
                ArrayList<Integer> arrayList = dashboardData.getRoundoff();
                //create for loop to add data to graph
                for (int i = 0; i < arrayList.size(); i++) {
                    pointMap.addPoint(i, arrayList.get(i));
                }
                curveGraphView.configure(
                        new CurveGraphConfig.Builder(this)
                                .setAxisColor(R.color.black)                                             // Set X and Y axis line color stroke.
                                .setIntervalDisplayCount(7)                                             // Set number of values to be displayed in X ax
                                .setHorizontalGuideline(2)
                                .setVerticalGuideline(2)// Set number of background guidelines to be shown.
                                .setGuidelineColor(R.color.mainBlue)
                                .setNoDataMsg(" No Data ")                                              // Message when no data is provided to the view.
                                .setxAxisScaleTextColor(R.color.material_white)                                  // Set X axis scale text color.
                                .setyAxisScaleTextColor(R.color.material_white)                                  // Set Y axis scale text color
                                .setAnimationDuration(2500)// Set animation duration to be used after set data.
                                .build()
                );

                GraphData gd = GraphData.builder(this)
                        .setPointMap(pointMap)
                        .setGraphStroke(R.color.black)
                        .setGraphGradient(R.color.RoyalBlue, R.color.RoyalBlue)
                        .setPointColor(R.color.orange).setPointRadius(3)
//                        .animateLine(true)
                        .build();

                new Handler().postDelayed(() -> {
                    Collections.sort(arrayList);
                    curveGraphView.setData(arrayList.size(), arrayList.get(arrayList.size()-1), gd);
                }, 250);
            }

            cardView.setOnClickListener(view -> {
                if (dashboardData != null) {
                    if (dashboardData.getIssuedInvoices() >0) {
                        Intent intent = new Intent(DashboardActivity.this, BusinessInvoicesActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("activity","issued");
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } else {
                        Snackbar snackbar = Snackbar.make(linearLayout, "No Invoices Available", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(getResources().getColor(R.color.red));
                        snackbar.show();
                    }
                }
            });
        });






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        FrameLayout badgeLayout = (FrameLayout) menu.findItem(R.id.reports).getActionView();
        imageBadgeView = badgeLayout.findViewById(R.id.badge);
        imageBadgeView.setOnClickListener(view -> {
            if (reportCount > 0) {
                Intent intent1 = new Intent(DashboardActivity.this, ReportedInvoiceActivity.class);
                intent1.putExtra("email", email);
                startActivity(intent1);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            } else {
                Snackbar snackbar = Snackbar.make(linearLayout, "No Reports Available", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(getResources().getColor(R.color.red));
                snackbar.show();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint({"UnsafeOptInUsageError", "NonConstantResourceId"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_invoice:
                Intent intent2 = new Intent(DashboardActivity.this, MainActivity.class);
                intent2.putExtra("SCAN_RESULT", "shashank@gmail.com");
                startActivity(intent2);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            case R.id.logout_dash:
                dashboardViewModel.deleteInventory();
                dashboardViewModel.deleteDashboard();
                SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("loggedIn", false).apply();
                sharedPreferences.edit().putBoolean("hasData", true).apply();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            case R.id.Returned:
                Intent intent1 = new Intent(DashboardActivity.this, BusinessInvoicesActivity.class);
                intent1.putExtra("email", email);
                intent1.putExtra("activity","returned");
                startActivity(intent1);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            case R.id.Inventory:
                Intent intent3 = new Intent(DashboardActivity.this, CreateInventoryActivity.class);
                startActivity(intent3);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}