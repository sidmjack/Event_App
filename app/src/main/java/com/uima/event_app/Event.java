package com.uima.event_app;

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
    private List<String> tags;
    private String start_time;
    private String end_time;
    private String date;
    private String type;
    private String latutude;
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
        this.latutude = "0";
        this.longitude = "0";
    }

    public Event(String id, String name, String hostOrg, String location, String details, boolean needVolunteers, String imgId, String type, List<String> tags, String start_time, String end_time, String date, String lat, String log) {
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
        this.date = date;
        this.latutude = lat;
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
    public List<String> getTags() { return tags; }
    public String getDate() {return date; }
    public String getStart_time() {return start_time;}
    public String getEnd_time() {return end_time;}
    public String getLatutude() {return latutude;}
    public String getLongitude() {return longitude;}


}