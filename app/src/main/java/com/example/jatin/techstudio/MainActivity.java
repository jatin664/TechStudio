package com.example.jatin.techstudio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.example.jatin.techstudio.Categories.Gaming;
import com.example.jatin.techstudio.Categories.Home;
import com.example.jatin.techstudio.Categories.Laptops;
import com.example.jatin.techstudio.Categories.Phone;
import com.example.jatin.techstudio.Categories.TV;
import com.example.jatin.techstudio.Categories.Tablet;

import im.delight.apprater.AppRater;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Crashlytics.log(Log.DEBUG, "Main Activity", "Crash in onCreate method");

        if (!isNetworkAvailable()) {
            Snackbar.make(findViewById(android.R.id.content),"Internet Connection is not available",Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(android.R.id.content),"Switching to Offline Mode...",Snackbar.LENGTH_LONG).show();
                }
            },Snackbar.LENGTH_LONG+1000);
        }

        //Loading home fragment
        Fragment fragment=new Home();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_id,fragment);
        fragmentTransaction.commit();

        //App Rater
        AppRater appRater=new AppRater(this);
        appRater.setDaysBeforePrompt(4);
        appRater.setLaunchesBeforePrompt(10);
        appRater.setPhrases(getString(R.string.RateThisApp),getString(R.string.RateThisAppExp),"Rate Now","Later","Never");
        appRater.show();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Load home fragmentation
                    Fragment fragment=new Home();
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frameLayout_id,fragment);
                    fragmentTransaction.commit();
                }
            },280);


        } else if (id == R.id.nav_phone) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Load Phone Fragmentation
                    Fragment fragment=new Phone();
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frameLayout_id,fragment);
                    fragmentTransaction.commit();
                }
            },280);


        } else if (id == R.id.nav_laptop) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Load Laptop Fragmentation
                    Fragment fragment=new Laptops();
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frameLayout_id,fragment);
                    fragmentTransaction.commit();
                }
            },280);


        } else if (id == R.id.nav_tv) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Load TV Fragmentation
                    Fragment fragment=new TV();
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frameLayout_id,fragment);
                    fragmentTransaction.commit();
                }
            },280);


        } else if (id == R.id.nav_tablets) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Load Tablets Fragmentation
                    Fragment fragment=new Tablet();
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frameLayout_id,fragment);
                    fragmentTransaction.commit();
                }
            },280);


        } else if (id == R.id.nav_gaming) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Load Gaming Fragmentation
                    Fragment fragment=new Gaming();
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frameLayout_id,fragment);
                    fragmentTransaction.commit();
                }
            },280);


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //method for internet connection...
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
