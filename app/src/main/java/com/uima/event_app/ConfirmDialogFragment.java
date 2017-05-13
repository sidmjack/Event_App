package com.uima.event_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by sidneyjackson on 5/13/17.
 */

public class ConfirmDialogFragment extends DialogFragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    //private DatabaseReference myRef = database.getReference();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    //private DatabaseReference currentUserRef = database.getReference().child("users").child(currentUser.getUid());
    private String eventId;

    public ConfirmDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventId = bundle.getString("eventId", " ");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Remove from My Event Board: ")
                .setMessage("Are you sure?")
                .setNegativeButton("Nah...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("I'm Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final DatabaseReference favEventRef = database.getReference().child("users").child(currentUser.getUid()).child("favorites");
                        favEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    String eventKey = child.getValue(String.class);
                                    if (eventKey.equals(eventId)) {
                                        favEventRef.child(child.getKey()).removeValue();
                                        return;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Do Nothing
                            }
                        });
                    }
                });

        return builder.create();
    }
}