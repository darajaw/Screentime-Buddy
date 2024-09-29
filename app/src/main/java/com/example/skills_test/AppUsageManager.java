package com.example.skills_test;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppUsageManager {

    private final UsageStatsManager usageStatsManager;
    private final PackageManager packageManager;

    public AppUsageManager(Context context) {
        this.usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        this.packageManager = context.getPackageManager();
    }

    public List<AppDetails> getAppUsageStats() {
        List<AppDetails> appDetails = new ArrayList<>();

        // Set the time range for the last 24 hours
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, -1); // Go back 24 hours
        long startTime = calendar.getTimeInMillis();

        // Query usage stats for the given time range
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        // Process each usage stat item
        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            for (UsageStats usageStats : usageStatsList) {
                String packageName = usageStats.getPackageName();
                long totalTimeInForeground = usageStats.getTotalTimeInForeground();

                if (totalTimeInForeground > 0) {
                    try {
                        ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                        String appName = appInfo.loadLabel(packageManager).toString();
                        Drawable appIcon = appInfo.loadIcon(packageManager);

                        // Convert total time from milliseconds to a human-readable format
                        String formattedTime = formatTime(totalTimeInForeground);

                        // Add the app details to the list
                        appDetails.add(new AppDetails(appName, appIcon, formattedTime));
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return appDetails;
    }

    // Method to convert time from milliseconds to a readable format (seconds, minutes, hours)
    private String formatTime(long totalTimeInMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(totalTimeInMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeInMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeInMillis) % 60;

        StringBuilder formattedTime = new StringBuilder();
        if (hours > 0) {
            formattedTime.append(hours).append(" hours ");
        }
        if (minutes > 0) {
            formattedTime.append(minutes).append(" minutes ");
        }
        if (seconds > 0 || (hours == 0 && minutes == 0)) {
            formattedTime.append(seconds).append(" seconds");
        }

        return formattedTime.toString().trim(); // Return the formatted time, removing any extra spaces
    }
}
