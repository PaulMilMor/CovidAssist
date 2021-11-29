package com.josemillanes.covidassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    EditText usuario, contraseña;
    Button entrar, registrar;
    MyOpenHelper.DatabaseHelper DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        usuario = (EditText) findViewById(R.id.editTextTextPersonName);
        contraseña = (EditText) findViewById(R.id.editTextTextPassword);
        registrar = (Button) findViewById(R.id.button2);
        entrar = (Button) findViewById(R.id.button);
        DbHelper = new MyOpenHelper.DatabaseHelper(this);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = usuario.getText().toString();
                String p = contraseña.getText().toString();
                if (u.equals("") && p.equals("")){
                    Toast.makeText(LogInActivity.this,"Error: Campos vacios", Toast.LENGTH_SHORT).show();
                }else if (1 == DbHelper.login(u, p)){
                    Toast.makeText(LogInActivity.this,"Correcto", Toast.LENGTH_SHORT).show();

                  // Intent intentLogin = new Intent (LogInActivity.this, MainActivity.class);
                    //startActivity(intentLogin);

                }else{
                    Toast.makeText(LogInActivity.this,"Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (LogInActivity.this ,RegistrarActivity.class);
                startActivity(i);
            }
        });

    }
    }
