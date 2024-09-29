package com.example.skills_test;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class DonutChart {

    private PieChart pieChart;

    // Constructor
    public DonutChart(PieChart pieChart) {
        this.pieChart = pieChart;
    }

    // Method to configure and create the donut chart
    public void createDonutChart(Context context) {
        // Example data
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "Category 1"));
        entries.add(new PieEntry(30f, "Category 2"));
        entries.add(new PieEntry(20f, "Category 3"));
        entries.add(new PieEntry(10f, "Category 4"));

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Set colors for the chart
        dataSet.setSliceSpace(3f); // Space between slices
        dataSet.setSelectionShift(5f); // Shift when slice is selected

        // Enable hole for donut effect
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40f); // Size of the donut hole
        pieChart.setHoleColor(Color.WHITE); // Color of the hole

        // Optional: Add center text
        pieChart.setCenterText("Donut Chart");
        pieChart.setCenterTextSize(18f);

        // Set data to the PieChart
        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Refresh chart after setting data
        pieChart.invalidate(); // Refresh the chart
    }
}
