package com.example.periodapp.Login_Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.example.periodapp.DrawerActivity;
import com.example.periodapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity implements Serializable {

    Button signupButton;
    EditText etUsername,  etPassword,etMail, etBirthdate, etRepPass;
    Calendar c;
    DatePickerDialog dpd;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    public static final String TAG = "HALKO";




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        signupButton = findViewById(R.id.btnSignup);
        etUsername= findViewById(R.id.etLogin);
        etMail= findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);

        etBirthdate= findViewById(R.id.etBirth);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=etMail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String name = etUsername.getText().toString().trim();
                String date = etBirthdate.getText().toString().trim();
                signupFunction(mail,pass,name, date);
            }
        });
        etBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataUrodzenia(v);
            }
        });
    }

    private void signupFunction(String mail, String pass, String name, String date) {
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID=mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference= firebaseFirestore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name",name);
                            user.put("birthdate", date);
                            user.put("email", mail);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: dodoane dane uzytkownika"+ userID);
                                }
                            });
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            Intent intent= new Intent(SignUpActivity.this, DrawerActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

/*
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

    }*/


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
                etBirthdate.setText(dataUr);
            }
        }, day, month, year);
        dpd.show();
    }
}


