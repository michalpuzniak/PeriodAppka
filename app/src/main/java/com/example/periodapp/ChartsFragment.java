package com.example.periodapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;


public class ChartsFragment extends Fragment {
AnyChartView chartView;

    public ChartsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charts, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        chartView= view.findViewById(R.id.piechart);
        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {

            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Menstruacja", 4));
        data.add(new ValueDataEntry("Faza Folikularna", 9));
        data.add(new ValueDataEntry("Owulacja", 1));
        data.add(new ValueDataEntry("Faza lutealna", 14));


        pie.data(data);

        pie.title("Fazy cyklu menstruacyjnego");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("fazy")
                .padding(0d, 0d, 10d, 0d);
        chartView.setChart(pie);
    }
}