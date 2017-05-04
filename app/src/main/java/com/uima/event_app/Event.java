package com.uima.event_app;

import java.util.List;

/**
 * Created by Channing on 4/15/2017.
 */
public class Event {
    String id;
    String hostOrg;
    String name;
    String location;
    String details;
    boolean needVolunteers;
    String imgId;
    List<String> types;
    List<String> tags;
    String start_time;
    String end_time;
    Organization organization;
    String address;
    String date;

    public Event(String name, String hostOrg, String location, String details, boolean needVolunteers, String imgId, List<String> types, List<String> tags) {
        this.id = "0";
        this.name = name;
        this.hostOrg = hostOrg;
        this.location = location;
        this.details = details;
        this.needVolunteers = needVolunteers;
        this.imgId = imgId;
        this.types = types;
        this.tags = tags;
    }

    public Event(String id, String name, String hostOrg, String location, String details, boolean needVolunteers, String imgId, List<String> types, List<String> tags) {
        this.id = id;
        this.name = name;
        this.hostOrg = hostOrg;
        this.location = location;
        this.details = details;
        this.needVolunteers = needVolunteers;
        this.imgId = imgId;
        this.types = types;
        this.tags = tags;
    }

    /*eventInfo: id, name, location, address, details, start_time, end_time, date */
    public Event (String[] eventInfo, Organization org, boolean needVolunteers, List<String> types, List<String> tags, String imgID) {

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
        this.types = types;
        this.tags = tags;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getHostOrg() { return hostOrg; }
    public String getLocation() { return location; }
    public String getDetails() { return details; }
    public boolean getNeedVolunteers() { return needVolunteers; }
    public String getImgId() { return imgId; }
    public List<String> getTypes() { return types; }
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