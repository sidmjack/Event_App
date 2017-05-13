package com.uima.event_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class EditEventActivity extends CreateEventActivity {
    private String eventID, lat, log;
    private long startTime, endTime;
    DatabaseReference editRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Activity");

        Bundle extras = getIntent().getExtras();
        eventID = extras.getString("event id");
        editRef = database.getReference().child("events").child(eventID);

        Button updateButton = (Button) findViewById(R.id.create_event);
        updateButton.setText("update");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allInfoFilled()) {
                    updateEventDB();
                    Toast.makeText(getBaseContext(), "Event Updated", Toast.LENGTH_SHORT).show();
                    // TODO: update the event in question
                    finish();
                }
            }
        });

        Button addTagsButton = (Button) findViewById(R.id.add_tags);
        Button addImgButton = (Button) findViewById(R.id.create_add_image);

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

        editRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);

                eventName.setText(event.getName());
                eventLocation.setText(event.getLocation());
                eventDetails.setText(event.getDetails());
                needVolunteers.setChecked(event.getNeedVolunteers());
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(event.getStart_time());
                eventStartTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
                eventStartTime.setCurrentMinute(c.get(Calendar.MINUTE));
                c.setTimeInMillis(event.getEnd_time());
                eventEndTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
                eventEndTime.setCurrentMinute(c.get(Calendar.MINUTE));
                eventDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                imgReference = event.getImgId();
                eventType.setSelection(types.indexOf(event.getType()));
                HashMap<String, String> tags = event.getTags();

                if (tags != null && tags.size() > 0) {
                    for (int i = 0; i < event_attributes.length; i++) {
                        if (tags.values().contains(event_attributes[i])) {
                            checkedItems[i] = true;
                        } else {
                            checkedItems[i] = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle("Edit Activity");
    }

    private void updateEventDB() {
        String imgId = imgReference;

        final Calendar c = Calendar.getInstance();
        c.set(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), eventStartTime.getCurrentHour(), eventStartTime.getCurrentMinute());
        long start_time = c.getTimeInMillis();
        c.set(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), eventEndTime.getCurrentHour(), eventEndTime.getCurrentMinute());
        long end_time = c.getTimeInMillis();
        HashMap<String, String> tags = new HashMap<>();
        Event e = new Event(eventID, eventName.getText().toString(), hostOrg, eventLocation.getText().toString(), eventDetails.getText().toString(), needVolunteers.isChecked(), imgId, clickType, tags, start_time, end_time, eventLat.getText().toString(), eventLog.getText().toString());

        // Write a message to the database
        myRef = database.getReference().child("events").child(eventID);

        myRef.setValue(e);
        populateAttributeList(eventID);
    }
}
