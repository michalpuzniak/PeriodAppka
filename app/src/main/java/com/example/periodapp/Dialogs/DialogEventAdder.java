package com.example.periodapp.Dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.periodapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogEventAdder extends DialogFragment {


    Button add;
    Date d;
    Spinner spin;
    DatePicker datePicker;
    int option;
    DateAdderInterface listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View view= inflater.inflate(R.layout.calendar_adder,container,false);
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                spin=view.findViewById(R.id.spinner_calendar);
                add=view.findViewById(R.id.addButtonDialog);
                datePicker=view.findViewById(R.id.datepickerSpinner);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int day= datePicker.getDayOfMonth();
                        int month =datePicker.getMonth()+1;
                        int year= datePicker.getYear();
                        String dateToAdd;
                        if(day<10){
                            if(month<10) {
                                dateToAdd="0"+day+"/"+"0"+month+"/"+year;
                            }
                            else{
                                dateToAdd="0"+day+"/"+month+"/"+year;
                            }
                        }
                        else{
                            if(month<10){
                                dateToAdd=day+"/"+"0"+month+"/"+year;}
                            else dateToAdd=day+"/"+month+"/"+year;                       
                        }
                        if (spin.getSelectedItem().toString().equals("Start of Menstruation")) {
                            option =1;
                        }
                        else if(spin.getSelectedItem().toString().equals("End of Menstruation")){
                            option =0;
                        }
                        listener.addDate(dateToAdd, option);
                        getDialog().dismiss();
                    }
                });

                return view;
    }

    public interface DateAdderInterface{
        void addDate(String date, int option);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try
        {
            listener = (DateAdderInterface) getTargetFragment();
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString());
        }
    }
}