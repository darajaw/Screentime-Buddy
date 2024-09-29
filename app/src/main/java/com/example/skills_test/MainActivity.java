// MainActivity.java
package com.example.skills_test;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private AppList appListAdapter; // Custom adapter for the ListView
    private PieChart pieChart; // PieChart for the donut chart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        appListAdapter = new AppList(this, new ArrayList<>()); // Initialize custom adapter
        listView.setAdapter(appListAdapter);

        // Retrieve app usage data
        getAppUsageStats();

        Button btnOpenUsageAccessSettings = findViewById(R.id.button);
        btnOpenUsageAccessSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        });

        // Initialize PieChart for donut chart
        pieChart = findViewById(R.id.pieChart);

        // Create an instance of DonutChart and pass the PieChart
        DonutChart donutChart = new DonutChart(pieChart);

        // Call the createDonutChart method to display the chart
        donutChart.createDonutChart(this);
    }

    private void getAppUsageStats() {
        appListAdapter.clear();

        // Get the UsageStatsManager
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        // Set the time range (last 24 hours)
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long startTime = calendar.getTimeInMillis();

        // Query the usage stats
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        List<AppDetails> appDetails = new ArrayList<>();

        // Process and display the usage data
        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            for (UsageStats usageStats : usageStatsList) {
                String packageName = usageStats.getPackageName();
                long totalTimeInForeground = usageStats.getTotalTimeInForeground();

                // Check if the app has usage time and is not a system app
                if (totalTimeInForeground > 0 ) {
                    try {
                        // Get app name and icon
                        ApplicationInfo appObject = getPackageManager().getApplicationInfo(packageName, 0);
                        String appName = appObject.loadLabel(getPackageManager()).toString();
                        Drawable appIcon = appObject.loadIcon(getPackageManager());

                        // Add app info to the list
                        appDetails.add(new AppDetails(appName, appIcon, totalTimeInForeground));
                    }
                    catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Update the adapter with the app info list
            appListAdapter.clear();
            appListAdapter.addAll(appDetails);
            appListAdapter.notifyDataSetChanged();
        } else {

            AppDetails nullInfo = new AppDetails("No usage data available.", null, 0);

            appListAdapter.add(nullInfo);
        }
    }
}
