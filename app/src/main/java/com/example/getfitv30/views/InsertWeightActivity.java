package com.example.getfitv30.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfitv30.R;
import com.example.getfitv30.logic.Message;
import com.example.getfitv30.logic.Profile;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class InsertWeightActivity extends AppCompatActivity
{
    private long uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.instert_weight_activity);

        // Reading the userId from the shared preferences
        SharedPreferences sharedPref = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        this.uid = sharedPref.getLong("uid", -1);

        Map<String, String> data = this.getLastWeightAndBmi();
        this.fillData(data);
    }

    // Gets the last know weight and bmi of the current user
    private Map<String, String> getLastWeightAndBmi()
    {
        Profile profile = new Profile(InsertWeightActivity.this);
        return profile.getLastWeightAndBmi(this.uid);
    }

    private void fillData(Map<String, String> data)
    {
        ((TextView)findViewById(R.id.old_weight)).setText(data.get("old_weight") + " kg");
        ((TextView)findViewById(R.id.old_bmi)).setText(data.get("old_bmi"));
    }

    // Tries to insert a new weight record into the database
    public void insertWeight(View view)
    {
        String new_w_string = ((EditText)findViewById(R.id.new_weight_inp)).getText().toString();

        // Checking if the required field has been filled
        if(new_w_string.equals("")){
            Message.message(InsertWeightActivity.this, "You must fill out all the fields!");
            return;
        }

        int new_weight = Integer.parseInt(new_w_string);

        // Inserting the records into the database
        Profile profile = new Profile(InsertWeightActivity.this);
        profile.setNewWeight(this.uid, new_weight);

        Message.message(InsertWeightActivity.this,"Weight Added");

        // Redirecting the user to the profile
        this.backToProfile();
    }

    // Back button press (top-left button)
    public void backToProfile(View view)
    {
        Intent intent = new Intent(InsertWeightActivity.this, ProfileActivity.class);
        InsertWeightActivity.this.startActivity(intent);
    }

    public void backToProfile()
    {
        Intent intent = new Intent(InsertWeightActivity.this, ProfileActivity.class);
        InsertWeightActivity.this.startActivity(intent);
    }
}
