package com.josemillanes.covidassist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private Usuario usuario;

    public EventoAdapter(Activity context, int layout, ArrayList<Evento> eventos, MyOpenHelper db, Usuario usuario) {
        this.context = context;
        this.layout = layout;
        this.eventos = eventos;
        this.db = db;
        this.usuario = usuario;
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
        String currentAsistentes = "Asistentes: " + eventos.get(position).getEventAttendance()+ "/" + eventos.get(position).getEventCapacity();

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
                        if(evento.getEventAttendance() < evento.getEventCapacity()) {
                            Intent intentCuestionario = new Intent(menuContext, CuestionarioActivity.class);
                            intentCuestionario.putExtra("usuario", usuario);
                            intentCuestionario.putExtra("evento",evento);
                            menuContext.startActivity(intentCuestionario);

                        } else {
                            Toast.makeText(context, "Este evento ya alcanzó su máxima capacidad", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.opcion_asistentes:
                        break;
                    case R.id.opcion_contagio:
                        break;
                    case R.id.opcion_editar:
                        Intent intentForm = new Intent(menuContext, CreateEventActivity.class);
                        intentForm.putExtra("evento",evento);
                        menuContext.startActivity(intentForm);
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
