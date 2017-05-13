package com.uima.event_app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sidneyjackson on 4/18/17.
 */

public class ManageEventsAdapter  extends ArrayAdapter<Event> {
    int res;
    String imgID;
    String event_name;
    String event_date_time;

    public ManageEventsAdapter(Context ctx, int res, List<Event> items)  {
        super(ctx, res, items);
        this.res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout eventSelectListView;
        Event eventSelectedItem = getItem(position);

        if (convertView == null) {
            eventSelectListView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(res, eventSelectListView, true);
        } else {
            eventSelectListView = (LinearLayout) convertView;
        }

        imgID = eventSelectedItem.getImgId();
        event_name = eventSelectedItem.getName();
        event_date_time = (eventSelectedItem.getLocation() + " at " + eventSelectedItem.getStartTimeString());

        //ImageView eventSelectOrganizationLogo = (ImageView) eventSelectListView.findViewById(R.id.selected_event_organization_logo);
        TextView eventSelect_name = (TextView) eventSelectListView.findViewById(R.id.selected_event_name);
        TextView eventSelect_desc = (TextView) eventSelectListView.findViewById(R.id.selected_event_description);
        final ImageView eventBoardIconView = (ImageView) eventSelectListView.findViewById(R.id.selected_event_organization_logo);

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference sRef = storage.getReference();

        // Reference to an image file in Firebase Storage
        if (eventSelectedItem.getImgId() != null && !eventSelectedItem.getImgId().equals("")) {
            sRef.child(eventSelectedItem.getImgId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getContext()).load(uri).fit().centerCrop().into(eventBoardIconView);
                }
            });
        }

        //eventSelectOrganizationLogo.setImageResource(imgID);
        eventSelect_name.setText(event_name);
        eventSelect_desc.setText(event_date_time);

        return eventSelectListView;
    }
}
