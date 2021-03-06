package com.uima.event_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.R.id.message;
import static com.uima.event_app.R.id.event_date;

public class CreateEventActivity extends AppCompatActivity {

    protected FirebaseDatabase database;
    protected DatabaseReference myRef;

    protected int PICK_IMAGE_REQUEST = 1;

    protected EditText eventName;
    protected EditText eventLocation;
    protected CheckBox needVolunteers;
    protected EditText eventDetails;
    protected DatePicker eventDate;
    protected TimePicker eventStartTime;
    protected TimePicker eventEndTime;
    protected Spinner eventType;
    protected ImageView eventImage;
    protected TextView eventLat;
    protected TextView eventLog;

    protected String hostOrg;

    // Needed for Image Storage in Firebase Storage
    public StorageReference mStorage;
    public Uri uri;
    protected String imgReference;

    protected ListView attributeListView;
    protected static ArrayList<String> attributeItems = new ArrayList<>();
    protected static ListAttributeAdapter laAdapter;

    protected Spinner categorySpinner;
    protected UserProfile user;

    protected String key = "fake key";
    protected String clickType;

    protected List<String> types;

    private GoogleApiClient client;

    // Integrity Error Type
    int ERROR_TYPE = -1;
    String[] ErrorMessage = {"Enter Event Name", "Enter Event Location", "Enter Event Details", "Double Check Event Times", "Include an Event Image", "Enter Events in the Future (No Time Travelling Allowed)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Intent intent = getIntent();

        // Retrieves Fire base Storage Reference
        mStorage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        user = new UserProfile();

        initializeUser();

        eventName = (EditText) findViewById(R.id.create_event_name);
        eventLocation = (EditText) findViewById(R.id.create_event_location);
        eventDetails = (EditText) findViewById(R.id.create_event_details);
        needVolunteers = (CheckBox) findViewById(R.id.need_volunteers);
        eventImage = (ImageView) findViewById(R.id.create_image);
        eventDate = (DatePicker) findViewById(event_date);
        eventStartTime = (TimePicker) findViewById(R.id.event_start_time);
        eventEndTime = (TimePicker) findViewById(R.id.event_end_time);
        int temp = eventEndTime.getCurrentHour()+1;
        eventEndTime.setCurrentHour(temp);
        eventLat = (TextView) findViewById(R.id.event_latitude);
        eventLog = (TextView) findViewById(R.id.event_longitude);
        eventLat.setText(intent.getStringExtra("latitude"));
        eventLog.setText(intent.getStringExtra("longitude"));

        eventType = (Spinner) findViewById(R.id.create_event_type);

        eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clickType = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        types = new ArrayList<>();
        types.add("Local Culture");
        types.add("Social Activism");
        types.add("Popular Culture");
        types.add("Community Outreach");
        types.add("Education & Learning");
        types.add("Shopping & Market Events");
        types.add("Miscellaneous");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventType.setAdapter(typeAdapter);
        setTitle("Create New Event");

        Button createButton = (Button) findViewById(R.id.create_event);
        Button cancelButton = (Button) findViewById(R.id.cancel_event);
        Button addTagsButton = (Button) findViewById(R.id.add_tags);
        Button addImgButton = (Button) findViewById(R.id.create_add_image);

        System.out.println("Commit");
        // in case this should be a duplicate
        Bundle extras = getIntent().getExtras();

        if (extras.getBoolean("duplicate", false)) {
            String eventNameStr = extras.getString("event name");
            String eventLocationStr = extras.getString("event location");
            String eventDetailsStr = extras.getString("event details");

            eventName.setText(eventNameStr);
            eventLocation.setText(eventLocationStr);
            eventDetails.setText(eventDetailsStr);
        }

        // Attribute Selection List
        attributeListView = (ListView) findViewById(R.id.event_attribute_list_view);

        laAdapter = new ListAttributeAdapter(this, R.layout.check_list_item, attributeItems);
        attributeListView.setAdapter(laAdapter); // Layout File

        // setup the alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Attributes");

        // Add a Checkbox List
        final String[] event_attributes = getResources().getStringArray(R.array.event_attributes);
        final boolean[] checkedItems = new boolean[event_attributes.length];

        /*for(int i = 0; i < event_attributes.length; i++) {
            if(checkedItems[i]) {
                attributeItems.add(event_attributes[i]);
                System.out.println("Event Attributes" + event_attributes[i] + "\n");
            }
        }*/

        builder.setMultiChoiceItems(event_attributes, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    // Add Attribute Item!
                    attributeItems.add(event_attributes[which]);
                } else {
                    // Remove Attribute Item!
                    attributeItems.remove(event_attributes[which]);
                }
            }
        });

        // Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });
        builder.setNegativeButton("Cancel", null);


        addTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        // Create and Cancel Buttons

        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Integrity Check Here!
                if (allInfoFilled()) {
                    writeToEventDB();
                    Toast.makeText(getBaseContext(), "Event Created", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //Toast.makeText(getBaseContext(), "Fill out all event information", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "No Changes Made.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected boolean allInfoFilled() {
        Calendar c = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        c.set(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), eventStartTime.getCurrentHour(), eventStartTime.getCurrentMinute());
        long start_time = c.getTimeInMillis();
        c.set(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), eventEndTime.getCurrentHour(), eventEndTime.getCurrentMinute());
        long end_time = c.getTimeInMillis();

        if (eventName.getText().toString().equals("")) {
            ERROR_TYPE = 0; // Check if name is entered.
            Toast.makeText(getBaseContext(), ErrorMessage[ERROR_TYPE], Toast.LENGTH_SHORT).show();
            return false;
        } else if (eventLocation.getText().toString().equals("")) {
            ERROR_TYPE = 1; // Check if event location is not typed.
            Toast.makeText(getBaseContext(), ErrorMessage[ERROR_TYPE], Toast.LENGTH_SHORT).show();
            return false;
        } else if (eventDetails.getText().toString().equals("")) {
            ERROR_TYPE = 2; // Check if event details are entered.
            Toast.makeText(getBaseContext(), ErrorMessage[ERROR_TYPE], Toast.LENGTH_SHORT).show();
            return false;
        } else if (compareEventTimes() == false) {
            ERROR_TYPE = 3; // Double check your event times.
            Toast.makeText(getBaseContext(), ErrorMessage[ERROR_TYPE], Toast.LENGTH_SHORT).show();
            return false;
        } else if (imgReference == null || imgReference.equals("")) {
            ERROR_TYPE = 4; // Image not included.
            Toast.makeText(getBaseContext(), ErrorMessage[ERROR_TYPE], Toast.LENGTH_SHORT).show();
            return false;
        } else if (start_time < now.getTimeInMillis() || end_time < now.getTimeInMillis()) {
            ERROR_TYPE = 5; // Image not included.
            Toast.makeText(getBaseContext(), ErrorMessage[ERROR_TYPE], Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Create New Event");
    }

    private void writeToEventDB() {
        myRef = database.getReference().child("events").push();
        String myKey = myRef.getKey();

        String imgId = imgReference;

        // Setting the date and start/end times
        final Calendar c = Calendar.getInstance();
        c.set(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), eventStartTime.getCurrentHour(), eventStartTime.getCurrentMinute());
        long start_time = c.getTimeInMillis();
        c.set(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), eventEndTime.getCurrentHour(), eventEndTime.getCurrentMinute());
        long end_time = c.getTimeInMillis();

        HashMap<String, String> tags = new HashMap<>();
        Event e = new Event(myKey, eventName.getText().toString(), hostOrg, eventLocation.getText().toString(), eventDetails.getText().toString(), needVolunteers.isChecked(), imgId, clickType, tags, start_time, end_time, eventLat.getText().toString(), eventLog.getText().toString());
        // Write a message to the database
        myRef.setValue(e);
        populateAttributeList(myKey);
    }

    // Working Here!
    public void populateAttributeList(String key) {

       /*String[] attribute_name = getResources().getStringArray(R.array.event_attributes);
       ArrayList<String> attributes = new ArrayList<>();

       for (String atr_name : attribute_name) {
           attributes.add(atr_name);
       }*/

        final DatabaseReference myAtrRef = database.getReference().child("events").child(key).child("tags");

        final ArrayList<String> eAttributes = attributeItems;

        for(String tags: eAttributes) {
            myAtrRef.push().setValue(tags);
            System.out.println("Tags: " + tags);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            // Stores image in firebase.
            StorageReference filepath = mStorage.child("EventPhotos").child(uri.getLastPathSegment());
            imgReference = filepath.getPath();

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Picasso.with(CreateEventActivity.this).load(downloadUri).fit().centerCrop().into(eventImage);
                    Toast.makeText(CreateEventActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void readFromDB(String message) {
        // Read from the database
        myRef = database.getReference("events");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CreateEvent Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class ListAttributeAdapter extends ArrayAdapter<String> {
        int res;

        public ListAttributeAdapter(Context ctx, int res, List<String> attributes) {
            super(ctx, res, attributes);
            this.res = res;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout attributeView;
            String attribute = getItem(position);

            if (convertView == null) {
                attributeView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(res, attributeView, true);
            } else {
                attributeView = (LinearLayout) convertView;
            }

            TextView attribute_name = (TextView) attributeView.findViewById(R.id.event_attribute);

            attribute_name.setText(attribute);

            return attributeView;
        }
    }

    private void initializeUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference currentUserRef = database.getReference().child("users").child(currentUser.getUid());
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserProfile.class);
                hostOrg = user.getUid();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        currentUserRef.addValueEventListener(userListener);
    }

    // Returns number of hours and minutes (as decimals).
    public boolean compareEventTimes() {
        double start = eventStartTime.getCurrentHour() + ((eventStartTime.getCurrentMinute())/60.0);
        double end = eventEndTime.getCurrentHour() + ((eventEndTime.getCurrentMinute())/60.0);
        double duration = end-start;
        if (duration <= 0) {
            return false;
        } else {
            return true;
        }
    }




}
