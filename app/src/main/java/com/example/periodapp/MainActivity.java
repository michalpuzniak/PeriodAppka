package com.example.periodapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    TextView tvZacz, tvWelcome;
    boolean loginCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvZacz= findViewById(R.id.btnStart);
        tvWelcome = findViewById(R.id.tvWelcome);


        if(SaveSharedPreferences.getUserName(MainActivity.this).length()!= 0)
        {
            loginCheck=true;
            String k =SaveSharedPreferences.getUserName(this);
            tvWelcome.setText("Witaj "+k);
        }
        else
        {
            tvWelcome.setVisibility(View.GONE);
            loginCheck=false;
        }




        tvZacz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_newuser = new Intent(MainActivity.this,com.example.periodapp.NewAccount.class);
                Intent intent_olduser = new Intent(MainActivity.this, com.example.periodapp.DrawerActivity.class);
                if(loginCheck){
                    startActivity(intent_olduser);

                }
                else startActivity(intent_newuser);

            }
        });
    }
    public void newUser(View v){
        Intent intent_newuser = new Intent(MainActivity.this,com.example.periodapp.NewAccount.class);
        startActivity(intent_newuser);
    }


}
