package com.example.shareameal.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.shareameal.domain.Meal;
import com.example.shareameal.domain.User;
import com.example.shareameal.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Meal.class, User.class}, version = 6, exportSchema = false)
public abstract class MealRoomDatabase extends RoomDatabase {
    private static final String TAG_NAME = MealRoomDatabase.class.getSimpleName();
    private static final String databaseName = "meal_database";

    public abstract MealDAO mealDAO();

    private static MealRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MealRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MealRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MealRoomDatabase.class, databaseName)
                            .fallbackToDestructiveMigration()
//                            .addCallback(sRoomDatabaseCallback)
                            .build();
                    Log.d(TAG_NAME, "New instance created.");
                }
            }
        }
        new PopulateDbAsync(INSTANCE).execute();
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final String TAG_NAME = PopulateDbAsync.class.getSimpleName();
        private final MealDAO mDao;

        public PopulateDbAsync(MealRoomDatabase instance) {
            mDao = instance.mealDAO();
        }


        @Override
        protected Void doInBackground(Void... voids) {
//            if (mDao.getAnyUser().length < 1){
                ArrayList<Meal> meals = new ArrayList<>();
                ArrayList<User> users = new ArrayList<>();
                Log.d(TAG_NAME,"Get meals and users from the API");

                try {
                    String data = NetworkUtils.getMeal("");
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray itemsArray = jsonObject.getJSONArray("result");

                    int i = 0;
                    while (i < itemsArray.length() ) {
                        JSONObject json = itemsArray.getJSONObject(i);

                        try {
                            JSONObject jCook = json.getJSONObject("cook");
                            User cook = new User(jCook.getInt("id"), jCook.getString("firstName"), jCook.getString("lastName"),
                                    jCook.getString("city"));

                            users.add(cook);

                            Meal meal = new Meal(json.getInt("id"), json.getString("name"), json.getString("description")
                                    , json.getString("dateTime"), json.getInt("maxAmountOfParticipants"),
                                    json.getDouble("price"), json.getString("imageUrl"), cook.getUserID());
                            meal.setCook(cook);

                            JSONArray jAllergens = json.getJSONArray("allergenes");
                            String allergens = "";
                            for (int j=0; j<jAllergens.length()-1; j++){
                                allergens += jAllergens.get(j) + ", ";
                            }
                            if (jAllergens.length()>0) allergens+=jAllergens.get(jAllergens.length()-1);

                            meal.setAllergens(allergens);
                            meal.setActive(Boolean.parseBoolean(json.getString("isActive")));
                            meal.setVega(Boolean.parseBoolean(json.getString("isVega")));
                            meal.setVegan(Boolean.parseBoolean(json.getString("isVegan")));
                            meal.setToTakeHome(Boolean.parseBoolean(json.getString("isToTakeHome")));
                            JSONArray jParticipants = json.getJSONArray("participants");
                            meal.setSpotsLeft(meal.getMaxAmountOfParticipants()-jParticipants.length());

                            meals.add(meal);
                        } catch (Exception e) {
                            Log.e(TAG_NAME,"Could not put entities from JSONString into Database; " + e.getMessage());
                        }
                        i++;
                    }
                    mDao.insertAllUsers(users);
                    mDao.insertAllMeals(meals);
                } catch (Exception e) {
                    Log.e(TAG_NAME,e.getMessage());
                }
//            }
            return null;
        }
    }



}
