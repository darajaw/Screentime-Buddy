package com.example.skills_test;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.health.connect.datatypes.AppInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

// Custom adapter class to display app info
public class AppList extends ArrayAdapter<AppDetails> {

    public AppList(Context context, List<AppDetails> appList) {
        super(context, 0, appList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AppDetails appDetails = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_app_info, parent, false);
        }

        // Lookup view for data population
        ImageView appIcon = convertView.findViewById(R.id.app_icon);
        TextView appName = convertView.findViewById(R.id.app_name);
        TextView appUsage = convertView.findViewById(R.id.app_usage);

        // Populate the data into the template view using the AppDetails object
        if (appDetails != null) {
            appIcon.setImageDrawable(appDetails.icon);
            appName.setText(appDetails.name);
            appUsage.setText(String.format("Time Used: %s", appDetails.totalTimeUsed));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}

