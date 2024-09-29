package com.example.skills_test;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private ListView listView;
    private AppList appListAdapter;
    private PieChart pieChart;
    private AppUsageManager appUsageManager;
    private DonutChart donutChart;
    private TotalScreenTimeCalc totalScreenTimeCalc;
    private TextView totalTimeTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        listView = view.findViewById(R.id.listView);
        totalTimeTextView = view.findViewById(R.id.tvTotalScreenTime);
        pieChart = view.findViewById(R.id.pieChart);

        // Initialize managers
        appUsageManager = new AppUsageManager(requireContext());
        totalScreenTimeCalc = new TotalScreenTimeCalc(requireContext());
        donutChart = new DonutChart(pieChart);

        // Initialize the adapter for the ListView
        appListAdapter = new AppList(requireContext(), new ArrayList<>());
        listView.setAdapter(appListAdapter);

        // Display mock app data
        retrieveAndDisplayMockAppUsageData();

        // Set up button to open usage access settings
        Button btnOpenUsageAccessSettings = view.findViewById(R.id.button);
        btnOpenUsageAccessSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        });
    }

    // Method to retrieve and display mock app usage data for FirstFragment
    private void retrieveAndDisplayMockAppUsageData() {
        List<AppDetails> mockAppDetails = createMockAppDetails();
        updateUI(mockAppDetails);

        // Create and display the donut chart
        donutChart.createDonutChart(requireContext(), mockAppDetails);
    }

    // Method to create a mock list of AppDetails for FirstFragment
    private List<AppDetails> createMockAppDetails() {
        List<AppDetails> mockAppDetails = new ArrayList<>();

        // Data for mock apps: name, usage time (in minutes), and drawable resource
        String[] appNames = {"Pizza Hut", "FaceBook", "TikTok","Duolingo"};
        long[] usageTimes = {500, 130, 45, 11}; // Usage time in minutes
        int[] iconResources = {
                R.drawable.zaza,
                R.drawable.facebook,
                R.drawable.tiktok,
                R.drawable.duo
        };

        // Create mock AppDetails instances for each app
        for (int i = 0; i < appNames.length; i++) {
            mockAppDetails.add(createMockAppDetail(appNames[i], usageTimes[i], iconResources[i]));
        }

        return mockAppDetails;
    }

    // Helper method to create an AppDetail instance with a custom icon
    private AppDetails createMockAppDetail(String appName, long minutes, int iconResource) {
        // Load your custom JPEG image from resources
        Drawable appIcon = requireContext().getDrawable(iconResource);

        // Create a mock AppDetails instance
        String formattedTime = formatTime(minutes * 60 * 1000); // Convert minutes to milliseconds

        return new AppDetails(appName, appIcon, formattedTime);
    }

    // Method to convert time from milliseconds to a readable format (seconds, minutes, hours)
    private String formatTime(long totalTimeInMillis) {
        long hours = totalTimeInMillis / (1000 * 60 * 60);
        long minutes = (totalTimeInMillis / (1000 * 60)) % 60;
        long seconds = (totalTimeInMillis / 1000) % 60;

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

    // Update ListView with app usage data
    private void updateUI(List<AppDetails> appDetailsList) {
        appListAdapter.clear();
        appListAdapter.addAll(appDetailsList);
        appListAdapter.notifyDataSetChanged();
    }
}
