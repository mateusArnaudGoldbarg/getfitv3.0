package com.example.getfitv30.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.getfitv30.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getfitv30.data.FetchEdamam;
import com.example.getfitv30.logic.Message;
import com.example.getfitv30.logic.Profile;

public class InsertMealActivity extends AppCompatActivity
{
    private static boolean first_press;

    private String food_name;
    private int amount;
    private int calories;

    private static TextView cal_display;
    private static Button cal_button;

    private static double kcal_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.insert_meal_activity);

        cal_display = ((TextView)findViewById(R.id.cal_consumption));
        cal_button = ((Button)findViewById(R.id.calc_cal_btn));

        // Every time we init this activity, default the first press value to true
        first_press = true;
    }

    public void insertMeal(View view)
    {
        // On first press, it calls the Edamam API, to calculate the consumed calories
        // On the second press, it inserts the records into the database

        if (first_press){
            this.food_name = ((EditText)findViewById(R.id.food_name_input)).getText().toString();
            String amount_str = ((EditText)findViewById(R.id.food_amount_inp)).getText().toString();

            // If the user did not fill out both fields, stop the method execution and display a toast
            if (food_name.equals("") || amount_str.equals("")){
                Message.message(InsertMealActivity.this, "You must fill out both fields!");
                return;
            }

            this.amount = Integer.parseInt(amount_str);

            // Disabling the button, so we cannot call the API before it returns
            cal_button.setText("Processing...");
            cal_button.setEnabled(false);

            // Fetching data from the Edamam API asynchronously
            FetchEdamam process = new FetchEdamam(this.food_name, this.amount);
            process.execute();
        }
        else{
            // This is only called on the second button press. We insert the values into the database
            Log.d("ADDING", "To the database");

            // Reading the userId from the shared preferences
            SharedPreferences sharedPref = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            long uid = sharedPref.getLong("uid", -1);

            // Inserting into the database
            Profile profile = new Profile(InsertMealActivity.this);
            profile.setConsumedCalories(uid, this.food_name, this.amount, kcal_amount);

            // Resetting the first press value
            first_press = true;

            // Redirection the user to the main page
            this.backToProfile();
        }
    }

    public static void displayCalories(double cal_amount)
    {
        cal_display.setText(String.valueOf(cal_amount) + " Kcal");

        // Enabling the button again, and changing the text
        cal_button.setText("Add");
        cal_button.setEnabled(true);

        // Only changing the first press value, when we received the data
        first_press = false;

        // Saving the value for later, so on the second press, we can insert it into the database
        kcal_amount = cal_amount;
    }

    // Back button press (top-left button), redirect to the main profile page
    public void backToProfile(View view)
    {
        first_press = true;

        Intent intent = new Intent(InsertMealActivity.this, ProfileActivity.class);
        InsertMealActivity.this.startActivity(intent);
    }

    public void backToProfile()
    {
        Intent intent = new Intent(InsertMealActivity.this, ProfileActivity.class);
        InsertMealActivity.this.startActivity(intent);
    }
}
