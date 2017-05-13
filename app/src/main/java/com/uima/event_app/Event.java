package com.uima.event_app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Channing on 4/15/2017.
 */
public class Event {
    private String id;
    private String hostOrg;
    private String name;
    private String location;
    private String details;
    private boolean needVolunteers;
    private String imgId;
    private HashMap<String, String> tags; // Firebase Key, Attribute Name
    private long start_time;
    private long end_time;
    private String type;
    private String latitude;
    private String longitude;

    public Event() {
        this.name = "EJ";
        this.hostOrg = "JHU";
        this.location = "Hopkins";
        this.details = "An event";
        this.needVolunteers = false;
        this.imgId = "";
        this.type = null;
        this.tags = null;
        this.latitude = "0";
        this.longitude = "0";
    }

    // stores date and times as integers
    public Event(String id, String name, String hostOrg, String location, String details, boolean needVolunteers, String imgId, String type, HashMap<String, String> tags, long start_time, long end_time, String lat, String log) {
        this.id = id;
        this.name = name;
        this.hostOrg = hostOrg;
        this.location = location;
        this.details = details;
        this.needVolunteers = needVolunteers;
        this.imgId = imgId;
        this.type = type;
        this.tags = tags;
        this.end_time = end_time;
        this.start_time = start_time;
        this.imgId = imgId;
        this.latitude = lat;
        this.longitude = log;

    }


    public String getId() { return id; }
    public String getName() { return name; }
    public String getHostOrg() { return hostOrg; }
    public String getLocation() { return location; }
    public String getDetails() { return details; }
    public boolean getNeedVolunteers() { return needVolunteers; }
    public String getImgId() { return imgId; }
    public String getType() { return type; }
    public HashMap<String, String> getTags() { return tags; }
    /*public ArrayList<String> getEventTags() {
        ArrayList<String> eventTags = new ArrayList<>();
        for (String key : this.tags.keySet()) {
            eventTags.add(this.tags.get(key));
        }
        return eventTags;
    }*/
    public String getLatitude() {return latitude;}
    public String getLongitude() {return longitude;}
    public long getStart_time() { return start_time; }
    public long getEnd_time() { return end_time; }

    public String getStartTimeString() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(this.start_time);

        String time;

        if (c.get(Calendar.HOUR_OF_DAY) < 12) {
            time = String.format("%d:%02d AM", c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
        } else {
            time = String.format("%d:%02d PM", c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
        }

        return time;
    }

    public String getEndTimeString() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(this.end_time);

        String time;

        if (c.get(Calendar.HOUR_OF_DAY) < 12) {
            time = String.format("%d:%02d AM", c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
        } else {
            time = String.format("%d:%02d PM", c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
        }

        return time;
    }

    public String getDateString() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(start_time);

        return String.format("%d/%d/%d", c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
    }

    public String getDateString2() {

        Date d = new Date(this.start_time);
        return String.format("(%tA) %tB %te, %tY", d, d, d, d);
    }

}