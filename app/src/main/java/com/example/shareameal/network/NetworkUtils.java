package com.example.shareameal.network;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String TAG_NAME = NetworkUtils.class.getSimpleName();
    private static final String MEALS_BASE_URL =  "https://shareameal-api.herokuapp.com/api/meal";

    public static String getMeal(String mealID){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String mealJSONString = null;

        try {
            Log.d(TAG_NAME,"Starting API retrieval");
            Uri builtURI = Uri.parse(MEALS_BASE_URL).buildUpon()
                    .appendPath(mealID)
                    .build();

            URL requestURL = new URL(builtURI.toString());
            Log.d(TAG_NAME,"Url for GET Request: " + builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }
            mealJSONString = builder.toString();
        } catch (IOException e) {
            Log.e(TAG_NAME,e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG_NAME,e.getMessage());
                }
            }
        }
        Log.d(TAG_NAME, "Result: " + mealJSONString);
        return mealJSONString;
    }
}
