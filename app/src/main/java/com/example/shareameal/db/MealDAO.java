package com.example.shareameal.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.shareameal.domain.Meal;
import com.example.shareameal.domain.User;

import java.util.List;

@Dao
public interface MealDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMeals(List<Meal> meals);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllUsers(List<User> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeal(Meal meal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("DELETE FROM Meal")
    void deleteAllMeals();

    @Query("SELECT * FROM Meal")
    LiveData<List<Meal>> getAllMeals();

    @Query("DELETE FROM User")
    void deleteAllUsers();

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM Meal WHERE mealID = :id")
    LiveData<Meal> getMeal(int id);

    @Query("SELECT * FROM User WHERE userID = :id")
    LiveData<User> getUser(int id);

    @Query("SELECT * from User LIMIT 1")
    User[] getAnyUser();

}
