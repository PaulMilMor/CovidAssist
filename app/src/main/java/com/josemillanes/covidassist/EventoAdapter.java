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
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventoAdapter extends BaseAdapter {

    Button openDialog;
    TextView infoTv;

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
        String currentAsistentes = "Asistentes: " + eventos.get(position).getEventAttendance()+ "/" + eventos.get(position).getEventCapacity();
        boolean currentContagio = eventos.get(position).isEventContagio();

        TextView tituloText = (TextView) v.findViewById(R.id.evento_titulo);
        TextView fechaText = (TextView) v.findViewById(R.id.evento_fecha);
        TextView asistentesText = (TextView) v.findViewById(R.id.evento_asistentes);
        TextView contagioText = (TextView) v.findViewById(R.id.contagio_text);

        ImageButton opcionesButton = (ImageButton) v.findViewById(R.id.evento_opciones);
        opcionesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreActionsMenu(view, eventos.get(position));
            }
        });
        tituloText.setText(currentTitle);
        fechaText.setText(currentDate);
        asistentesText.setText(currentAsistentes);
        if(currentContagio) {
            contagioText.setVisibility(View.VISIBLE);
        } else {
            contagioText.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    private void showMoreActionsMenu(View view, Evento evento) {
        Context menuContext = this.context;
        PopupMenu optionsMenu = new PopupMenu(menuContext, view);
        optionsMenu.getMenu().add("Ver Detalles");
        if(db.hasAsistencia(evento.getEventId(),usuario.getUserId())) {
            optionsMenu.getMenu().add("Quitar Asistencia");
            if(!evento.isEventContagio()) optionsMenu.getMenu().add("Informar Contagio");
        } else {
            optionsMenu.getMenu().add("Marcar Asistencia");
        }
        if(evento.getEventCreator() == usuario.getUserId()) {
            optionsMenu.getMenu().add("Editar Evento");
            optionsMenu.getMenu().add("Eliminar Evento");
        }
        //optionsMenu.getMenuInflater().inflate(R.menu.opcion_evento_items, optionsMenu.getMenu());
        optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch(menuItem.getTitle().toString()) {
                    case "Ver Detalles":

                        Intent intentdetails = new Intent(menuContext, DetailsActivity.class);
                        intentdetails.putExtra("evento", evento);
                        menuContext.startActivity(intentdetails);
                        break;
                   case "Marcar Asistencia":
                        if(evento.getEventAttendance() < evento.getEventCapacity()) {
                            Intent intentCuestionario = new Intent(menuContext, CuestionarioActivity.class);
                            intentCuestionario.putExtra("usuario", usuario);
                            intentCuestionario.putExtra("evento",evento);
                            menuContext.startActivity(intentCuestionario);

                        } else {
                            Toast.makeText(context, "Este evento ya alcanzó su máxima capacidad", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Quitar Asistencia":
                        db.removerAsistencia(usuario.getUserId(), evento.getEventId());
                        evento.setEventAttendance(evento.getEventAttendance() - 1 );
                        notifyDataSetChanged();
                        break;
                    case "Informar Contagio":
                        //Aquí iría un dialogo por si si o por si no
                        //las siguientes lineas irian en el si
                    final Dialog dialog = new Dialog(menuContext);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.custom_dialog);
                        final EditText nameEt = dialog.findViewById(R.id.name_et);
                        final EditText ageEt = dialog.findViewById(R.id.age_et);
                        final CheckBox termsCb = dialog.findViewById(R.id.terms_cb);
                        Button submitButton = dialog.findViewById(R.id.submit_button);

                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = nameEt.getText().toString();
                                String age = ageEt.getText().toString();
                                Boolean hasAccepted = termsCb.isChecked();
                                evento.setEventContagio(true);
                                db.updateEvento(evento);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();


                        break;
                    case "Editar Evento":
                        Intent intentForm = new Intent(menuContext, CreateEventActivity.class);
                        intentForm.putExtra("evento",evento);
                        menuContext.startActivity(intentForm);
                        break;
                    case "Eliminar Evento":
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
