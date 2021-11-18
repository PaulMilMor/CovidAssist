package com.josemillanes.covidassist;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView usernameText;
    private TextView userEmailText;
    private ImageView userImage;
    private FirebaseConnection firebaseConnection = new FirebaseConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameText = (TextView) findViewById(R.id.usernameText);
        userEmailText = (TextView) findViewById(R.id.userEmailText);
        userImage   = (ImageView) findViewById(R.id.userImage);

        firebaseConnection.startFirestore();
        List<Usuario> userList = firebaseConnection.getUsers(usernameText, userEmailText,userImage);

       // usernameText.setText(""+userList.size());
     /*   usernameText.setText(userList.get(0).getUserName());
        userEmailText.setText(userList.get(0).getUserEmail());
        try {
            URL url = new URL(userList.get(0).getUserImg());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            userImage.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }
}