package com.uima.event_app;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserProfile Class
 * Defines a user and serves a model when retrieving input from Firebase.
 */

public class UserProfile {

    /** Variables */
    private String uid;
    private String firstname;
    private String username;
    private String zipcode;
    private String email;
    private Boolean isOrganizer;
    private String organizer;
    private String imagePath;
    private HashMap<String, String> favorites;
    //HashMap<String, Boolean> userEventPreferences; //<Event_Attributes, Event Preferences from Settings>

    /** Empty constructor for Firebase snapshot initialization */
    public UserProfile() {}

    /** Constructor with manual fields */
    public UserProfile(String id, String firstname, String email, String username, String zipcode,
                       Boolean isOrganizer, String organizer) {
        this.uid = id;
        this.firstname = firstname;
        this.username = username;
        this.zipcode = zipcode;
        this.email = email;
        this.isOrganizer = isOrganizer;
        this.organizer = organizer;
        this.favorites = new HashMap<>();
        this.imagePath = "";
    }

    /** Get methods */
    public String getUid() { return uid; }
    public String getFirstname() { return firstname; }
    public String getUsername() { return username; }
    public String getZipcode() { return zipcode; }
    public String getEmail() { return email; }
    public Boolean getIsOrganizer() { return isOrganizer; }
    public String getOrganizer() { return organizer; }
    public String getImagePath() { return imagePath; }
    public HashMap<String, String> getFavorites() {return favorites; }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("firstname", firstname);
        result.put("username", username);
        result.put("zipcode", zipcode);
        result.put("email", email);
        result.put("isOrganizer", isOrganizer);
        result.put("organizer", organizer);
        result.put("favorites", favorites);
        result.put("imagePath", imagePath);
        return result;
    }
}
