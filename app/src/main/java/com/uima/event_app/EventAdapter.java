package com.uima.event_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Channing on 4/15/2017.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    int res;

    public EventAdapter(Context context, int resource, List<Event> items) {
        super(context, resource, items);
        this.res = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout eventView;
        Event event = getItem(position);

        if (convertView == null) {
            eventView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(res, eventView, true);
        } else {
            eventView = (LinearLayout) convertView;
        }

        // set the values of each field in the Event list view
        TextView description = (TextView) eventView.findViewById(R.id.event_description);
        description.setText(event.getDetails());

        return eventView;
    }
}