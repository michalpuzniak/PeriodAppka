package com.example.periodapp;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileOutputStream;


import java.io.IOException;
import java.io.InputStreamReader;


import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;


public class NewAccount extends AppCompatActivity implements Serializable {

    Button registerButton;
    EditText etName,  etBirth, etWeight, etHeight, etNickname, etMail;
    Calendar c;
    DatePickerDialog dpd;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        registerButton = findViewById(R.id.btnRegister);
        etName= findViewById(R.id.etName);
        etWeight= findViewById(R.id.etWeight);
        etHeight= findViewById(R.id.etHeight);
        etBirth = findViewById(R.id.etBirthday);
        etNickname= findViewById(R.id.etNickname);
        etMail= findViewById(R.id.etMail);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMethod();
            }
        });
    }

    public void registerMethod()
    {
       String name, weight, height, birthday, nickname, mail;
       name=etName.getText().toString().trim();
       weight = etWeight.getText().toString().trim();
       height= etHeight.getText().toString().trim();
       birthday = etBirth.getText().toString();
       nickname= etNickname.getText().toString().trim();
       mail = etMail.getText().toString().trim();




       if(birthday.isEmpty()||weight.isEmpty()||height.isEmpty()||name.isEmpty()||nickname.isEmpty())
       {
           Toast.makeText(this,"Enter all data!", Toast.LENGTH_SHORT).show();

       }
       else
       {
            try
            {
                DatabaseUser db = new DatabaseUser(this);
                db.open();
                if(db.nickCheck(nickname)) {
                    db.createEntry(name,mail,weight,height,birthday,nickname);
                }
                else{
                    Toast.makeText(this, "Nick is already used. Try another one", Toast.LENGTH_LONG).show();
                }
                db.close();
            }
            catch(SQLException e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            SaveSharedPreferences.setUserName(this,name);
            SaveSharedPreferences.setNick(this, nickname);

           Intent dashboard = new Intent(this,com.example.periodapp.DrawerActivity.class);
           startActivity(dashboard);
       }

    }


    public void dataUrodzenia(View v)
    {

        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dataUr = dayOfMonth +"/"+"0"+(month+1) + "/"+year;
                etBirth.setText(dataUr);
            }
        }, day, month, year);
        dpd.show();
    }

}
