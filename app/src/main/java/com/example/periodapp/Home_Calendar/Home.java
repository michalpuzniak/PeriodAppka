package com.example.periodapp.Home_Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.periodapp.DatabaseUser;
import com.example.periodapp.Dialogs.DialogEventAdder;
import com.example.periodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Home extends Fragment implements DialogEventAdder.DateAdderInterface {
    List<EventDay> events = new ArrayList<>();
    CalendarView calendarView;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
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
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id= user.getUid();// getting id
        calendarView =  view.findViewById(R.id.calendarView);
        Button addButton=view.findViewById(R.id.addButtonDialog);

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
                List<Calendar> calendars= new ArrayList<>();
                calendars.add(calendar1);
                calendars.add(calendar2);
                calendarView.setHighlightedDays(calendars);
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
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure you want to remove this event?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                events.remove(eventDay);
                                //db remove event day
                                Calendar c1= eventDay.getCalendar();
                                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                                String date = simpleDateFormat.format(c1.getTime());

                                db.open();
                                db.deleteEvent(date);
                                db.close();
                                calendarView.setEvents(events);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            calendar1.setTime(dateToAdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(option==1){
            try {
                if(checkLastEvent(date1, option)) {
                    if (list_of_Events.size()>0) {
                        Calendar time_of_ending; //czas zakończenia poprzedniego okresu
                        time_of_ending = events.get(events.size() - 1).getCalendar();
                        events.add(new EventDay(calendar1, R.drawable.ic_tampon_icon));
                        long difference = calendar1.getTimeInMillis() - time_of_ending.getTimeInMillis();
                        int days = (int) (difference / (1000 * 60 * 60 * 24));
                        String dni = String.valueOf(days);
                        addToDB(date1,option,id);

                    }
                    else {
                        events.add(new EventDay(calendar1, R.drawable.ic_tampon_icon));
                        addToDB(date1,option,id);
                    }
                }
                else Toast.makeText(getContext(), "Choose correct date!", Toast.LENGTH_LONG).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if(option==0){
            try {
                if (list_of_Events.size()>0) {
                    if(checkLastEvent(date1, option)) {
                        Calendar time_of_ending; //czas zakończenia poprzedniego okresu
                        time_of_ending = events.get(events.size() - 1).getCalendar();
                        long difference = calendar1.getTimeInMillis() - time_of_ending.getTimeInMillis();
                        int days = (int) (difference / (1000 * 60 * 60 * 24));
                        String dni = String.valueOf(days);
                        events.add(new EventDay(calendar1, R.drawable.ic_happy_lady));
                        addToDB(date1,option,id);

                    }
                }
                else {
                    Toast.makeText(getContext(), "First you need to add start of menstruation date", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        calendarView.setEvents(events);
        db.close();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public void addToDB(String date, int type, String id) {
        String type1= Integer.toString(type);
        db.open();
        FirebaseUser user= mAuth.getCurrentUser();
        id=user.getUid();
        String option= String.valueOf(type);
        db.addEventToDatabase(date,id,type1);
        list_of_Events= db.getEvents(mAuth.getUid());
        db.close();
    }
    //Sprawdza poprawność wprowadzanych danych// Nie można dodać początku okresu jeśli poprzedni się nie zakończył
    public boolean checkLastEvent(String date, int option) throws ParseException {
        db.open();
        list_of_Events= db.getEvents(mAuth.getUid());
        db.close();
        if (list_of_Events.size()>0) {
            String lastOption = list_of_Events.get(list_of_Events.size()-1);
            char last_Option= lastOption.charAt(lastOption.length()-1);
            int ostatnia_opcja= Character.getNumericValue(last_Option);
            if(ostatnia_opcja==option){
                Toast.makeText(getContext(), "Add end date to your last period", Toast.LENGTH_SHORT).show();
                return false;
            }
            else{
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                Date dateToAdd= simpleDateFormat.parse(date);
                Calendar last= events.get(events.size()-1).getCalendar();
                Date lastDate= last.getTime();
                Date today= new Date();
                if(today.after(dateToAdd))
                {
                    return dateToAdd.after(lastDate);
                }
                else  return false;
            }
        }
        else return true;

    }

}
