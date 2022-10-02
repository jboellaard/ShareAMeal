package com.example.shareameal.domain;

import static androidx.room.ForeignKey.CASCADE;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.shareameal.network.ImageLoader;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "Meal")
public class Meal implements Parcelable {

    @PrimaryKey
    private int mealID;

    private String title;

    private String description;

    private String datetime;

    private boolean isActive = false;

    private boolean isVega = false;

    private boolean isVegan = false;

    private boolean isToTakeHome = false;

    private int maxAmountOfParticipants;

    @Ignore
    private User[] participants;

    private double price;

    private String imageUrl;

    private String allergens;

    @Ignore
    private User cook;

    @Ignore
    private Bitmap bitmap = null;

    private int cookID;

    private int spotsLeft;

    public Meal(int mealID, String title, String description, String datetime, int maxAmountOfParticipants, double price,
                String imageUrl, int cookID){
        this.mealID = mealID;
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.maxAmountOfParticipants = maxAmountOfParticipants;
        this.price = price;
        this.imageUrl = imageUrl;
        new ImageLoader(this).execute();
        this.cookID = cookID;
        this.participants = new User[maxAmountOfParticipants];
    }

    public Meal (){
        mealID = 0;
    }

    protected Meal(Parcel in) {
        mealID = in.readInt();
        title = in.readString();
        description = in.readString();
        datetime = in.readString();
        isActive = in.readByte() != 0;
        isVega = in.readByte() != 0;
        isVegan = in.readByte() != 0;
        isToTakeHome = in.readByte() != 0;
        maxAmountOfParticipants = in.readInt();
        price = in.readDouble();
        imageUrl = in.readString();
//        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        allergens = in.readString();
        cookID = in.readInt();
        cook = in.readParcelable(User.class.getClassLoader());
        spotsLeft = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mealID);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(datetime);
        parcel.writeByte((byte) (isActive ? 1 : 0));
        parcel.writeByte((byte) (isVega ? 1 : 0));
        parcel.writeByte((byte) (isVegan ? 1 : 0));
        parcel.writeByte((byte) (isToTakeHome ? 1 : 0));
        parcel.writeInt(maxAmountOfParticipants);
        parcel.writeDouble(price);
        parcel.writeString(imageUrl);
//        parcel.writeParcelable(bitmap,i);
        parcel.writeString(allergens);
        parcel.writeInt(cookID);
        parcel.writeParcelable(cook,i);
        parcel.writeInt(spotsLeft);
    }

    public static Creator<Meal> getCREATOR() {
        return CREATOR;
    }

    @ColumnInfo(name = "numberOfParticipants")
    private int numberOfParticipants;

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public void setCook(User cook){
        this.cook = cook;
        this.cookID = cook.getUserID();
    }

    public int getMealID() { return this.mealID; }

    public String getTitle() {
        return this.title;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getFormattedPrice(){
        DecimalFormat dfZero = new DecimalFormat("0.00");
        return dfZero.format(price);
    }

    public double getPrice(){ return this.price; }

    public boolean isActive() {
        return isActive;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedDatetime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        LocalDateTime d = ZonedDateTime.parse(this.datetime).toLocalDateTime();
        return d.format(formatter);
    }

    public String getSmallFormattedDatetime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        LocalDateTime d = ZonedDateTime.parse(this.datetime).toLocalDateTime();
        return d.format(formatter);
    }

    public String getDatetime() { return this.datetime; }

    public int getMaxAmountOfParticipants() {
        return maxAmountOfParticipants;
    }

    public User getCook() {
        return cook;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isVega() {
        return isVega;
    }

    public void setVega(boolean vega) {
        isVega = vega;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public boolean isToTakeHome() {
        return isToTakeHome;
    }

    public void setToTakeHome(boolean toTakeHome) {
        isToTakeHome = toTakeHome;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public User[] getParticipants() {
        return participants;
    }

    public void setParticipants(User[] participants) {
        this.participants = participants;
    }

    public void setNumberOfParticipants(int number) { this.numberOfParticipants = number; }

    public void setMealID(int id) {
        this.mealID = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setMaxAmountOfParticipants(int maxAmountOfParticipants) {
        this.maxAmountOfParticipants = maxAmountOfParticipants;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCookID() {
        return cookID;
    }

    public void setCookID(int cookID) {
        this.cookID = cookID;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public int getSpotsLeft() {
        return spotsLeft;
    }

    public void setSpotsLeft(int spotsLeft) {
        this.spotsLeft = spotsLeft;
    }

    public Bitmap getBitmap(){ return this.bitmap; }

    public void setBitmap(Bitmap b) { this.bitmap = b; }
}
