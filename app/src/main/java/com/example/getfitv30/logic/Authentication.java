package com.example.getfitv30.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.getfitv30.data.DatabaseAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Authentication
{
    private DatabaseAdapter db;

    public Authentication(Context context)
    {
        this.db = new DatabaseAdapter(context);
    }

    public long registerUser(String name, String email, String password, int weight, int height)
    {
        // Calculating the body mass index
        BMICalculator bmicalc = new BMICalculator(weight, height);
        float bmi = bmicalc.calculateBMI();

        // Inserting user into the users table
        long uid = this.db.insertUser(name, email, password);

        // Getting the current date
        String date_string = new CurrentDate().getDate();

        // Inserting user data into the table
        this.db.insertUserData(uid, height, weight, bmi, date_string);

        return uid;
    }

    public long loginUser(String email, String password)
    {
        long uid = this.db.loginUser(email, password);

        Log.d("USER ID", String.valueOf(uid));

        return uid;
    }
}
