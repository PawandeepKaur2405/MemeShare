package com.example.memeshare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String , Void , Bitmap> {
    @Override
    protected Bitmap doInBackground(String... strings) {

        try
        {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect();

            InputStream in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
