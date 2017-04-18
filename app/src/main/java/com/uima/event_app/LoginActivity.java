package com.uima.event_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

    }

    private boolean isEmailEntered() {
        EditText emailField = (EditText) findViewById(R.id.login_email_field);

        if (!emailField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    private boolean isPasswordEntered() {
        EditText passwordField = (EditText) findViewById(R.id.login_password_field);

        if (!passwordField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }


    public void signInTapped(View view) {

        if (!isEmailEntered()) {
            Toast.makeText(getApplicationContext(),"Invalid email!", Toast.LENGTH_SHORT).show();

        } else if (!isPasswordEntered()) {
            Toast.makeText(getApplicationContext(),"Invalid password!", Toast.LENGTH_SHORT).show();

        } else {
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putBoolean("logged_in", true);
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void registerTapped(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }








}
