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
            // Only include apps with usage time
            if (appDetails.totalTimeUsed > 0) {
                entries.add(new PieEntry(appDetails.totalTimeUsed, appDetails.name));
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
        pieChart.setCenterText("App Usage");
        pieChart.setCenterTextSize(18f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // Refresh chart
    }
}
