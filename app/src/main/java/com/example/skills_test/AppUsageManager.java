package com.example.skills_test;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class AppUsageManager {

    private final PackageManager packageManager;

    public AppUsageManager(Context context) {
        this.packageManager = context.getPackageManager();
    }

    public List<AppDetails> getAppUsageStats() {
        // Return an empty list; mock data will be provided in the fragments
        return new ArrayList<>();
    }
}
