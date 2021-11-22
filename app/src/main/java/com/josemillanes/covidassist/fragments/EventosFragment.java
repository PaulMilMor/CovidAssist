package com.josemillanes.covidassist.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.josemillanes.covidassist.Evento;
import com.josemillanes.covidassist.EventoAdapter;
import com.josemillanes.covidassist.MyOpenHelper;
import com.josemillanes.covidassist.R;

import java.util.ArrayList;

public class EventosFragment extends Fragment {

   private ListView eventosListView;
    private ArrayList<Evento> eventos;
    private MyOpenHelper db;
    private Activity context;

    public EventosFragment(ArrayList<Evento> eventos, MyOpenHelper db, Activity context) {
        this.eventos = eventos;
        this.db = db;
        this.context = context;
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
        return layout;
    }

}
