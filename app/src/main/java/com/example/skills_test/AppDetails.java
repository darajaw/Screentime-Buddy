// AppDetails.java
package com.example.skills_test;

import android.graphics.drawable.Drawable;

public class AppDetails {
    public String name; // Name of the app
    public Drawable icon; // Icon of the app
    public long totalTimeUsed; // Total time used by the app

    // Constructor
    public AppDetails(String name, Drawable icon, long totalTimeUsed) {
        this.name = name;
        this.icon = icon;
        this.totalTimeUsed = totalTimeUsed;
    }
}
