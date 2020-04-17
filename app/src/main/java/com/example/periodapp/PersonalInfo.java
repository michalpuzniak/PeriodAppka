package com.example.periodapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalInfo extends AppCompatActivity implements DialogUsername.UsernameDialog, DialogMail.MailDialog
{

    private TextView btnUsername, btnmail, btnBirthdate;
    private String htmlEdit= "<u>edit</u>";
    EditText editusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        setTheme(R.style.ThemeToolbar);
        btnBirthdate= findViewById(R.id.btnEditBirthdates);
        btnUsername= findViewById(R.id.btnEditUsername);
        btnmail = findViewById(R.id.btnEditEmail);
        editusername= findViewById(R.id.editUsername);

        btnUsername.setText(Html.fromHtml(htmlEdit));
        btnmail.setText(Html.fromHtml(htmlEdit));
        btnBirthdate.setText(Html.fromHtml(htmlEdit));


        btnUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });
        btnmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMail();
            }
        });
    }
    public void changeName()
    {
        DialogUsername dialogUsername= new DialogUsername();
        dialogUsername.show(getSupportFragmentManager(),"usernameDialog");
    }
    public void changeMail(){
        DialogMail dialogMail = new DialogMail();
        dialogMail.show(getSupportFragmentManager(),"dialogMail" );
    }

    @Override
    public void ChangeName(String username) {
        String nick = SaveSharedPreferences.getNick(this);
        DatabaseUser db = new DatabaseUser(this);
        try {
            db.open();
            db.updateEntry(nick,username,"name");
            db.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        SaveSharedPreferences.setUserName(this,username);
    }

    @Override
    public void changeMail(String mail) {
        String nick = SaveSharedPreferences.getNick(this);
        DatabaseUser db = new DatabaseUser(this);
        try {
            db.open();
            db.updateEntry(nick,mail,"mail");
            db.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}

