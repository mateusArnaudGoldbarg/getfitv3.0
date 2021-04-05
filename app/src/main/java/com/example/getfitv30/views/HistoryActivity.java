package com.example.getfitv30.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.getfitv30.R;
import com.example.getfitv30.logic.Profile;
import com.example.getfitv30.models.UserWeights;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity
{
    private long uid;
    private LineChart weightChart;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.graph_activity);

        // Storing the chart
        this.weightChart = (LineChart)findViewById(R.id.weight_history);

        // Reading the userId from the shared preferences
        SharedPreferences sharedPref = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        this.uid = sharedPref.getLong("uid", -1);

        // Getting the chart data from the database
        this.profile = new Profile(HistoryActivity.this);
        ArrayList<UserWeights> chart_data = this.profile.getAllWeights(this.uid);

        this.createChart(chart_data);
    }

    // Creating the chart and displaying the data
    private void createChart(ArrayList<UserWeights> data)
    {
        ArrayList<Entry> values = this.profile.convertWeightsToEntries(data);

        LineDataSet weightDataSet = new LineDataSet(values, "Weight");
        weightDataSet.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(weightDataSet);

        LineData lineData = new LineData(dataSets);

        this.weightChart.setData(lineData);
    }

    // Opens the insert weight activity
    public void openInsertWeight(View view)
    {
        Intent intent = new Intent(HistoryActivity.this, InsertWeightActivity.class);
        HistoryActivity.this.startActivity(intent);
    }

    // Back button press (top-left button)
    public void backToProfile(View view)
    {
        Intent intent = new Intent(HistoryActivity.this, ProfileActivity.class);
        HistoryActivity.this.startActivity(intent);
    }
}
