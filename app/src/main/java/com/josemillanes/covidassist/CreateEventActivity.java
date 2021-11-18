package com.josemillanes.covidassist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    private EditText titleText = (EditText) findViewById(R.id.title_text);
    private EditText descriptionText = (EditText) findViewById(R.id.description_text);
    private EditText placeText = (EditText) findViewById(R.id.place_text);
    private EditText dateText = (EditText) findViewById(R.id.date_text);
    private EditText capacityText = (EditText) findViewById(R.id.capacity_text);
    Button createEventButton = (Button) findViewById(R.id.create_event_button);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Usuario> attendants = new ArrayList<>();
                Evento evento = new Evento(
                        titleText.getText().toString(),
                        placeText.getText().toString(),
                        new Date(),
                        //dateText.getText().toString(),
                        "Planeado",
                        Integer.parseInt(capacityText.getText().toString()),
                        "yo",
                        false,
                        attendants
                );
                //Aquí iría algo para manejar el evento creado y subirlo a la bd
            }
        });

    }
}