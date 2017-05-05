package com.uima.event_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    SharedPreferences myPrefs;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersReference;

    EditText nameField;
    EditText emailField;
    EditText usernameField;
    EditText passwordField;
    EditText zipcodeField;
    CheckBox organizerCheckbox;
    EditText organizerField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("users");

        nameField = (EditText) findViewById(R.id.registration_name_field);
        emailField = (EditText) findViewById(R.id.registration_email_field);
        usernameField = (EditText) findViewById(R.id.registration_username_field);
        passwordField = (EditText) findViewById(R.id.registration_password_field);
        zipcodeField = (EditText) findViewById(R.id.registration_zipcode_field);
        organizerCheckbox = (CheckBox) findViewById(R.id.registration_organizer_box);
        organizerField = (EditText) findViewById(R.id.registration_organization_field);

        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

    }

    private boolean isNameEntered() {

        if (!nameField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    private boolean isEmailEntered() {

        if (!emailField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    private boolean isUsernameEntered() {

        if (!usernameField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    private boolean isPasswordEntered() {

        if (!passwordField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    private boolean isZipcodeEntered() {

        if (!zipcodeField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    private boolean isOrganizationEntered() {

        if (organizerCheckbox.isChecked()) {
            if (!organizerField.getText().toString().equals("")) {
                return true;
            } else {
                return false;
            }
        }

        return true;
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
        } else if (!isZipcodeEntered()) {
            Toast.makeText(getApplicationContext(),"Please enter zipcode!", Toast.LENGTH_SHORT).show();
        } else if (!isOrganizationEntered()) {
            Toast.makeText(getApplicationContext(),"Please enter organization!", Toast.LENGTH_SHORT).show();
        } else {

            this.signUp(emailField.getText().toString(), passwordField.getText().toString());

        }
    }


    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("RegistrationActivity", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            createUser(user.getUid(), nameField.getText().toString(), emailField.getText().toString(), usernameField.getText().toString(),
                                    passwordField.getText().toString(), zipcodeField.getText().toString(), organizerCheckbox.isChecked(),
                                    organizerField.getText().toString());
                        } else {
                            Log.w("RegistrationActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Signup failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUser(String uid, String firstname, String email, String username, String password,
                            String zipcode, Boolean isOrganization, String organization) {
        UserProfile user = new UserProfile(uid, firstname, email, username, zipcode, isOrganization, organization);

        usersReference.child(uid).setValue(user.toMap());
        signIn(email, password);

    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = myPrefs.edit();
                            editor.putBoolean("logged_in", true);
                            editor.apply();

                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(getApplicationContext(), "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }





}
