package com.example.periodapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class Calendar extends Fragment implements DialogCalendarAdder.DateAdderInterface {
    List<EventDay> events = new ArrayList<>();
    CalendarView calendarView;
    private OnFragmentInteractionListener mListener;

    public Calendar() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        calendarView =  view.findViewById(R.id.calendarView);
        Button addButton=view.findViewById(R.id.addButtonDialog);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCalendarAdder dialogCalendarAdder = new DialogCalendarAdder();
                dialogCalendarAdder.setTargetFragment(Calendar.this,1);
                dialogCalendarAdder.show(getFragmentManager(), "dateAdder");

            }
        });
    }
    @Override
    public void addDate(String date1, int option) {
        java.util.Calendar calendar1 = java.util.Calendar.getInstance();
        Date dateToAdd;



        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateToAdd= simpleDateFormat.parse(date1);
            calendar1.setTime(dateToAdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(option==1){
            events.add(new EventDay(calendar1,R.drawable.ic_tampon_icon));
            Toast.makeText(getContext(), "yoooo", Toast.LENGTH_SHORT).show();
        }
        else if(option==0){
            events.add(new EventDay(calendar1, R.drawable.ic_happy_lady));
        }
        calendarView.setEvents(events);

    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
