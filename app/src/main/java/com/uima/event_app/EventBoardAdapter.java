package com.uima.event_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sidneyjackson on 4/19/17.
 */

public class EventBoardAdapter extends ArrayAdapter<Event> {
    int res;

    public EventBoardAdapter(Context ctx, int res, List<Event> items)  {
        super(ctx, res, items);
        this.res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout eventBoardView;
        Event ebEvent = getItem(position);

        if (convertView == null) {
            eventBoardView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(res, eventBoardView, true);
        } else {
            eventBoardView = (LinearLayout) convertView;
        }

        String eventName = ebEvent.getName();
        int eventImage = Integer.parseInt(ebEvent.getImgId());
        String date = ("Date: " + ebEvent.getDate());
        String time = ("Time: " + ebEvent.getStart_time() + " - "+ ebEvent.getEnd_time());
        String desc = ebEvent.getDetails();

        TextView eventBoardName = (TextView) eventBoardView.findViewById(R.id.event_board_event_name);
        ImageView eventBoardPicture = (ImageView) eventBoardView.findViewById(R.id.event_board_header);
        TextView eventDate = (TextView) eventBoardView.findViewById(R.id.event_board_event_date);
        TextView eventTime = (TextView) eventBoardView.findViewById(R.id.event_board_event_time);
        TextView eventDesc = (TextView) eventBoardView.findViewById(R.id.selected_event_desc);

        eventBoardName.setText(eventName);
        eventBoardPicture.setImageResource(eventImage);
        eventDate.setText(date);
        eventTime.setText(time);
        eventDesc.setText(desc);

        return eventBoardView;
    }
}
