package com.example.periodapp;

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

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Home extends Fragment implements DialogEventAdder.DateAdderInterface {
    List<EventDay> events = new ArrayList<>();
    CalendarView calendarView;
    FirebaseAuth mAuth;
    private OnFragmentInteractionListener mListener;
    DatabaseUser db;
    ArrayList<String> list_of_Events= new ArrayList<>();
    public Home() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id= user.getUid();
        db = new DatabaseUser(getContext());
        db.open();
        list_of_Events=db.getEvents(id);//sciaga dane z sql lite
        db.close();
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        calendarView =  view.findViewById(R.id.calendarView);
        Button addButton=view.findViewById(R.id.addButtonDialog);

        ArrayList<String> lista= new ArrayList<>();

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id= user.getUid();

        db = new DatabaseUser(getContext());
        db.open();
        list_of_Events=db.getEvents(id);//sciaga dane z sql lite
        db.close();

        if(list_of_Events!=null){
            for (String s:list_of_Events
                 ) {
                s=s.substring(s.indexOf(",")+1);
                String date= s.substring(0,s.indexOf(","));
                String option = s.substring(s.indexOf(",")+1);
                int option1= Integer.parseInt(option);
                Date dateToAdd;
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                try {
                    dateToAdd= simpleDateFormat.parse(date);
                    java.util.Calendar calendar1 = java.util.Calendar.getInstance();
                    calendar1.setTime(dateToAdd);
                    if(option1==1){
                        events.add( new EventDay(calendar1, R.drawable.ic_tampon_icon));
                    }
                    else{
                        events.add(new EventDay(calendar1, R.drawable.ic_happy_lady));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        calendarView.setEvents(events);
        for (String s:lista
             ) {list_of_Events.add(s);

        }
        int len= list_of_Events.size();
        for(int i =0;i<len-1;i++){
            String s= list_of_Events.get(i);
            java.util.Calendar calendar1 = java.util.Calendar.getInstance();
            java.util.Calendar calendar2 = java.util.Calendar.getInstance();
            s=s.substring(s.indexOf(",")+1);
            String date= s.substring(0,s.indexOf(","));
            String option = s.substring(s.indexOf(",")+1);

            String e= list_of_Events.get(i+1);
            e=e.substring(e.indexOf(",")+1);
            String date1= e.substring(0,e.indexOf(","));
            String option1 = e.substring(e.indexOf(",")+1);
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
            if(option.trim()=="start"&& option1.trim().equals("end")){
                try {
                    Date startDate= simpleDateFormat.parse(date);
                    Date endDate= simpleDateFormat.parse(date1);
                    calendar1.setTime(startDate);
                    calendar2.setTime(endDate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                CalendarUtils.getDatesRange(calendar1,calendar2);
                calendarView.setHighlightedDays(CalendarUtils.getDatesRange(calendar1,calendar2));

            }
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEventAdder dialogEventAdder = new DialogEventAdder();
                dialogEventAdder.setTargetFragment(Home.this,1);
                dialogEventAdder.show(getFragmentManager(), "dateAdder");
            }
        });
    }

    @Override
    public void addDate(String date1, int option) {
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id= user.getUid();
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
        }
        else if(option==0){
            events.add(new EventDay(calendar1, R.drawable.ic_happy_lady));
        }
        calendarView.setEvents(events);
        addToDB(date1,option,id);

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public void addToDB(String date, int type, String id) {
        String type1= Integer.toString(type);
        db.open();
        FirebaseUser user= mAuth.getCurrentUser();
        id=user.getUid();
        db.addEventToDatabase(date,id,type1);
        db.close();
    }

}
