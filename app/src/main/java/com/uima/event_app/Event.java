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

    public String getId() { return id; }
    public String getName() { return name; }
    public String getHostOrg() { return hostOrg; }
    public String getLocation() { return location; }
    public String getDetails() { return details; }
    public boolean getNeedVolunteers() { return needVolunteers; }
    public String getImgId() { return imgId; }
    public List<String> getTypes() { return types; }
    public List<String> getTags() { return tags; }
}