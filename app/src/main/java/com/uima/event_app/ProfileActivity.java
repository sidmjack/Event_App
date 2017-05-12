package com.uima.event_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences myPrefs;
    UserProfile userProfile;
    private int PICK_IMAGE_REQUEST = 1;

    TextView nameTextView;
    TextView emailTextView;
    ImageView profileImage;
    TextView zipcodeTextView;

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference currentUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = (TextView) findViewById(R.id.profile_name);
        emailTextView = (TextView) findViewById(R.id.profile_email);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        zipcodeTextView = (TextView) findViewById(R.id.profile_zip);

        final Button addImg = (Button) findViewById(R.id.profile_addImg);
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
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

    /**
     * Get snapshot of user from Firebase and populate data.
     *
     */
    private void populateProfileData() {

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                nameTextView.setText(user.getFirstname());
                emailTextView.setText(user.getEmail());
                zipcodeTextView.setText(user.getZipcode());
                if (user.getImagePath() != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference sRef = storage.getReference();
                    sRef.child(user.getImagePath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(ProfileActivity.this).load(uri).fit().centerCrop().into(profileImage);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        currentUserRef.addValueEventListener(userListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            // Stores image in firebase.
            StorageReference mStorage = FirebaseStorage.getInstance().getReference();
            final StorageReference filepath = mStorage.child("EventPhotos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    //System.out.println("File Path " + downloadUri.toString());
                    currentUserRef.child("imagePath").setValue(filepath.getPath());
                    Picasso.with(ProfileActivity.this).load(downloadUri.toString()).fit().centerCrop().into(profileImage);
                    Toast.makeText(ProfileActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}