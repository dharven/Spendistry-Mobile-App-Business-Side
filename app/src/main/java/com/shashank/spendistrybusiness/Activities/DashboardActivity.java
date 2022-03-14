package com.shashank.spendistrybusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.broooapps.graphview.CurveGraphConfig;
import com.broooapps.graphview.CurveGraphView;
import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.PointMap;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.DashboardViewModel;

import ru.nikartm.support.BadgePosition;
import ru.nikartm.support.ImageBadgeView;

public class DashboardActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ImageBadgeView imageBadgeView;
    private CurveGraphView curveGraphView;
    private String email, yearlyIncome, monthlyIncome, totalIncome, issuedInvoices, roundoff;
    private int reportCount;
    private Vendor vendorDetails;
    private TextView monthlyIncomeTextView, yearlyIncomeTextView, issuedInvoicesTextView, nameTextView, emailTextView;
    private SharedPreferences sharedPreferences;
    private DashboardViewModel dashboardViewModel;

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

//
        dashboardViewModel.getDashboardData(email);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        toolbar = findViewById(R.id.toolbar_dashboard);
        monthlyIncomeTextView = findViewById(R.id.monthly_income);
        yearlyIncomeTextView = findViewById(R.id.yearly_income);
        issuedInvoicesTextView = findViewById(R.id.issued_invoices);
        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        setGraph();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
        ImageButton imageButton = findViewById(R.id.edit_profile);
        curveGraphView = findViewById(R.id.cgv);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getDashboardData(email).observe(this, dashboardData -> {
            if (dashboardData != null) {

                monthlyIncome = String.valueOf(dashboardData.getMonthlyIncome());
                yearlyIncome = String.valueOf(dashboardData.getYearlyIncome());
                totalIncome = String.valueOf(dashboardData.getTotalIncome());
                issuedInvoices = String.valueOf(dashboardData.getIssuedInvoices());
                roundoff = String.valueOf(dashboardData.getRoundoff());
                vendorDetails = dashboardData.getVendorDetails();
                reportCount = dashboardData.getReportCount();
                imageBadgeView.setBadgeValue(reportCount);
                nameTextView.setText(vendorDetails.getFirstName()+ " " + vendorDetails.getLastName());
                emailTextView.setText(vendorDetails.getEmail());
                monthlyIncomeTextView.setText(monthlyIncome);
                yearlyIncomeTextView.setText(yearlyIncome);
                issuedInvoicesTextView.setText(issuedInvoices);
//                curveGraphView.setData(new GraphData(new PointMap(dashboardData.getMonthlyIncome(), "Monthly Income"), new PointMap(dashboardData.getYearlyIncome(), "Yearly Income")));
            }
        });


        curveGraphView.configure(
                new CurveGraphConfig.Builder(this)
                        .setAxisColor(R.color.black)                                             // Set X and Y axis line color stroke.
                        .setIntervalDisplayCount(7)                                             // Set number of values to be displayed in X ax
                        .setHorizontalGuideline(2)
                        .setVerticalGuideline(2)// Set number of background guidelines to be shown.
                        .setGuidelineColor(R.color.GreenYellow)
                        .setNoDataMsg(" No Data ")                                              // Message when no data is provided to the view.
                        .setxAxisScaleTextColor(R.color.material_white)                                  // Set X axis scale text color.
                        .setyAxisScaleTextColor(R.color.material_white)                                  // Set Y axis scale text color
                        .setAnimationDuration(2500)// Set animation duration to be used after set data.
                        .build()
        );


        PointMap pointMap = new PointMap();
        pointMap.addPoint(0, 100);
        pointMap.addPoint(1, 500);
        pointMap.addPoint(4, 300);
        pointMap.addPoint(5, 500);

        GraphData gd = GraphData.builder(this)
                .setPointMap(pointMap)
                .setGraphStroke(R.color.black)
                .setGraphGradient(R.color.RoyalBlue, R.color.RoyalBlue)
                .setPointColor(R.color.orange).setPointRadius(3)
                .animateLine(true)
                .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                curveGraphView.setData(7, 600, gd);
            }
        }, 250);




    }

    private void setGraph(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        FrameLayout badgeLayout = (FrameLayout) menu.findItem(R.id.reports).getActionView();
        imageBadgeView = badgeLayout.findViewById(R.id.badge);
//        imageBadgeView.setBadgeValue(4);
        imageBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DashboardActivity.this, ReportedInvoiceActivity.class);
                startActivity(intent1);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_invoice:
                Intent intent2 = new Intent(DashboardActivity.this, MainActivity.class);
                intent2.putExtra("SCAN_RESULT", "shashank@gmail.com");
                startActivity(intent2);
                return true;
            case R.id.logout_dash:
                SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("loggedIn", false).apply();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.Inventory:
                Intent intent3 = new Intent(DashboardActivity.this, CreateInventoryActivity.class);
                startActivity(intent3);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}