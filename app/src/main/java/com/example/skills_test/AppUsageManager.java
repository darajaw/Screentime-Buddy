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

public class AppUsageManager {

    private final UsageStatsManager usageStatsManager;
    private final PackageManager packageManager;

    public AppUsageManager(Context context) {
        this.usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        this.packageManager = context.getPackageManager();
    }

    public List<AppDetails> getAppUsageStats() {
        List<AppDetails> appDetails = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, -1); // Last 24 hours
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            for (UsageStats usageStats : usageStatsList) {
                String packageName = usageStats.getPackageName();
                long totalTimeInForeground = usageStats.getTotalTimeInForeground();

                if (totalTimeInForeground > 0) {
                    try {
                        ApplicationInfo appObject = packageManager.getApplicationInfo(packageName, 0);
                        String appName = appObject.loadLabel(packageManager).toString();
                        Drawable appIcon = appObject.loadIcon(packageManager);
                        appDetails.add(new AppDetails(appName, appIcon, totalTimeInForeground));
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return appDetails;
    }
}
