package com.example.periodapp;

import android.content.Context;
import android.database.SQLException;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class Calendar extends Fragment implements DialogEventAdder.DateAdderInterface {
    List<EventDay> events = new ArrayList<>();
    CalendarView calendarView;
    private OnFragmentInteractionListener mListener;

    public Calendar() {}

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
        ArrayList<String> list_of_Events= new ArrayList<>();
        DatabaseUser db = new DatabaseUser(getActivity());
        db.open();
        Toast.makeText(getActivity(), SaveSharedPreferences.getNick(getActivity()), Toast.LENGTH_SHORT).show();
        String nick = SaveSharedPreferences.getNick(getActivity()).trim();

        list_of_Events= db.getEvents(nick);

        for (String s:list_of_Events
             ) {

            s=s.substring(s.indexOf(",")+1);
            String date= s.substring(0,s.indexOf(","));
            String option = s.substring(s.indexOf(",")+1);
            int numberOption=0;
            if(option.trim().equals("start")){
                numberOption=1;
            }
            addDate(date,numberOption);
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEventAdder dialogEventAdder = new DialogEventAdder();
                dialogEventAdder.setTargetFragment(Calendar.this,1);
                dialogEventAdder.show(getFragmentManager(), "dateAdder");
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
            Toast.makeText(getContext(),date1, Toast.LENGTH_SHORT).show();
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
        try {
            DatabaseUser db = new DatabaseUser(getActivity());
            db.open();
            String type_of_event= null;
            if(option==1){
                type_of_event="start";
            }
            if(option==0){
                type_of_event="end";
            }
            db.addEventToDatabase(date1,SaveSharedPreferences.getNick(getContext()),type_of_event);
            Toast.makeText(getContext(), "yo888oo", Toast.LENGTH_SHORT).show();
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
