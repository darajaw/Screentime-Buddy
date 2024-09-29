package com.example.skills_test;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private AppList appListAdapter;
    private PieChart pieChart;
    private AppUsageManager appUsageManager;
    private DonutChart donutChart;
    private TotalScreenTimeCalc totalScreenTimeCalc;
    private TextView totalTimeTextView;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));

        // Initialize views
        listView = findViewById(R.id.listView);
        totalTimeTextView = findViewById(R.id.tvTotalScreenTime);
        pieChart = findViewById(R.id.pieChart);

        // Initialize managers
        appUsageManager = new AppUsageManager(this);
        totalScreenTimeCalc = new TotalScreenTimeCalc(this);
        donutChart = new DonutChart(pieChart);

        // Initialize the adapter for the ListView
        appListAdapter = new AppList(this, new ArrayList<>());
        listView.setAdapter(appListAdapter);

        // Check if the app has usage stats permission, and if not, request it
        if (!hasUsageStatsPermission()) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        } else {
            displayScreenTime();
            retrieveAndDisplayAppUsageData();
        }

        // Set up button to open usage access settings if needed
        Button btnOpenUsageAccessSettings = findViewById(R.id.button);
        btnOpenUsageAccessSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        });
    }

    // Check if the app has permission to access usage stats
    private boolean hasUsageStatsPermission() {
        List<AppDetails> usageStats = appUsageManager.getAppUsageStats();
        return usageStats != null && !usageStats.isEmpty();
    }

    // Method to display total screen time
    private void displayScreenTime() {
        long totalTimeInMillis = totalScreenTimeCalc.calculateTotalScreenTime();
        String formattedTime = totalScreenTimeCalc.formatTime(totalTimeInMillis);
        totalTimeTextView.setText("Total Screen Time: " + formattedTime);
    }

    // Method to retrieve and display app usage data
    private void retrieveAndDisplayAppUsageData() {
        List<AppDetails> appDetailsList = appUsageManager.getAppUsageStats();
        updateUI(appDetailsList);

        // Create and display the donut chart
        donutChart.createDonutChart(this, appDetailsList);
    }

    // Update ListView with app usage data
    private void updateUI(List<AppDetails> appDetailsList) {
        appListAdapter.clear();
        appListAdapter.addAll(appDetailsList);
        appListAdapter.notifyDataSetChanged();
    }

    // Define the adapter as a private inner class, but outside of onCreate
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(@NonNull AppCompatActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                default:
                    return new FirstFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2; // Number of fragments
        }
    }
}
