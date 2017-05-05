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
    private Organization organization;
    private String address;
    private String date;
    private String key;
    private String type;

    public Event(String name, String location, String details) {
        this.name = name;
        this.hostOrg = "";
        this.location = location;
        this.details = details;
        this.needVolunteers = false;
        this.imgId = "";
        this.type = null;
        this.tags = null;
        this.key = null;
    }

    public Event(String name, String hostOrg, String location, String details, boolean needVolunteers, String imgId, String type, List<String> tags) {
        this.id = "0";
        this.name = name;
        this.hostOrg = hostOrg;
        this.location = location;
        this.details = details;
        this.needVolunteers = needVolunteers;
        this.imgId = imgId;
        this.type = type;
        this.tags = tags;
        this.key = null;
    }

    public Event(String id, String name, String hostOrg, String location, String details, boolean needVolunteers, String imgId, String type, List<String> tags) {
        this.id = id;
        this.name = name;
        this.hostOrg = hostOrg;
        this.location = location;
        this.details = details;
        this.needVolunteers = needVolunteers;
        this.imgId = imgId;
        this.type = type;
        this.tags = tags;
        this.key = null;
    }

    /*eventInfo: id, name, location, address, details, start_time, end_time, date */
    public Event (String[] eventInfo, Organization org, boolean needVolunteers, String type, List<String> tags, String imgID) {

        // Pass relevant string information.
        this.id = eventInfo[0];
        this.name= eventInfo[1];
        this.location= eventInfo[2];
        this.address= eventInfo[3];
        this.details= eventInfo[4];
        this.start_time= eventInfo[5];
        this.end_time= eventInfo[6];
        this.date= eventInfo[7];

        // Pass relevant event information.
        this.organization = org;
        this.hostOrg = org.getOrganizationName();

        // Event Background Image
        this.imgId = imgID;

        // Will this event need volunteers?
        this.needVolunteers = needVolunteers;

        // Pass event types and tags.
        this.type = type;
        this.tags = tags;
        this.key = null;
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
    public String getAddress() {return address; }
    public String getStart_time() {return start_time;}
    public String getEnd_time() {return end_time;}

    @Override
    public String toString() {
        return String.format("Name: %s, Location: %s, Details: %s", name, location, details);
    }

}