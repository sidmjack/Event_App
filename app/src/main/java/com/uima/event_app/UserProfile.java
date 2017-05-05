package com.uima.event_app;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
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
    private String website;
    private String phoneNumber;
    private String facebook;
    private String instagram;
    private String twitter;
    private Boolean isOrganizer;
    private String organizer;
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
        this.website = "";
        this.phoneNumber = "";
        this.facebook = "";
        this.instagram = "";
        this.twitter = "";
        this.isOrganizer = isOrganizer;
        this.organizer = organizer;
    }

    /** Get methods */
    public String getUid() { return uid; }
    public String getFirstname() { return firstname; }
    public String getUsername() { return username; }
    public String getZipcode() { return zipcode; }
    public String getEmail() { return email; }
    public String getWebsite() { return website; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getFacebook() { return facebook; }
    public String getInstagram() { return instagram; }
    public String getTwitter() { return twitter; }
    public Boolean getIsOrganizer() { return isOrganizer; }
    public String getOrganizer() { return organizer; }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("firstname", firstname);
        result.put("username", username);
        result.put("zipcode", zipcode);
        result.put("email", email);
        result.put("website", website);
        result.put("facebook", facebook);
        result.put("instagram", instagram);
        result.put("twitter", twitter);
        result.put("isOrganizer", isOrganizer);
        result.put("organizer", organizer);

        return result;
    }

    /*
    public void validate(String[] organizationInformation) {
        if (organizationInformation.length == 8) {
            return;
        } else {
            throw new IllegalArgumentException
                    ("Array Size of UserInformation is incorrect.");
        }
    } */
}
