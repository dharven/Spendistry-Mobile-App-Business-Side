package com.shashank.spendistrybusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

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

import com.broooapps.graphview.CurveGraphConfig;
import com.broooapps.graphview.CurveGraphView;
import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.PointMap;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.shashank.spendistrybusiness.R;

import ru.nikartm.support.BadgePosition;
import ru.nikartm.support.ImageBadgeView;

public class DashboardActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ImageBadgeView imageBadgeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
        ImageButton imageButton = findViewById(R.id.edit_profile);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
                                       });


        CurveGraphView curveGraphView = findViewById(R.id.cgv);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        FrameLayout badgeLayout = (FrameLayout) menu.findItem(R.id.reports).getActionView();
        imageBadgeView = badgeLayout.findViewById(R.id.badge);
        imageBadgeView.setBadgeValue(4);
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