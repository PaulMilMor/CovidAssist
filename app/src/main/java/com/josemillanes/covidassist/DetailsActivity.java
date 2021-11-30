package com.josemillanes.covidassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private TextView titleText;
    private TextView descriptionText;
    private TextView placeText;
    private TextView dateText;
    private TextView capacityText;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Date selectedDate;

    private Evento editedEvento;

    private MyOpenHelper db;

    private Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new MyOpenHelper(this);

        titleText = (TextView) findViewById(R.id.title_text);
        descriptionText = (TextView) findViewById(R.id.description_text);
        placeText = (TextView) findViewById(R.id.place_text);
        dateText = (TextView) findViewById(R.id.date_text);
        capacityText = (TextView) findViewById(R.id.capacity_text);

        Intent intent = getIntent();
        editedEvento = (Evento) intent.getSerializableExtra("evento");
        if(editedEvento != null) {
            titleText.setText(editedEvento.getEventTitle());
            //No existe el campo descripción en la base de datos
            descriptionText.setText(editedEvento.getEventTitle());
            placeText.setText(editedEvento.getEventPlace());
            selectedDate = editedEvento.getEventDate();
            capacityText.setText(""+editedEvento.getEventCapacity());

        }
    }
}