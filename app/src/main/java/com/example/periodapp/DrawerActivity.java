package com.example.periodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeToolbar);

        setContentView(R.layout.activity_drawer);
        mDrawerLayout = findViewById(R.id.drawerLayout1);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        NavigationView nv= findViewById(R.id.nav);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContents(nv);
    }

    public void selectItemDrawer(MenuItem menuItem){
        Fragment myfragment= null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.settings:
            {fragmentClass = Settings.class;
                break;}
            case R.id.calendar:
            {
                fragmentClass=Calendar.class;
                break;
            }
            default:
                fragmentClass=Settings.class;
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
