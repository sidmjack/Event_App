package com.uima.event_app;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.id.message;

public class CreateEventActivity extends AppCompatActivity {

    Button orangeButton;
    Button purpleButton;

    EditText eventName;
    EditText eventLocation;
    EditText eventDetails;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private EditText eventName;
    private EditText eventLocation;
    private EditText eventDetails;
    private CheckBox needVolunteers;

    private ListView attributeListView;
    protected static ArrayList<String> attributeItems;
    protected static ListAttributeAdapter laAdapter;

    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setTitle("Create Activity");

        orangeButton = (Button) findViewById(R.id.create_event);
        purpleButton = (Button) findViewById(R.id.cancel_event);
        eventName = (EditText) findViewById(R.id.create_event_name);
        eventLocation = (EditText) findViewById(R.id.create_event_location);
        eventDetails = (EditText) findViewById(R.id.create_event_details);
        database = FirebaseDatabase.getInstance();

        eventName = (EditText) findViewById(R.id.create_event_name);
        eventLocation = (EditText) findViewById(R.id.create_event_location);
        eventDetails = (EditText) findViewById(R.id.create_event_details);

        Button createButton = (Button) findViewById(R.id.create_event);
        Button cancelButton = (Button) findViewById(R.id.cancel_event);

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

        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
        // Spinner

        Spinner spinner = (Spinner) findViewById(R.id.create_event_type);

        // Attribute Selection List
        attributeListView = (ListView) findViewById(R.id.event_attribute_list_view);

        attributeItems = populateAttributeList();

        laAdapter = new ListAttributeAdapter(this, R.layout.check_list_item, attributeItems);
        attributeListView.setAdapter(laAdapter); // Layout File

        // Dialog for Attribute Selection List

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Event Attributes");

        // Add a Checkbox List
        String[] event_attributes = getResources().getStringArray(R.array.event_attributes);
        //boolean[] checkedItems = {true, false, false, true, false};
        boolean[] checkedItems = initializeCheck(event_attributes.length);
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

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


        // Create and Cancel Buttons

        createButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                writeToEventDB();
                Toast.makeText(getBaseContext(), "Event Created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Create Canceled", Toast.LENGTH_SHORT).show();
                finish();
                Toast.makeText(getBaseContext(), "Event Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle("Create Activity");
    }



    private void writeToEventDB() {
        Event e = new Event(eventName.getText().toString(), eventLocation.getText().toString(), eventDetails.getText().toString());


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

    public ArrayList<String> populateAttributeList() {

        String[] attribute_name = getResources().getStringArray(R.array.event_attributes);
        ArrayList<String> attributes = new ArrayList<String>();

        for (String atr_name : attribute_name) {
            attributes.add(atr_name);
        }

        return attributes;
    }

    public class ListAttributeAdapter extends ArrayAdapter<String> {
        int res;

        public ListAttributeAdapter(Context ctx, int res, List<String> attributes)  {
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

}
