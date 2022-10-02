package com.example.shareameal.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareameal.domain.Meal;
import com.example.shareameal.R;
import com.example.shareameal.domain.User;
import com.example.shareameal.db.MealViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG_NAME = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MealListAdapter mAdapter;

    private LinkedList<Meal> allMeals;
    private ArrayList<User> allUsers;
    private ArrayList<Meal> mealsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addAMeal = findViewById(R.id.fab);
        addAMeal.setOnClickListener(view -> {
            Toast toast = Toast.makeText(mRecyclerView.getContext(), "Meals cannot be added yet.",
                    Toast.LENGTH_LONG);
            toast.show();
        });

        mAdapter = new MealListAdapter(this);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(mAdapter);

        CheckBox checkBoxVega = findViewById(R.id.checkBoxVega);
        CheckBox checkBoxVegan = findViewById(R.id.checkBoxVegan);
        Button filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean vega = false;
                boolean vegan = false;
                if (checkBoxVega.isChecked()) {
                    vega = true;
                }
                if (checkBoxVegan.isChecked()) {
                    vegan = true;
                }
                mAdapter.performFiltering(allMeals,vega,vegan);
                Toast.makeText(getApplicationContext(), "Results were filtered.", Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter = new MealListAdapter(this);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(mAdapter);

        if(savedInstanceState!=null) {
            mealsArrayList = savedInstanceState.getParcelableArrayList("All_meals");
            if (mealsArrayList!=null){
                Log.d(TAG_NAME,"Repopulating adapter using saved Instance.");
                allMeals = new LinkedList<>();
                allMeals.addAll(mealsArrayList);
                mAdapter.setData(allMeals);
            } else {
                loadData();
            }
        } else {
            loadData();
        }
    }

    private void loadData(){
        MealViewModel mMealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        mMealViewModel.getAllUsers().observe(this, users -> {
            allUsers = new ArrayList<>();
            allUsers.addAll(users);
            Log.i(TAG_NAME, users.size() + " users are loaded in.");
        });
        mMealViewModel.getAllMeals().observe(this, meals -> {
            if (allUsers!=null){
                allMeals = new LinkedList<>();
                allMeals.addAll(meals);
                for (Meal meal : meals) {
                    User cook = new User(meal.getCookID(),"","","");
                    for (User user : allUsers){
                        if(user.getUserID()==meal.getCookID()) cook = user;
                    }
                    meal.setCook(cook);
                }
                mAdapter.setData(allMeals);
                Toast toast = Toast.makeText(mRecyclerView.getContext(), "The amount of items loaded in: " + meals.size(),
                        Toast.LENGTH_LONG);
                toast.show();
                Log.i(TAG_NAME, meals.size() + " meals are loaded in.");
            }
        });
        mAdapter.setData(allMeals);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add items to action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle selected items from action bar
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //save items when rotating application
        if (allMeals!=null){
            Log.d(TAG_NAME,"Saving instance state");
            super.onSaveInstanceState(outState);
            mealsArrayList = new ArrayList<>();
            mealsArrayList.addAll(allMeals);
            outState.putParcelableArrayList("All_meals", mealsArrayList);
        }
    }
}
