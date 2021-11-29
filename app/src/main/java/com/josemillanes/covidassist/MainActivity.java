package com.josemillanes.covidassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.josemillanes.covidassist.fragments.EventosFragment;
import com.josemillanes.covidassist.fragments.ProfileFragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String SELECTION = "SELECTION";
    private BottomNavigationView bottomNavigationView;

    private MyOpenHelper db;
    private ArrayList<Evento> eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyOpenHelper(this);
        eventos = db.getEventos();
        setupBottomMenu(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventos = db.getEventos();
        EventoAdapter eventoAdapter = new EventoAdapter(this,R.layout.evento_list_item,eventos,db);
    }

    private void setupBottomMenu(Bundle savedInstanceState) {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_home:
                   //showFragment(new EventosFragment());
                    showFragment(new EventosFragment(eventos, db,this));
                    //showFragment(PageFragment.newInstance(R.drawable.ic_baseline_home_24));
                    break;
                case R.id.action_myevents:
                    showFragment(PageFragment.newInstance(R.drawable.ic_baseline_calendar_today_24));
                    break;
                case R.id.action_history:
                    showFragment(PageFragment.newInstance(R.drawable.ic_baseline_history_24));
                    break;
                case R.id.action_profile:
                    showFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
        if(savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        } else {
            bottomNavigationView.setSelectedItemId(savedInstanceState.getInt(SELECTION));
        }
    }

    private void showFragment(Fragment frg) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_nav_enter, R.anim.bottom_nav_exit)
                .replace(R.id.container, frg)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTION, bottomNavigationView.getSelectedItemId());
    }

}