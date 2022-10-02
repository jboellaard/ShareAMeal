package com.example.shareameal.db;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.shareameal.domain.Meal;
import com.example.shareameal.domain.User;

import java.util.List;

public class MealRepository {
    private final String TAG_NAME = MealRepository.class.getSimpleName();
    private final MealDAO mMealDAO;

    MealRepository(Application application) {
        Log.d(TAG_NAME, "Getting the database.");
        mMealDAO = MealRoomDatabase.getDatabase(application).mealDAO();
    }

    public LiveData<Meal> getMeal(int id){
        return mMealDAO.getMeal(id);
    }

    public LiveData<List<Meal>> getAllMeals() { return mMealDAO.getAllMeals(); }

    public void insert(Meal meal) {
        new insertMealAsyncTask(mMealDAO).execute(meal);
    }

    private static class insertMealAsyncTask extends AsyncTask<Meal, Void, Void> {

        private String TAG_NAME = insertMealAsyncTask.class.getSimpleName();
        private MealDAO mAsyncTaskDao;

        insertMealAsyncTask(MealDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Meal... params) {
            Log.d(TAG_NAME,"Inserting meal into Room Database");
            mAsyncTaskDao.insertMeal(params[0]);
            return null;
        }
    }

    public void insertAllMeals(List<Meal> meals) {
//        Meal[] array = new Meal[meals.size()];
//        for (int i=0; i<meals.size(); i++){
//            array[i] = meals.get(i);
//        }
        new insertMealsAsyncTask(mMealDAO).execute(meals);
    }

    private static class insertMealsAsyncTask extends AsyncTask<List<Meal>, Void, Void> {

        private String TAG_NAME = insertMealsAsyncTask.class.getSimpleName();
        private MealDAO mAsyncTaskDao;

        insertMealsAsyncTask(MealDAO dao) {
            mAsyncTaskDao = dao;
        }

//        @Override
//        protected Void doInBackground(Meal[]... meals) {
//            Log.d(TAG_NAME,"Inserting list of meals into Room Database");
//            mAsyncTaskDao.insertAllMeals(meals[0]);
//            return null;
//        }

        @Override
        protected Void doInBackground(List<Meal>... meals) {
            Log.d(TAG_NAME,"Inserting list of meals into Room Database");
            mAsyncTaskDao.insertAllMeals(meals[0]);
            return null;
        }
    }

    public LiveData<User> getUser(int id){
        return mMealDAO.getUser(id);
    }

    public LiveData<List<User>> getAllUsers() { return mMealDAO.getAllUsers(); }

    public void insert (User user) {
        new insertUserAsyncTask(mMealDAO).execute(user);
    }

    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private String TAG_NAME = insertUserAsyncTask.class.getSimpleName();
        private MealDAO mAsyncTaskDao;

        insertUserAsyncTask(MealDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            Log.d(TAG_NAME,"Inserting user into Room Database");
            mAsyncTaskDao.insertUser(params[0]);
            return null;
        }
    }

    public void insertAllUsers(List<User> users) {
//        User[] array = new User[users.size()];
//        for (int i=0; i<users.size(); i++){
//            array[i] = users.get(i);
//        }
        new insertUsersAsyncTask(mMealDAO).execute(users);
    }

    private static class insertUsersAsyncTask extends AsyncTask<List<User>, Void, Void> {

        private String TAG_NAME = insertUsersAsyncTask.class.getSimpleName();
        private MealDAO mAsyncTaskDao;

        insertUsersAsyncTask(MealDAO dao) {
            mAsyncTaskDao = dao;
        }

//        @Override
//        protected Void doInBackground(User[]... users) {
//            Log.d(TAG_NAME,"Inserting list of users into Room Database");
//            mAsyncTaskDao.insertAllUsers(users[0]);
//            return null;
//        }

        @Override
        protected Void doInBackground(List<User>... users) {
            Log.d(TAG_NAME,"Inserting list of users into Room Database");
            mAsyncTaskDao.insertAllUsers(users[0]);
            return null;
        }
    }



}
