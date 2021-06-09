package com.example.periodapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;


import com.example.periodapp.DietMood.Diet;
import com.example.periodapp.Home_Calendar.Home;
import com.example.periodapp.Login_Signup.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DrawerActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    ChipNavigationBar chipNavigationBar;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        chipNavigationBar= findViewById(R.id.nav_bar);
        chipNavigationBar.setItemSelected(R.id.home,true);
        menu();

        // Setting name and email of user to display on header in nav bar
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        String userid= firebaseAuth.getCurrentUser().getUid();
        DocumentReference docRef = firebaseFirestore.collection("users").document(userid);

        FragmentManager fm= getSupportFragmentManager();
        Fragment main_fragment= null;
        Class fragClass;
        fragClass= Home.class;
        try {
            main_fragment=(Fragment) fragClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        fm.beginTransaction().replace(R.id.fragent_container,main_fragment).commit();

    }

    private void menu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment my_fragment = null;
                Class fragment_class;
                switch(i){
                    case R.id.home:
                        fragment_class= Home.class;
                        break;
                    case R.id.diet:
                        fragment_class= Diet.class;
                        break;
                    case R.id.profile:
                        fragment_class= Profile.class;
                        break;
                    case R.id.help:
                        fragment_class = HelpFragment.class;
                        break;
                    case R.id.logOut:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent= new Intent(DrawerActivity.this, LogInActivity.class);
                        startActivity(intent);
                        fragment_class=Home.class;
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
                try
                {
                    my_fragment= (Fragment) fragment_class.newInstance();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                FragmentManager fm= getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragent_container, my_fragment).commit();
            }
        });
    }

}
