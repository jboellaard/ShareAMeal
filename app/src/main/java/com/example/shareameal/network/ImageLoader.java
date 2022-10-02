package com.example.shareameal.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.shareameal.domain.Meal;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {
    private final String TAG_NAME = ImageLoader.class.getSimpleName();
    private final Meal meal;

    public ImageLoader(Meal meal) {
        this.meal = meal;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            java.net.URL urlConnection = new URL(meal.getImageUrl());
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Log.d(TAG_NAME,"Bitmap created from Url");
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Log.e(TAG_NAME,e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        meal.setBitmap(result);
    }
}

