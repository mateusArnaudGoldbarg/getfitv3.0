package com.example.getfitv30.logic;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.getfitv30.data.DatabaseAdapter;
import com.example.getfitv30.models.UserWeights;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Profile
{
    private DatabaseAdapter db;

    public Profile(Context context)
    {
        this.db = new DatabaseAdapter(context);
    }

    // Gets the data for the current user, and returns a cursor (which contains the selected row)
    public Map<String, String> getProfileData(long uid)
    {
        Map<String, String> result = new HashMap<String, String>();

        // Querying the database for all the required information
        String name = this.db.getUserName(uid);
        int weight = this.db.getLastUserWeight(uid);
        int height = this.db.getLastUserHeight(uid);
        int cal = this.db.getTotalCalories(uid);
        float bmi = this.db.getLastUserBmi(uid);

        // Putting the results in a hashmap
        result.put("username", String.valueOf(name));
        result.put("height", String.valueOf(height));
        result.put("weight", String.valueOf(weight));
        result.put("bmi", String.valueOf(bmi));
        result.put("cal", String.valueOf(cal));

        return result;
    }

    // Inserts a new record into the database, which contains the consumed food, amount, and calories
    public void setConsumedCalories(long uid, String food_name, int amount, double kcal)
    {
        this.db.insertConsumedCalories(uid, food_name, amount, kcal);
    }

    // Inserts a new weight record into the database
    public void setNewWeight(long uid, int weight)
    {
        // Getting the last known user height
        int last_height = this.db.getLastUserHeight(uid);

        // Calculating the new body mass index
        BMICalculator bmicalc = new BMICalculator(weight, last_height);
        float bmi = bmicalc.calculateBMI();

        // Getting the current date
        String date_string = new CurrentDate().getDate();

        // Inserting the new record into the database
        this.db.insertUserData(uid, last_height, weight, bmi, date_string);
    }

    // Gets the last known weight and BMI and returns them as a string hashmap
    public Map<String, String> getLastWeightAndBmi(long uid)
    {
        Map<String, String> result = new HashMap<String, String>();

        // Getting the required data from the database
        int last_weight = this.db.getLastUserWeight(uid);
        float last_bmi = this.db.getLastUserBmi(uid);

        // Putting the data into a hashmap
        result.put("old_weight", String.valueOf(last_weight));
        result.put("old_bmi", String.valueOf(last_bmi));

        return result;
    }

    // Gets all the weight records for the current user and returns them as a list
    public ArrayList<UserWeights> getAllWeights(long uid)
    {
        return this.db.getAllWeights(uid);
    }

    // Converts the UserWeight list into a line chart entry list
    public ArrayList<Entry> convertWeightsToEntries(ArrayList<UserWeights> data)
    {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        // The for loop has 2 indexes, one to iterate through the list backwards, and one is incrementing
        // to display the chart points correctly
        for(int i = data.size() - 1, j = 0; i >= 0; i--, j++){
            Entry e = new Entry(j, (float)data.get(i).getWeight());
            entries.add(e);
        }

        return entries;
    }
}
