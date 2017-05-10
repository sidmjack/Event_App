package com.uima.event_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences myPrefs;
    UserProfile userProfile;

    TextView nameTextView;
    TextView emailTextView;
    TextView facebookTextView;
    TextView websiteTextView;
    TextView zipcodeTextView;
    ImageView profileImage;

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference currentUserRef;

    private int PICK_IMAGE_REQUEST = 111;
    private Uri filePath;

    private static int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = (TextView) findViewById(R.id.profile_name);
        emailTextView = (TextView) findViewById(R.id.profile_email);
        facebookTextView = (TextView) findViewById(R.id.profile_fb);
        websiteTextView = (TextView) findViewById(R.id.profile_website);
        zipcodeTextView = (TextView) findViewById(R.id.profile_zip);
        profileImage = (ImageView) findViewById(R.id.profile_image);

        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Button changeImgButton = (Button) findViewById(R.id.profile_change_pic);

        changeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserRef = database.getReference().child("users").child(currentUser.getUid());

        populateProfileData();
    }

    /**
     * When log out is tapped.
     *
     * @param view The view.
     */
    public void logOutTapped(View view) {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putBoolean("logged_in", false);
        editor.apply();

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                profileImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get snapshot of user from Firebase and populate data.
     */
    private void populateProfileData() {

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                nameTextView.setText(user.getFirstname());
                emailTextView.setText(user.getEmail());
                facebookTextView.setText(user.getFacebook());
                websiteTextView.setText(user.getWebsite());
                zipcodeTextView.setText(user.getZipcode());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        currentUserRef.addValueEventListener(userListener);
    }
}

/*  Intent intent = new Intent(this, HomeActivity.class);
    intent.putExtra("finish", true);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
    startActivity(intent);
    finish();*/