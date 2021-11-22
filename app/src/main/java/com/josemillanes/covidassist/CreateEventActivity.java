package com.josemillanes.covidassist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText descriptionText;
    private EditText placeText;
    private EditText dateText;
    private EditText capacityText;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Date selectedDate;

    private MyOpenHelper db;

    private Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        db = new MyOpenHelper(this);
        createEventButton = (Button) findViewById(R.id.create_event_button);
        titleText = (EditText) findViewById(R.id.title_text);
        descriptionText = (EditText) findViewById(R.id.description_text);
        placeText = (EditText) findViewById(R.id.place_text);
        dateText = (EditText) findViewById(R.id.date_text);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int currentYear = cal.get(Calendar.YEAR);
                int currentMonth = cal.get(Calendar.MONTH);
                int currentDay = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String selectedDateText = i2 + "/" + (i1+1) + "/" + i;
                        cal.set(i,i1,i2,0,0);
                        selectedDate = cal.getTime();
                        dateText.setText(selectedDateText);
                    }
                },currentYear,currentMonth,currentDay);
                datePickerDialog.show();
            }
        });
        capacityText = (EditText) findViewById(R.id.capacity_text);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Usuario> attendants = new ArrayList<>();
                Evento evento = new Evento(
                        titleText.getText().toString(),
                        descriptionText.getText().toString(),
                        placeText.getText().toString(),
                        selectedDate,
                        "Planeado",
                        Integer.parseInt(capacityText.getText().toString()),
                        //Aquí es necesario obtener el id del usuario que crea el evento
                        1,
                        false,
                        attendants
                );
                db.insertEvento(evento);
                Toast.makeText(CreateEventActivity.this,"Se creó el evento", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

    }
}