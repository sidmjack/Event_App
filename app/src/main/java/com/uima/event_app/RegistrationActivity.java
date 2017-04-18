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

public class RegistrationActivity extends AppCompatActivity {

    SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

    }

    private boolean isNameEntered() {
        EditText nameField = (EditText) findViewById(R.id.registration_name_field);

        if (!nameField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    private boolean isEmailEntered() {
        EditText emailField = (EditText) findViewById(R.id.registration_email_field);

        if (!emailField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    private boolean isUsernameEntered() {
        EditText usernameField = (EditText) findViewById(R.id.registration_username_field);

        if (!usernameField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    private boolean isPasswordEntered() {
        EditText passwordField = (EditText) findViewById(R.id.registration_password_field);

        if (!passwordField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }


    public void createAccountTapped(View view) {

        if (!isNameEntered()) {
            Toast.makeText(getApplicationContext(), "Please Enter a Name!", Toast.LENGTH_SHORT).show();
        } else if (!isEmailEntered()) {
            Toast.makeText(getApplicationContext(),"Please enter email!", Toast.LENGTH_SHORT).show();
        } else if (!isUsernameEntered()) {
            Toast.makeText(getApplicationContext(),"Please enter username!", Toast.LENGTH_SHORT).show();
        } else if (!isPasswordEntered()) {
            Toast.makeText(getApplicationContext(),"Please enter password!", Toast.LENGTH_SHORT).show();

        } else  {
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
