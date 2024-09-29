package com.example.skills_test;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private AppList appListAdapter; // Custom adapter for the ListView
    private PieChart pieChart; // PieChart for the donut chart
    private AppUsageManager appUsageManager; // Instance for AppUsageManager
    private DonutChart donutChart; // Instance for DonutChart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        appListAdapter = new AppList(this, new ArrayList<>()); // Initialize custom adapter
        listView.setAdapter(appListAdapter);

        // Initialize the AppUsageManager and DonutChart
        appUsageManager = new AppUsageManager(this);
        pieChart = findViewById(R.id.pieChart);
        donutChart = new DonutChart(pieChart); // Create DonutChart instance

        // Retrieve app usage data and update UI
        List<AppDetails> appDetailsList = appUsageManager.getAppUsageStats();
        updateUI(appDetailsList);

        // Call the createDonutChart method to display the chart
        donutChart.createDonutChart(this);

        Button btnOpenUsageAccessSettings = findViewById(R.id.button);
        btnOpenUsageAccessSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        });
    }

    private void updateUI(List<AppDetails> appDetailsList) {
        appListAdapter.clear();
        appListAdapter.addAll(appDetailsList);
        appListAdapter.notifyDataSetChanged();
    }
}
