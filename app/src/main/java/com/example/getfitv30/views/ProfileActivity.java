package com.example.getfitv30.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.getfitv30.R;
import com.example.getfitv30.logic.MyDialog;
import com.example.getfitv30.logic.Profile;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity
{
    private Button information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        information = (Button) findViewById(R.id.infromation);

        setContentView(R.layout.profile_activity);

        // Reading the userId from the shared preferences
        SharedPreferences sharedPref = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        long uid = sharedPref.getLong("uid", -1);

        Map<String, String> user_data = this.getUserData(uid);
        this.fillUserData(user_data);

    }

    public void OpenDialog(View view){
        MyDialog dia = new MyDialog();
        dia.show(getSupportFragmentManager(),"asdasdasd");

    }

    // Gets the user data for a given user id
    private Map<String, String> getUserData(long uid)
    {
        Profile profile = new Profile(ProfileActivity.this);
        return profile.getProfileData(uid);
    }

    // Fills the user's data into the corresponding text fields
    private void fillUserData(Map<String, String> user_data)
    {
        ((TextView)findViewById(R.id.username)).setText(user_data.get("username"));
        ((TextView)findViewById(R.id.prof_height)).setText(user_data.get("height") + " cm");
        ((TextView)findViewById(R.id.prof_weight)).setText(user_data.get("weight") + " kg");
        ((TextView)findViewById(R.id.prof_bmi)).setText(user_data.get("bmi"));
        ((TextView)findViewById(R.id.prof_cal)).setText(user_data.get("cal") + " Kcal");
    }

    // Logs the user out, and opens the main activity
    public void logoutUser(View view)
    {
        // Clearing the user data from the shared preferences
        SharedPreferences sharedPref = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();

        // Opening the main activity
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        ProfileActivity.this.startActivity(intent);
    }

    // Opens the insert meal activity
    public void openInsertMeal(View view)
    {
        Intent intent = new Intent(ProfileActivity.this, InsertMealActivity.class);
        ProfileActivity.this.startActivity(intent);
    }

    // Opens the insert weight activity
    public void openInsertWeight(View view)
    {
        Intent intent = new Intent(ProfileActivity.this, InsertWeightActivity.class);
        ProfileActivity.this.startActivity(intent);
    }

    // Opens the history activity
    public void openHistory(View view)
    {
        Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
        ProfileActivity.this.startActivity(intent);
    }
}
