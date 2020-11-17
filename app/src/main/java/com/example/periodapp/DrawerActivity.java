package com.example.periodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DrawerActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    TextView head_mail;
    TextView head_name;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeToolbar);
        setContentView(R.layout.activity_drawer);
        mDrawerLayout = findViewById(R.id.drawerLayout1);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        NavigationView nv= findViewById(R.id.nav);
        View headerView = nv.getHeaderView(0);
        head_mail= headerView.findViewById(R.id.headerMail);

        head_name= headerView.findViewById(R.id.headerName);
        // Setting name and email of user to display on header in nav bar
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        String userid= firebaseAuth.getCurrentUser().getUid();
        DocumentReference docRef = firebaseFirestore.collection("users").document(userid);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                head_mail.setText(value.getString("email"));
                head_name.setText(value.getString("name"));
            }
        });

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContents(nv);

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
        fm.beginTransaction().replace(R.id.framelayout,main_fragment).commit();

    }


    public void selectItemDrawer(MenuItem menuItem){
        Fragment myfragment= null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.settings: {
                fragmentClass = Home.class;
                break;
            }
            case R.id.calendar: {
                fragmentClass= Home.class;
                break;
            }
            case R.id.logOut: {
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(DrawerActivity.this, MainActivity.class);
                startActivity(intent);
                fragmentClass=Home.class;
                break;
            }
            case R.id.help: {
                fragmentClass=HelpFragment.class;
                break;
            }
            case R.id.charts:{
                fragmentClass=ChartsFragment.class;
                break;
            }
            default:
                fragmentClass= Home.class;
        }
        try
        {
            myfragment= (Fragment) fragmentClass.newInstance();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.framelayout, myfragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();

    }
    private void setupDrawerContents(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
