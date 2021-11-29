package com.josemillanes.covidassist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static Bitmap getImagen (byte[] image){
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int buffersize=1024;
        byte[] buffer = new byte[buffersize];
        int len=0;
        while((len=inputStream.read(buffer)) != -1){
            stream.write(buffer,0,len);
        }
        return stream.toByteArray();
    }
}
