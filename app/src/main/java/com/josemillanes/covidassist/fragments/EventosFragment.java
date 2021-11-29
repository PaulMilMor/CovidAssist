package com.josemillanes.covidassist.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josemillanes.covidassist.CreateEventActivity;
import com.josemillanes.covidassist.Evento;
import com.josemillanes.covidassist.EventoAdapter;
import com.josemillanes.covidassist.MyOpenHelper;
import com.josemillanes.covidassist.R;
import com.josemillanes.covidassist.Usuario;

import java.util.ArrayList;

public class EventosFragment extends Fragment {

   private ListView eventosListView;
    private ArrayList<Evento> eventos;
    private MyOpenHelper db;
    private Activity context;
    private Usuario usuario;

    private FloatingActionButton nuevoEventoButton;

    public EventosFragment(ArrayList<Evento> eventos, MyOpenHelper db,Usuario usuario, Activity context) {
        this.eventos = eventos;
        this.db = db;
        this.context = context;
        this.usuario = usuario;
    }
    public EventosFragment(ArrayList<Evento> eventos,  Activity context) {
        this.eventos = eventos;
      //  this.db = db;
        this.context = context;
    }
    public EventosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View layout = inflater.inflate(R.layout.fragment_eventos, container, false);

        eventosListView = (ListView) layout.findViewById(R.id.eventos_list);

        EventoAdapter eventoAdapter = new EventoAdapter(context, R.layout.evento_list_item, eventos, db);
        eventosListView.setEmptyView(layout.findViewById(R.id.empty));
        eventosListView.setAdapter(eventoAdapter);

        nuevoEventoButton = (FloatingActionButton) layout.findViewById(R.id.nuevo_evento_button);
        nuevoEventoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+usuario.getUserId(),Toast.LENGTH_SHORT).show();
                Intent intentForm = new Intent(context, CreateEventActivity.class);
                intentForm.putExtra("usuario", usuario);
                startActivity(intentForm);
            }
        });

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        eventos = db.getEventos();
        EventoAdapter eventoAdapter = new EventoAdapter(context,R.layout.evento_list_item,eventos,db);
        eventosListView.setAdapter(eventoAdapter);
    }

}
