package com.example.skills_test;

import android.content.Context;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DonutChart {

    private PieChart pieChart;

    public DonutChart(PieChart pieChart) {
        this.pieChart = pieChart;
    }

    // Create the donut chart from real app usage data
    public void createDonutChart(Context context, List<AppDetails> appDetailsList) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (AppDetails appDetails : appDetailsList) {
            // Convert the total time used from a human-readable string to seconds for the chart
            float totalTimeInSeconds = parseTimeToSeconds(appDetails.totalTimeUsed);

            // Only include apps with non-zero usage time
            if (totalTimeInSeconds > 0) {
                entries.add(new PieEntry(totalTimeInSeconds, appDetails.name));
            }
        }

        if (entries.isEmpty()) {
            entries.add(new PieEntry(1, "No Data")); // Placeholder entry if no usage data
        }

        PieDataSet dataSet = new PieDataSet(entries, "App Usage");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40f);
        pieChart.setHoleColor(android.graphics.Color.WHITE);
        pieChart.setCenterTextSize(18f);

        pieChart.getDescription().setEnabled(false);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // Refresh chart
    }

    // Helper method to parse the human-readable time (hours, minutes, seconds) into total seconds
    private float parseTimeToSeconds(String formattedTime) {
        float totalSeconds = 0;

        // Split the formatted time into components
        String[] timeComponents = formattedTime.split(" ");
        for (int i = 0; i < timeComponents.length; i++) {
            if (timeComponents[i].contains("hour")) {
                totalSeconds += Float.parseFloat(timeComponents[i - 1]) * 3600; // Convert hours to seconds
            } else if (timeComponents[i].contains("minute")) {
                totalSeconds += Float.parseFloat(timeComponents[i - 1]) * 60; // Convert minutes to seconds
            } else if (timeComponents[i].contains("second")) {
                totalSeconds += Float.parseFloat(timeComponents[i - 1]); // Already in seconds
            }
        }

        return totalSeconds;
    }
}
