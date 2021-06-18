package com.example.memeshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONException;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {

    private ImageView memeImageView ;
    private ProgressBar mProgressBar;
    private String currentImageUrl;

    public void downloadImage(String url)
    {
        ImageDownloader task = new ImageDownloader();
        Bitmap myImage;

        try {
            myImage = task.execute(url).get();
            memeImageView.setImageBitmap(myImage);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        catch (Exception e)
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memeImageView = findViewById(R.id.memeImageView);
        mProgressBar = findViewById(R.id.loader);

        loadMeme();
    }

    private void loadMeme()
    {
        mProgressBar.setVisibility(View.VISIBLE);

        String url ="https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("SUCCESS","Loaded..");

                        try
                        {
                            String url = response.getString("url");
                            currentImageUrl = url;
                            downloadImage(url);
                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(MainActivity.this,
                                    R.string.fail_load,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("FIALED","Not loaded");

            }
        });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }

    public void shareMeme(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT , getString(R.string.send_message) + " : " + currentImageUrl);
        Intent chooser = Intent.createChooser(intent , "Share this meme using..");
        startActivity(chooser);

    }

    public void nextMeme(View view) {
        loadMeme();
    }
}