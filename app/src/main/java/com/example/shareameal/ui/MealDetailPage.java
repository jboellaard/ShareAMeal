package com.example.shareameal.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shareameal.network.ImageLoader;
import com.example.shareameal.domain.Meal;
import com.example.shareameal.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealDetailPage extends AppCompatActivity {
    private final String TAG_NAME = MealDetailPage.class.getSimpleName();
    private Meal meal;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.meal = intent.getParcelableExtra("meal");
        Log.d(TAG_NAME, "Id of meal loaded in: " + String.valueOf(this.meal.getMealID()));

        ImageView image = findViewById(R.id.meal_image);
        TextView title = findViewById(R.id.meal_title);
        TextView description = findViewById(R.id.meal_description);
        TextView price = findViewById(R.id.meal_price);
        TextView spotsLeft = findViewById(R.id.meal_spotsleft);
        TextView allergenInfo = findViewById(R.id.meal_allergeninfo);
        TextView date = findViewById(R.id.meal_date);

        TextView cookName = findViewById(R.id.cook_name);
        TextView cookCity = findViewById(R.id.cook_city);

        ImageView vega = findViewById(R.id.meal_vega);
        ImageView vegan = findViewById(R.id.meal_vegan);

        Resources res = getResources();

        if (meal.getBitmap()==null){
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public  void  run () {
                    try {
                        java.net.URL urlConnection = new URL(meal.getImageUrl());
                        HttpURLConnection connection = (HttpURLConnection) urlConnection
                                .openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(input);
                        meal.setBitmap(bitmap);
                        image.setImageBitmap(bitmap);
                    }
                    catch (Exception e) {
                        Log.e(TAG_NAME,e.getMessage());
                    }
                }
            });
        } else {
            image.setImageBitmap(meal.getBitmap());
        }
        title.setText(meal.getTitle());
        description.setText(meal.getDescription());
        price.setText(String.format(res.getString(R.string.price_full_string),meal.getFormattedPrice()));
        allergenInfo.setText(String.format(res.getString(R.string.allergeninfo_full_string),meal.getAllergens()));
        date.setText(meal.getFormattedDatetime());
        spotsLeft.setText(res.getString(R.string.spots_left)+meal.getSpotsLeft());

        cookName.setText(meal.getCook().getFirstName() + " " + meal.getCook().getLastName());
        cookCity.setText(meal.getCook().getCity());

        if (meal.isVega()) vega.setImageResource(R.drawable.ic_restricted);
        if (meal.isVegan()) vegan.setImageResource(R.drawable.ic_restricted);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
