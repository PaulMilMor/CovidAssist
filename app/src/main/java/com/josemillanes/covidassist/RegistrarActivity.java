package com.josemillanes.covidassist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;

public class RegistrarActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "RegistrarActivity";
    ConstraintLayout lnrLyt;
    private Button registrar;
    private EditText nombreUsuario, correoUsuario, contraseñaUsuario;
    private ImageView imagenUsuario;
    Uri selectImageUri;
    MyOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        lnrLyt = findViewById(R.id.layout);
        registrar = (Button) findViewById(R.id.registrar_button);
        nombreUsuario = (EditText) findViewById(R.id.nombre_text);
        correoUsuario = (EditText) findViewById(R.id.correo_text);
        contraseñaUsuario = (EditText) findViewById(R.id.contra_text);
        imagenUsuario = (ImageView) findViewById(R.id.imageView);
        imagenUsuario.setTag("ImagenAntigua");

        dbHelper = new MyOpenHelper(RegistrarActivity.this);



        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (nombreUsuario.getText().toString().equals("") || contraseñaUsuario.getText().toString().equals("") || correoUsuario.getText().toString().equals("")) {
                    showMessage("Te falto llenar los datos ");
                }else{
                    if (guardarImagen(selectImageUri)){
                        MyOpenHelper.DatabaseHelper innerHelper = new MyOpenHelper.DatabaseHelper(RegistrarActivity.this);
                        int[] results = innerHelper.login(correoUsuario.getText().toString(), contraseñaUsuario.getText().toString());
                        Usuario usuario = new Usuario(results[1],correoUsuario.getText().toString(),contraseñaUsuario.getText().toString());
                        showMessage("Bienvenido");
                        imagenUsuario.setImageURI(selectImageUri);

                        Intent in = new Intent (RegistrarActivity.this,MainActivity.class);
                        in.putExtra("usuario", usuario);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

                        startActivity(in);
                    }
            }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == SELECT_PICTURE){
                selectImageUri = data.getData();
                if (selectImageUri!=null){
                    imagenUsuario.setImageURI(selectImageUri);
                }
            }
        }
    }

    private boolean guardarImagen(Uri selectImageUri) {
        try {
            dbHelper.open();

            dbHelper.insertUsuario(nombreUsuario.getText().toString(), correoUsuario.getText().toString(),
                    contraseñaUsuario.getText().toString());

            dbHelper.close();
            return true;

        }catch (Exception e){
            dbHelper.close();
            return false;
        }
    }

    void showMessage (String message){
        Snackbar snackbar = Snackbar.make(lnrLyt, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private boolean loadImage() {
        try {
            dbHelper.open();
            byte[] bytes = dbHelper.recuperarImagen();
            dbHelper.close();
            imagenUsuario.setImageBitmap(Utils.getImagen(bytes));
            return true;
        }catch (Exception e){
            dbHelper.close();
            return false;
        }
    }

}