package com.example.skills_test;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView appList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appList = (TextView) findViewById(R.id.appList);

        // Retrieve app usage data
        getAppUsageStats();

        Button btnOpenUsageAccessSettings = findViewById(R.id.button);
        btnOpenUsageAccessSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        });
    }

    private void getAppUsageStats() {
        appList.setText("");

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

                // Print or display the app usage data
                appList.append(String.format("Package: %s, Time Used: %d ms. %n", packageName, totalTimeInForeground));

            }
        } else {
            // Prompt user to enable usage access permission
            appList.append("No usage data available. Please ensure usage access is enabled for this app.");
        }
    }
}
