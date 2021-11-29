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
    private FloatingActionButton subirFoto;
    private EditText nombreUsuario, correoUsuario, contraseñaUsuario;
    private ImageView imagenUsuario;
    Uri selectImageUri;
    MyOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        lnrLyt = findViewById(R.id.layout);
        subirFoto = (FloatingActionButton) findViewById(R.id.imagen_button);
        registrar = (Button) findViewById(R.id.registrar_button);
        nombreUsuario = (EditText) findViewById(R.id.nombre_text);
        correoUsuario = (EditText) findViewById(R.id.correo_text);
        contraseñaUsuario = (EditText) findViewById(R.id.contra_text);
        imagenUsuario = (ImageView) findViewById(R.id.imageView);
        imagenUsuario.setTag("ImagenAntigua");

        dbHelper = new MyOpenHelper(RegistrarActivity.this);

        subirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasStoragePermission(RegistrarActivity.this)) {
                    openImageChooser();
                } else {
                    ActivityCompat.requestPermissions(((AppCompatActivity) RegistrarActivity.this), new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                }
            }

            private void openImageChooser() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), SELECT_PICTURE);
            }

            private boolean hasStoragePermission(Context context) {
                int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
                return read == PackageManager.PERMISSION_GRANTED;
            }

        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreImagen = (String) imagenUsuario.getTag();

                if (nombreImagen.equals("ImagenAntigua")){
                    showMessage("Te falto escoger una imagen de perfil");
                }

                if (nombreUsuario.getText().toString().equals("") || contraseñaUsuario.getText().toString().equals("") || correoUsuario.getText().toString().equals("")) {
                    showMessage("Te falto llenar los datos ");
                }else{
                    if (guardarImagen(selectImageUri)){
                        showMessage("Bienvenido");
                        imagenUsuario.setImageURI(selectImageUri);

                    //    Intent in = new Intent (RegistrarActivity.this,MainActivity.class);
                      //  startActivity(in);
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
            InputStream stream = getContentResolver().openInputStream(selectImageUri);
            byte[] inputData = Utils.getBytes(stream);

            dbHelper.insertUsuario(nombreUsuario.getText().toString(), correoUsuario.getText().toString(),
                    contraseñaUsuario.getText().toString(), inputData);

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