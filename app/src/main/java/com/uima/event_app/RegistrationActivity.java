package com.uima.event_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Space;
import android.widget.Toast;

import static android.view.View.VISIBLE;

public class RegistrationActivity extends AppCompatActivity {

    SharedPreferences myPrefs;

    CheckBox orgCheck;
    EditText orgName;
    Space space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        orgName = (EditText) findViewById(R.id.org_name);
        space = (Space) findViewById(R.id.space);

        orgCheck = (CheckBox) findViewById(R.id.registration_organizer_box);
        orgCheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(orgCheck.isChecked()){
                    System.out.println("Checked");
                    orgName.setVisibility(View.VISIBLE);
                    orgName.setEnabled(true);
                    space.setVisibility(View.GONE);
                }else{
                    System.out.println("Un-Checked");
                    orgName.setVisibility(View.GONE);
                    orgName.setEnabled(false);
                    space.setVisibility(View.VISIBLE);

                }
            }
        });

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
            editor.putBoolean("logged_in", true);
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
