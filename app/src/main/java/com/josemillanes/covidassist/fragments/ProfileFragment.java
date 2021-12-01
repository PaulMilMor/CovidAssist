package com.josemillanes.covidassist.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josemillanes.covidassist.CreateEventActivity;
import com.josemillanes.covidassist.Evento;
import com.josemillanes.covidassist.LogInActivity;
import com.josemillanes.covidassist.MainActivity;
import com.josemillanes.covidassist.MyOpenHelper;
import com.josemillanes.covidassist.R;
import com.josemillanes.covidassist.RegistrarActivity;
import com.josemillanes.covidassist.Usuario;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    private Button cerrar, eliminar;
    private FloatingActionButton editar, finalizar;
    private EditText nombreUsuario, correoUsuario, contraseñaUsuario;
    private ImageView imagenUsuario;

    private MyOpenHelper db;
    private Activity context;
    private Usuario usuario;


    public ProfileFragment() {
    }

    public ProfileFragment(MyOpenHelper db, Usuario usuario, Activity context) {
        this.db = db;
        this.context = context;
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_profile, container, false);

        int id = usuario.getUserId();


        editar = (FloatingActionButton)layout.findViewById (R.id.editar);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreUsuario.setEnabled(true);
                correoUsuario.setEnabled(true);
                contraseñaUsuario.setEnabled(true);

                finalizar.setVisibility(View.VISIBLE);
                editar.setVisibility(View.INVISIBLE);

            }
        });

        finalizar = (FloatingActionButton) layout.findViewById(R.id.finalizar);
        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreUsuario.setEnabled(false);
                correoUsuario.setEnabled(false);
                contraseñaUsuario.setEnabled(false);

                editar.setVisibility(View.VISIBLE);
                finalizar.setVisibility(View.INVISIBLE);

                db.updateUsuario(id, nombreUsuario.getText().toString(), correoUsuario.getText().toString(), contraseñaUsuario.getText().toString());

            }
        });

        cerrar = (Button) layout.findViewById(R.id.cerrarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentcerrar = new Intent(context, LogInActivity.class);
                intentcerrar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intentcerrar);
            }
        });

        eliminar= (Button) layout.findViewById(R.id.elimnar) ;
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.deleteUsuario(id);

                Intent intentForm = new Intent(context, LogInActivity.class);
                intentForm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

                startActivity(intentForm);
            }
        });

        nombreUsuario = (EditText) layout.findViewById(R.id.nombre_text);
        correoUsuario = (EditText) layout.findViewById(R.id.correo_text);
        contraseñaUsuario = (EditText) layout.findViewById(R.id.contra_text);
        imagenUsuario = (ImageView) layout.findViewById(R.id.imageView);
        nombreUsuario.setEnabled(false);
        correoUsuario.setEnabled(false);
        contraseñaUsuario.setEnabled(false);
        finalizar.setVisibility(View.INVISIBLE);

        usuario = MyOpenHelper.verUsuario(usuario.getUserId());

        if (usuario != null) {
            nombreUsuario.setText(usuario.getUserName());
            correoUsuario.setText(usuario.getUserEmail());
            contraseñaUsuario.setText(usuario.getUserPassword());
        }

        return layout;
    }
}