package com.example.shareameal.db;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shareameal.domain.Meal;
import com.example.shareameal.domain.User;

import java.util.List;

public class MealViewModel extends AndroidViewModel {

    private final String TAG_NAME = MealViewModel.class.getSimpleName();
    private final MealRepository mRepository;


    public MealViewModel (Application application) {
        super(application);
        mRepository = new MealRepository(application);
    }

    public LiveData<List<Meal>> getAllMeals() {
        return mRepository.getAllMeals();
    }

    public void insert(Meal meal) { mRepository.insert(meal); }

    public LiveData<Meal> getMeal(int id) {
        return mRepository.getMeal(id);
    }

    public LiveData<List<User>> getAllUsers() {
        return mRepository.getAllUsers();
    }

    public void insert(User user) { mRepository.insert(user); }

    public LiveData<User> getUser(int id) {
        return mRepository.getUser(id);
    }

}
