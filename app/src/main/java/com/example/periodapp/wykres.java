package com.example.periodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;

public class wykres extends AppCompatActivity {
    AnyChartView wyk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wykres);
        wyk = findViewById(R.id.any_chart_view);


    }
}
