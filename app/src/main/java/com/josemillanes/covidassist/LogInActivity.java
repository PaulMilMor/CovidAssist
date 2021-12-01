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
                int[] results = new int[2];
                if (u.equals("") && p.equals("")){
                    Toast.makeText(LogInActivity.this,"Error: Campos vacios", Toast.LENGTH_SHORT).show();
                } else {
                    results = DbHelper.login(u,p);
                    if(results[0] ==1){
                        Toast.makeText(LogInActivity.this,"Correcto", Toast.LENGTH_SHORT).show();

                        Intent intentLogin = new Intent (LogInActivity.this, MainActivity.class);
                        Usuario usuario = new Usuario(results[1],u,p);
                        intentLogin.putExtra("usuario",usuario);
                        intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

                        startActivity(intentLogin);
                    } else{
                        Toast.makeText(LogInActivity.this,"Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
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
