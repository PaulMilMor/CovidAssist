package com.josemillanes.covidassist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventoAdapter extends BaseAdapter {

    private Activity context;
    private int layout;
    private ArrayList<Evento> eventos;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private MyOpenHelper db;

    public EventoAdapter(Activity context, int layout, ArrayList<Evento> eventos, MyOpenHelper db) {
        this.context = context;
        this.layout = layout;
        this.eventos = eventos;
        this.db = db;
    }

    @Override
    public int getCount() {
        return this.eventos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.eventos.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.evento_list_item,null);
        String currentTitle = eventos.get(position).getEventTitle();
        String currentDate = dateFormat.format(eventos.get(position).getEventDate());
        String currentStatus = eventos.get(position).getEventStatus();
        String currentAsistentes = "Asistentes: " + 0 + "/" + eventos.get(position).getEventCapacity();

        TextView tituloText = (TextView) v.findViewById(R.id.evento_titulo);
        TextView fechaText = (TextView) v.findViewById(R.id.evento_fecha);
        TextView statusText = (TextView) v.findViewById(R.id.evento_estatus);
        TextView asistentesText = (TextView) v.findViewById(R.id.evento_asistentes);

        ImageButton opcionesButton = (ImageButton) v.findViewById(R.id.evento_opciones);
        opcionesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreActionsMenu(view, eventos.get(position));
            }
        });
        tituloText.setText(currentTitle);
        fechaText.setText(currentDate);
        statusText.setText(currentStatus);
        asistentesText.setText(currentAsistentes);
        return v;
    }

    private void showMoreActionsMenu(View view, Evento evento) {
        Context menuContext = this.context;
        PopupMenu optionsMenu = new PopupMenu(menuContext, view);
        optionsMenu.getMenuInflater().inflate(R.menu.opcion_evento_items, optionsMenu.getMenu());
        optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.opcion_asistencia:
                        break;
                    case R.id.opcion_asistentes:
                        break;
                    case R.id.opcion_contagio:
                        break;
                    case R.id.opcion_editar:
                        break;
                    case R.id.opcion_eliminar:
                        Toast.makeText(context, "Se eliminó el evento", Toast.LENGTH_SHORT).show();
                        db.deleteEvento(evento.getEventId());
                        eventos.remove(evento);
                        notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        optionsMenu.show();
    }

}
