package com.josemillanes.covidassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.josemillanes.covidassist.fragments.EventosFragment;
import com.josemillanes.covidassist.fragments.ProfileFragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity<eventoAdapter> extends AppCompatActivity {


    private static final String SELECTION = "SELECTION";
    private BottomNavigationView bottomNavigationView;

    private MyOpenHelper db;
    private ArrayList<Evento> all_eventos;
    private ArrayList<Evento> my_eventos;
    private ArrayList<Evento> history_eventos;
    private Usuario usuario;

    private final int ALL_EVENTS = 0;
    private final int MY_EVENTS = 1;
    private final int HISTORY_EVENTS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyOpenHelper(this);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        all_eventos = db.getEventos();
        my_eventos = db.getMyEventos(usuario.getUserId());
        history_eventos = db.getHistoryEventos(usuario.getUserId());

        if(usuario != null) {
            Toast.makeText(this, ""+usuario.getUserId(), Toast.LENGTH_SHORT).show();
        }
        setupBottomMenu(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        all_eventos = db.getEventos();
        my_eventos = db.getMyEventos(usuario.getUserId());
        history_eventos = db.getHistoryEventos(usuario.getUserId());

        EventoAdapter eventoAdapter = new EventoAdapter(this,R.layout.evento_list_item,all_eventos,db, usuario);

    }

    private void setupBottomMenu(Bundle savedInstanceState) {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_home:
                   //showFragment(new EventosFragment());
                    showFragment(new EventosFragment(all_eventos, db,usuario,this,ALL_EVENTS));
                    //showFragment(PageFragment.newInstance(R.drawable.ic_baseline_home_24));
                    break;
                case R.id.action_myevents:
                    showFragment(new EventosFragment(my_eventos, db,usuario,this,MY_EVENTS));
                    break;
                case R.id.action_history:
                    showFragment(new EventosFragment(history_eventos, db,usuario,this,HISTORY_EVENTS));

                    break;
                case R.id.action_profile:
                    showFragment(new ProfileFragment(db,usuario,this));
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