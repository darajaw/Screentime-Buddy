package com.example.skills_test;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> appListAdapter; // Adapter for the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        appListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(appListAdapter);

        // Retrieve app usage data
        getAppUsageStats();

        Button btnOpenUsageAccessSettings = findViewById(R.id.button);
        btnOpenUsageAccessSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        });
    }

    private void getAppUsageStats() {
        // Get the UsageStatsManager
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        // Set the time range (last 24 hours)
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        // Query the usage stats
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        // Process and display the usage data
        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            for (UsageStats usageStats : usageStatsList) {
                String packageName = usageStats.getPackageName();
                long totalTimeInForeground = usageStats.getTotalTimeInForeground();

                // Check if the app is a system app
                if (!isSystemApp(packageName)) {
                    // Print or display the app usage data
                    appListAdapter.add(String.format("Package: %s, Time Used: %d ms.", packageName, totalTimeInForeground));
                }
            }
        } else {
            // Prompt user to enable usage access permission
            appListAdapter.add("No usage data available. Please ensure usage access is enabled for this app.");
        }
    }

    // Helper method to determine if an app is a system app (source: chatGPT)
    private boolean isSystemApp(String packageName) {
        try {
            // Get application info for the package
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageName, 0);
            return (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false; // If the app is not found, consider it not a system app
        }
    }
}
