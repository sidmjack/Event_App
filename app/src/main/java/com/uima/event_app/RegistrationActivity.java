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

/**
 * RegistrationActivity
 * This is where the user enters information.
 */
public class RegistrationActivity extends AppCompatActivity {

    /** My preferences. */
    SharedPreferences myPrefs;

    /** Firebase References */
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersReference;

    /** UI Componenets. */
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

    /**
     * Check firstname fields.
     *
     * @return True if fields filled in properly.
     */
    private boolean isNameEntered() {

        if (!nameField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    /**
     * Check email fields.
     *
     * @return True if fields filled in properly.
     */
    private boolean isEmailEntered() {

        if (!emailField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    /**
     * Check username fields.
     *
     * @return True if fields filled in properly.
     */
    private boolean isUsernameEntered() {

        if (!usernameField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    /**
     * Check password fields.
     *
     * @return True if fields filled in properly.
     */
    private boolean isPasswordEntered() {

        if (!passwordField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    /**
     * Check zipcode fields.
     *
     * @return True if fields filled in properly.
     */
    private boolean isZipcodeEntered() {

        if (!zipcodeField.getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    /**
     * Check organization fields.
     *
     * @return True if fields filled in properly.
     */
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

    /**
     * Check to make sure proper fields are entered.  If entered correctly, sign up the user.
     *
     * @param view The view.
     */
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

    /**
     * The sign up method that creates a user through Firebase.
     *
     * @param email The email of the new user.
     * @param password The password of the new user.
     */
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

    /**
     * After creating a user via authentication, actually create a new node in the database in Firebase for the user.
     *
     * @param uid The ID of the user.
     * @param firstname The firstname of the user.
     * @param email The email of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param zipcode The zipcode of the user.
     * @param isOrganization If the user is an organization.
     * @param organization Name of organization if any.
     */
    private void createUser(String uid, String firstname, String email, String username, String password,
                            String zipcode, Boolean isOrganization, String organization) {
        UserProfile user = new UserProfile(uid, firstname, email, username, zipcode, isOrganization, organization);

        usersReference.child(uid).setValue(user.toMap());
        signIn(email, password);

    }

    /**
     * After signing up and creating user, sign in the user.
     *
     * @param email The user email.
     * @param password The user password.
     */
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
