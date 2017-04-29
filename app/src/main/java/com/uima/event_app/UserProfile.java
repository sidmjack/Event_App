package com.uima.event_app;

import java.util.HashMap;

/**
 * Created by sidneyjackson on 4/29/17.
 */

public class UserProfile {

    String id;
    String userName;
    String userZipcode;
    String email;
    String phoneNumber;
    String facebook;
    String instagram;
    String twitter;
    HashMap<String, Integer> userTrends; // <Event_Attributes, Number of Events Attended with this attribute>
    HashMap<String, Boolean> userEventPreferences; //<Event_Attributes, Event Preferences from Settings>

    /* NOTE: For registration, be sure to return an empty string for fields where ethe user does not
    * input their information. */

    /* Note: The String array must be size 8! */
    public UserProfile(String[] userInformation) {
        validate(userInformation);
        this.id = userInformation[0];
        this.userName = userInformation[1];
        this.userZipcode = userInformation[2];
        this.email = userInformation[3];
        this.phoneNumber = userInformation[4];
        this.facebook = userInformation[5];
        this.instagram = userInformation[6];
        this.twitter = userInformation[7];
    }

    public String getUserId() { return id; }
    public String getUserName() { return userName; }
    public String getZipcode() { return userZipcode; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getFacebook() { return facebook; }
    public String getInstagram() { return instagram; }
    public String getTwitter() { return twitter; }

    /*The validate method can be made more extensive by returning adjusted arrays that
    * can easily be handled by the app. Here, we can convert blank input into empty strings.*/
    public void validate(String[] organizationInformation) {
        if (organizationInformation.length == 8) {
            return;
        } else {
            throw new IllegalArgumentException
                    ("Array Size of UserInformation is incorrect.");
        }
    }
}
