package com.example.shareameal.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareameal.network.ImageLoader;
import com.example.shareameal.domain.Meal;
import com.example.shareameal.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealListAdapter extends
        RecyclerView.Adapter<MealListAdapter.MealViewHolder> {

    private final String TAG_NAME = MealListAdapter.class.getSimpleName();
    private List<Meal> allMeals;
    private final LayoutInflater mInflater;

    public MealListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MealListAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.list_item,parent,false);
        return new MealViewHolder(mItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder viewHolder, int position) {
        if (allMeals != null) {
            Meal mCurrent = allMeals.get(position);
            viewHolder.titleMeal.setText(mCurrent.getTitle());
            viewHolder.priceMeal.setText(mCurrent.getSmallFormattedDatetime() + ", "+
                    mCurrent.getCook().getCity()
                    + ", €" + mCurrent.getFormattedPrice());
            if (mCurrent.getBitmap()==null){
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public  void  run () {
//                        new ImageLoader(mCurrent).execute();
//                        viewHolder.imgMeal.setImageBitmap(mCurrent.getBitmap());
                        try {
                            java.net.URL urlConnection = new URL(mCurrent.getImageUrl());
                            HttpURLConnection connection = (HttpURLConnection) urlConnection
                                    .openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(input);
                            mCurrent.setBitmap(bitmap);
                            viewHolder.imgMeal.setImageBitmap(bitmap);
                        }
                        catch (Exception e) {
                            Log.e(TAG_NAME,e.getMessage());
                        }
                    }
                });
            } else {
                viewHolder.imgMeal.setImageBitmap(mCurrent.getBitmap());
            }

        }
        else {
            viewHolder.titleMeal.setText("Loading...");
        }
    }

    public void setData(List<Meal> items) {
        Log.d(TAG_NAME,"Data updated");
        allMeals = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (allMeals != null) {
            return allMeals.size();
        } else {
            return 0;
        }
    }

    public void performFiltering(List<Meal> unfilteredList, Boolean wantsVega, Boolean wantsVegan) {
        List<Meal> filteredList = new LinkedList<>();
        if (wantsVega && wantsVegan) {
            for (Meal meal : unfilteredList) {
                if (meal.isVega() && meal.isVegan()) filteredList.add(meal);
            }
        } else if (wantsVega) {
            for (Meal meal : unfilteredList) {
                if (meal.isVega()) filteredList.add(meal);
            }
        } else if (wantsVegan) {
            for (Meal meal : unfilteredList) {
                if (meal.isVegan()) filteredList.add(meal);
            }
        } else {
            filteredList.addAll(unfilteredList);
        }
        setData(filteredList);
    }

    class MealViewHolder extends RecyclerView.ViewHolder {

        final MealListAdapter adapter;
        public final TextView titleMeal;
        public final TextView priceMeal;
        public ImageView imgMeal;

        public MealViewHolder(View itemLayoutView, MealListAdapter adapter) {
            super(itemLayoutView);
            titleMeal = itemLayoutView.findViewById(R.id.rec_meal_title);
            priceMeal = itemLayoutView.findViewById(R.id.rec_meal_info);
            imgMeal = itemLayoutView.findViewById(R.id.rec_meal_image);
            this.adapter = adapter;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MealDetailPage.class);
                    intent.putExtra("meal",allMeals.get(getLayoutPosition()));
                    context.startActivity(intent);
                }
            });
        }

    }

}


