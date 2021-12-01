package com.josemillanes.covidassist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class CuestionarioActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton pregunta1YesButton;
    private RadioButton pregunta1NoButton;

    private CheckBox pregunta2Muscular;
    private CheckBox pregunta2Escalofrios;
    private CheckBox pregunta2Toracico;
    private CheckBox pregunta2Irritacion;
    private CheckBox pregunta2Cabeza;
    private CheckBox pregunta2Fiebre;
    private CheckBox pregunta2Insipidez;
    private CheckBox pregunta2Tos;
    private CheckBox pregunta2Garganta;
    private CheckBox pregunta2Aire;
    private CheckBox pregunta2Olfato;
    private CheckBox pregunta2Articulaciones;

    private RadioButton pregunta3YesButton;
    private RadioButton pregunta3NoButton;

    private CheckBox pregunta4Diabetes;
    private CheckBox pregunta4Presion;
    private CheckBox pregunta4Corazon;
    private CheckBox pregunta4Renal;
    private CheckBox pregunta4Pulmon;
    private CheckBox pregunta4Cancer;
    private CheckBox pregunta4Inmuno;
    private CheckBox pregunta4Vih;

    private int pregunta1 = 0;
    private int pregunta2 = 0;
    private int pregunta2Cantidad =0;
    private int pregunta3 = 0;
    private int pregunta4 = 0;
    private int pregunta4Cantidad = 0;

    private Button enviarCuestionarioButton;

    private Usuario usuario;
    private Evento evento;
    private MyOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        db = new MyOpenHelper(this);

        pregunta1YesButton = findViewById(R.id.pregunta1YesButton);
        pregunta1YesButton.setOnClickListener(this);
        pregunta1NoButton = findViewById(R.id.pregunta1NoButton);
        pregunta1NoButton.setOnClickListener(this);

        pregunta2Muscular = findViewById(R.id.pregunta2Muscular);
        pregunta2Escalofrios = findViewById(R.id.pregunta2Escalofrios);
        pregunta2Toracico = findViewById(R.id.pregunta2Toracico);
        pregunta2Irritacion = findViewById(R.id.pregunta2Irritacion);
        pregunta2Cabeza = findViewById(R.id.pregunta2Cabeza);
        pregunta2Fiebre = findViewById(R.id.pregunta2Fiebre);
        pregunta2Insipidez = findViewById(R.id.pregunta2Insipidez);
        pregunta2Tos = findViewById(R.id.pregunta2Tos);
        pregunta2Garganta = findViewById(R.id.pregunta2Garganta);
        pregunta2Aire = findViewById(R.id.pregunta2Aire);
        pregunta2Olfato = findViewById(R.id.pregunta2Olfato);
        pregunta2Articulaciones = findViewById(R.id.pregunta2Articulaciones);
        pregunta2Muscular.setOnClickListener(this);
        pregunta2Escalofrios.setOnClickListener(this);
        pregunta2Toracico.setOnClickListener(this);
        pregunta2Irritacion.setOnClickListener(this);
        pregunta2Cabeza.setOnClickListener(this);
        pregunta2Fiebre.setOnClickListener(this);
        pregunta2Insipidez.setOnClickListener(this);
        pregunta2Tos.setOnClickListener(this);
        pregunta2Garganta.setOnClickListener(this);
        pregunta2Aire.setOnClickListener(this);
        pregunta2Olfato.setOnClickListener(this);
        pregunta2Articulaciones.setOnClickListener(this);


        pregunta3YesButton = findViewById(R.id.pregunta3YesButton);
        pregunta3YesButton.setOnClickListener(this);

        pregunta3NoButton = findViewById(R.id.pregunta3NoButton);
        pregunta3NoButton.setOnClickListener(this);

        pregunta4Diabetes = findViewById(R.id.pregunta4Diabetes);
        pregunta4Presion = findViewById(R.id.pregunta4Presion);
        pregunta4Corazon = findViewById(R.id.pregunta4Corazon);
        pregunta4Renal = findViewById(R.id.pregunta4Renal);
        pregunta4Pulmon = findViewById(R.id.pregunta4Pulmon);
        pregunta4Cancer = findViewById(R.id.pregunta4Cancer);
        pregunta4Inmuno = findViewById(R.id.pregunta4Inmuno);
        pregunta4Vih = findViewById(R.id.pregunta4Vih);
        pregunta4Diabetes.setOnClickListener(this);
        pregunta4Presion.setOnClickListener(this);
        pregunta4Corazon.setOnClickListener(this);
        pregunta4Renal.setOnClickListener(this);
        pregunta4Pulmon.setOnClickListener(this);
        pregunta4Cancer.setOnClickListener(this);
        pregunta4Inmuno.setOnClickListener(this);
        pregunta4Vih.setOnClickListener(this);

        enviarCuestionarioButton = findViewById(R.id.enviarCuestionarioButton);
        enviarCuestionarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!pregunta1YesButton.isChecked() && !pregunta1NoButton.isChecked()) || (!pregunta3YesButton.isChecked() && !pregunta3NoButton.isChecked())){
                    Toast.makeText(CuestionarioActivity.this, "Por favor responda todas las preguntas", Toast.LENGTH_SHORT).show();
                } else {
                    int puntuacion = pregunta1 + pregunta2 + pregunta3 + pregunta4;
                    AlertDialog.Builder resultsDialogo = new AlertDialog.Builder(CuestionarioActivity.this);
                    resultsDialogo.setTitle("Resultados");
                    resultsDialogo.setCancelable(false);
                    if(puntuacion < 200) {
                        resultsDialogo.setMessage("Se ha aceptado su solicitud de asistir a este evento. Recuerde llevar cubrebocas y mantener su distancia.");
                        resultsDialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                asistir();
                            }
                        });
                    } else if(puntuacion < 400 && puntuacion >= 200) {
                        resultsDialogo.setMessage("Se recomienda quedarse en casa pero también puede asistir si lleva cubrebocas y mantiene su distancia. ¿Desea asistir aún así?");
                        resultsDialogo.setPositiveButton("Asistir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                asistir();
                            }
                        });
                        resultsDialogo.setNegativeButton("No Asistir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                noAsistir();
                            }
                        });
                    } else if(puntuacion >= 400) {
                        resultsDialogo.setMessage("No podemos permitir que asista a este evento. Se recomienda ponerse en cuarentena y contactar a su médico.");
                        resultsDialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                noAsistir();
                            }
                        });
                    }
                    resultsDialogo.show();
                }

              }
        });

        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        evento = (Evento) intent.getSerializableExtra("evento");

    }
    //método para cuando el usuario no asistirá al evento
    private void noAsistir() {
        onBackPressed();

    }

    //método para cuando el usuario asistirá al evento
    private void asistir() {
        db.marcarAsistencia(usuario.getUserId(), evento.getEventId());
        onBackPressed();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.pregunta1YesButton:
                pregunta1 = 160;
                break;
            case R.id.pregunta1NoButton:
                pregunta1 = 0;
                break;
            case R.id.pregunta2Escalofrios:
            case R.id.pregunta2Muscular:
            case R.id.pregunta2Toracico:
            case R.id.pregunta2Irritacion:
            case R.id.pregunta2Cabeza:
            case R.id.pregunta2Fiebre:
            case R.id.pregunta2Insipidez:
            case R.id.pregunta2Tos:
            case R.id.pregunta2Garganta:
            case R.id.pregunta2Aire:
            case R.id.pregunta2Olfato:
            case R.id.pregunta2Articulaciones:
                CheckBox checkBoxP2 = (CheckBox) view;
                if(checkBoxP2.isChecked()) {
                    if(pregunta2Cantidad < 6) {
                        pregunta2 += 40;
                    }
                    pregunta2Cantidad++;
                } else  {
                    if(pregunta2Cantidad <= 6) {
                        pregunta2 -= 40;
                    }
                    pregunta2Cantidad--;
                }
                break;
            case R.id.pregunta3YesButton:
                pregunta3 = 280;
                break;
            case R.id.pregunta3NoButton:
                pregunta3 = 0;
                break;
            case R.id.pregunta4Diabetes:
            case R.id.pregunta4Presion:
            case R.id.pregunta4Corazon:
            case R.id.pregunta4Renal:
            case R.id.pregunta4Pulmon:
            case R.id.pregunta4Cancer:
            case R.id.pregunta4Inmuno:
            case R.id.pregunta4Vih:
                CheckBox checkBoxP4 = (CheckBox) view;
                if(checkBoxP4.isChecked()) {
                    if(pregunta4Cantidad < 4) {
                        pregunta4 += 30;
                    }
                    pregunta4Cantidad++;
                } else  {
                    if(pregunta4Cantidad <= 4) {
                        pregunta4 -= 30;
                    }
                    pregunta4Cantidad--;
                }
                break;
        }
    }
}