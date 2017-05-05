package com.uima.event_app;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    protected Button orangeButton;
    protected Button purpleButton;

    protected FirebaseDatabase database;
    protected DatabaseReference myRef;

    protected EditText eventName;
    protected EditText eventLocation;
    protected EditText eventDetails;
    protected CheckBox needVolunteers;
    protected DatePicker eventDate;
    protected TimePicker eventStartTime;
    protected TimePicker eventEndTime;
    protected Spinner eventType;
    protected ImageView eventImage;

    private String hostOrg;

    protected ListView attributeListView;
    protected static ArrayList<String> attributeItems;
    protected static ListAttributeAdapter laAdapter;

    private Spinner categorySpinner;
    private UserProfile user;

    protected String key = "fake key";
    protected String clickType;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setTitle("Create Activity");

        orangeButton = (Button) findViewById(R.id.create_event);
        purpleButton = (Button) findViewById(R.id.cancel_event);

        database = FirebaseDatabase.getInstance();
        user = new UserProfile();

        initializeUser();

        eventName = (EditText) findViewById(R.id.create_event_name);
        eventLocation = (EditText) findViewById(R.id.create_event_location);
        eventDetails = (EditText) findViewById(R.id.create_event_details);
        needVolunteers = (CheckBox) findViewById(R.id.need_volunteers);
        eventImage = (ImageView) findViewById(R.id.create_image);
        eventDate = (DatePicker) findViewById(R.id.event_date);
        eventStartTime = (TimePicker) findViewById(R.id.event_start_time);
        eventEndTime = (TimePicker) findViewById(R.id.event_end_time);
        eventType = (Spinner) findViewById(R.id.create_event_type);
        eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                clickType = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> types = new ArrayList<String>();
        types.add("Local Culture");
        types.add("Social Activism");
        types.add("Popular Culture");
        types.add("Community Outreach");
        types.add("Education & Learning");
        types.add("Shopping & Market Events");
        types.add("Miscellaneous");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventType.setAdapter(typeAdapter);

        attributeItems = new ArrayList<String>();

        Button addTagsButton = (Button) findViewById(R.id.add_tags);

        Bundle extras = getIntent().getExtras();

        if (extras.getBoolean("duplicate", false)) {
            String eventNameStr = extras.getString("event name");
            String eventLocationStr = extras.getString("event location");
            String eventDetailsStr = extras.getString("event details");

            eventName.setText(eventNameStr);
            eventLocation.setText(eventLocationStr);
            eventDetails.setText(eventDetailsStr);
        }

        orangeButton.setText("create");
        purpleButton.setText("cancel");

        // Create and Cancel Buttons

        orangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                writeToEventDB();
                Toast.makeText(getBaseContext(), "Event Created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.create_event_type);

        // Attribute Selection List
        attributeListView = (ListView) findViewById(R.id.event_attribute_list_view);

        laAdapter = new ListAttributeAdapter(this, R.layout.check_list_item, attributeItems);
        attributeListView.setAdapter(laAdapter); // Layout File

        // Dialog for Attribute Selection List

        // setup the alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Attributes");

        // Add a Checkbox List
        String[] event_attributes = getResources().getStringArray(R.array.event_attributes);
        //boolean[] checkedItems = {true, false, false, true, false};
        boolean[] checkedItems = initializeCheck(event_attributes.length);

        for(int i = 0; i < event_attributes.length; i++) {
            if(checkedItems[i]) {
                attributeItems.add(event_attributes[i]);
            }
        }

        builder.setMultiChoiceItems(event_attributes, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void writeToEventDB() {
        String start_time = eventStartTime.getCurrentHour() + ":" + eventStartTime.getCurrentMinute();
        String end_time = eventEndTime.getCurrentHour() + ":" + eventEndTime.getCurrentMinute();
        String imgId = "22"; //eventImage.getId() + "";
        String event_date = eventDate.getMonth() + "/" + eventDate.getDayOfMonth() + "/" + eventDate.getYear();
        Event e = new Event("1", eventName.getText().toString(), hostOrg, eventLocation.getText().toString(), eventDetails.getText().toString(), needVolunteers.isChecked(), imgId, clickType, attributeItems, start_time, end_time, event_date);

        // Write a message to the database
        myRef = database.getReference().child("events").push();

        myRef.setValue(e);
        String myKey = myRef.getKey();
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

    /*
    public ArrayList<String> populateAttributeList() {

        String[] attribute_name = getResources().getStringArray(R.array.event_attributes);
        ArrayList<String> attributes = new ArrayList<String>();

        for (String atr_name : attribute_name) {
            attributes.add(atr_name);
        }

        return attributes;
    } */

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
            CheckBox attributeCheck = (CheckBox) attributeView.findViewById(R.id.attribute_check);

            attribute_name.setText(attribute);

            return attributeView;
        }
    }

    public boolean[] initializeCheck(int length) {
        ArrayList<Boolean> boolArrayList = new ArrayList<Boolean>();
        boolean temp = false;

        for (int i = 0; i < length; i++) {
            boolArrayList.add(temp);
        }

        boolean boolArray[] = new boolean[length];

        for (int i = 0; i < length; i++) {
            boolArray[i] = temp;
        }

        return boolArray;
    }

    private void initializeUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference currentUserRef = database.getReference().child("users").child(currentUser.getUid());
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserProfile.class);
                hostOrg = user.getOrganizer();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        currentUserRef.addValueEventListener(userListener);

    }

}
