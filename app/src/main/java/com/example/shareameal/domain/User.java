package com.example.shareameal.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User implements Parcelable {

    @PrimaryKey
    private int userID;

    private String firstName;

    private String lastName;

    private String city;

    public User(int userID, String firstName, String lastName, String city){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }

    protected User(Parcel in) {
        userID = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        city = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userID);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(city);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUserID(int id) { userID = id; }
}
