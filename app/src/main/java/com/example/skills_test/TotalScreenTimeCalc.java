package com.example.skills_test;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TotalScreenTimeCalc {

    private final UsageStatsManager usageStatsManager;

    public TotalScreenTimeCalc(Context context) {
        this.usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    // Calculate total screen time in the past 24 hours
    public long calculateTotalScreenTime() {
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, -1); // Last 24 hours
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        long totalTime = 0;
        if (usageStatsList != null) {
            for (UsageStats usageStats : usageStatsList) {
                totalTime += usageStats.getTotalTimeInForeground();
            }
        }

        return totalTime; // Return total time in milliseconds
    }

    // Convert milliseconds to a readable format (hours and minutes)
    public String formatTime(long totalTimeInMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(totalTimeInMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeInMillis) % 60;
        return hours + " hours " + minutes + " minutes";
    }
}
