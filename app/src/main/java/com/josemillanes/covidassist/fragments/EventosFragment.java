package com.josemillanes.covidassist.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josemillanes.covidassist.CreateEventActivity;
import com.josemillanes.covidassist.DetailsActivity;
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

    private final int ALL_EVENTS = 0;
    private final int MY_EVENTS = 1;
    private final int HISTORY_EVENTS = 2;

    private int selected;

    private FloatingActionButton nuevoEventoButton;

    public EventosFragment(ArrayList<Evento> eventos, MyOpenHelper db,Usuario usuario, Activity context, int selected) {
        this.eventos = eventos;
        this.db = db;
        this.context = context;
        this.usuario = usuario;
        this.selected = selected;
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

        EventoAdapter eventoAdapter = new EventoAdapter(context, R.layout.evento_list_item, eventos, db,usuario);
        eventosListView.setEmptyView(layout.findViewById(R.id.empty));
        eventosListView.setAdapter(eventoAdapter);
        eventosListView.setClickable(true);
        eventosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DetailsActivity.class);
                startActivity(intent);
            }
        });

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
        switch(selected) {
            case ALL_EVENTS:
                eventos = db.getEventos();
                break;
            case MY_EVENTS:
                eventos = db.getMyEventos(usuario.getUserId());
                break;
            case HISTORY_EVENTS:
                eventos = db.getHistoryEventos(usuario.getUserId());
                break;
        }
        EventoAdapter eventoAdapter = new EventoAdapter(context,R.layout.evento_list_item,eventos,db,usuario );
        eventosListView.setAdapter(eventoAdapter);
    }

}
