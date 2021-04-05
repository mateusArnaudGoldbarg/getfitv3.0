package com.example.getfitv30.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.getfitv30.R;
import com.example.getfitv30.logic.Authentication;
import com.example.getfitv30.logic.Message;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
    }

    // Tries to log in a user, shows a toast on unsuccessful login
    public void loginUser(View view)
    {
        // Get user data
        String email = ((EditText)findViewById(R.id.login_email_input)).getText().toString();
        String pass = ((EditText)findViewById(R.id.login_pass_input)).getText().toString();

        if(email.equals("") || pass.equals("")){
            Message.message(LoginActivity.this, "You must fill out all the fields!");
            return;
        }

        // Logging in the the user
        Authentication auth = new Authentication(LoginActivity.this);
        long uid = auth.loginUser(email, pass);

        // On success, go to the profile page
        if (uid > 0){
            // Saving the user id in the shared preferences
            SharedPreferences sharedPref = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            sharedPref.edit().putLong("uid", uid).apply();

            this.goToProfile();
        }
        else{
            Message.message(LoginActivity.this, "Incorrect username or password!");
        }
    }

    // Forwards the user to the profile page on a successful login
    private void goToProfile()
    {
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        LoginActivity.this.startActivity(intent);
    }
}
