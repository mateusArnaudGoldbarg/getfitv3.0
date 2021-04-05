package com.example.getfitv30.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.getfitv30.R;
import com.example.getfitv30.logic.Authentication;
import com.example.getfitv30.logic.Message;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_activity);
    }

    // Registers the user into the database
    public void registerUser(View view)
    {
        // Get all data
        String name = ((EditText)findViewById(R.id.register_name_input)).getText().toString();
        String email = ((EditText)findViewById(R.id.register_email_input)).getText().toString();
        String pass = ((EditText)findViewById(R.id.register_pass_input)).getText().toString();
        String h_as_s = ((EditText)findViewById(R.id.register_height_input)).getText().toString();
        String w_as_s = ((EditText)findViewById(R.id.register_weight_input)).getText().toString();

        if(name.equals("") || email.equals("") || pass.equals("") || h_as_s.equals("") || w_as_s.equals("")){
            Message.message(RegisterActivity.this, "You must fill out all the fields!");
            return;
        }

        // Converting data
        int height = Integer.parseInt(h_as_s);
        int weight = Integer.parseInt(w_as_s);

        // Registering the user
        Authentication auth = new Authentication(RegisterActivity.this);
        long uid = auth.registerUser(name, email, pass, weight, height);

        // On success, go to the profile page
        if (uid > 0){
            Log.d("USER ID", String.valueOf(uid));

            // Saving the user id in the shared preferences
            SharedPreferences sharedPref = RegisterActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            sharedPref.edit().clear().apply();
            sharedPref.edit().putLong("uid", uid).apply();

            this.goToProfile();
        }
    }

    // Forwards the user to the profile page on a successful registration
    private void goToProfile()
    {
        Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
        RegisterActivity.this.startActivity(intent);
    }
}
