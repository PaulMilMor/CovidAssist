package com.josemillanes.covidassist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class CuestionarioActivity extends AppCompatActivity {

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
    private int pregunta3 = 0;
    private int pregunta4 = 0;

    private Button enviarCuestionarioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);
        pregunta1YesButton = findViewById(R.id.pregunta1YesButton);
        pregunta1YesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregunta1 = 160;
            }
        });
        pregunta1NoButton = findViewById(R.id.pregunta1NoButton);
        pregunta1NoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregunta1 = 0;
            }
        });

        pregunta3YesButton = findViewById(R.id.pregunta3YesButton);
        pregunta3YesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregunta3 = 280;
            }
        });

        pregunta3NoButton = findViewById(R.id.pregunta3NoButton);
        pregunta3NoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregunta3 = 0;
            }
        });

        enviarCuestionarioButton = findViewById(R.id.enviarCuestionarioButton);
        enviarCuestionarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resultado = "Verde";
                int puntuacion = pregunta1 + pregunta2 + pregunta3 + pregunta4;
                if(puntuacion < 200) {
                    resultado = "Verde";
                } else if(puntuacion < 400 && puntuacion >= 200) {
                    resultado = "Amarillo";
                } else if(puntuacion >= 400) {
                    resultado = "Rojo";
                }
                Toast.makeText(CuestionarioActivity.this, resultado, Toast.LENGTH_SHORT).show();
            }
        });

    }
}