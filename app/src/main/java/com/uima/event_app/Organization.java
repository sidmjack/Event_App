package com.uima.event_app;

/**
 * Created by sidneyjackson on 4/18/17.
 */

public class Organization {

    String id;
    String organizationName;
    String organizationAddress;
    String organizationDescription;
    String logoId;
    String email;
    String phoneNumber;
    String website;
    String facebook;
    String instagram;
    String twitter;

    /* Note: The String array must be size 11! */
    public Organization(String[] organizationInformation) {
        validate(organizationInformation);
        this.id = organizationInformation[0];
        this.organizationName = organizationInformation[1];
        this.organizationAddress = organizationInformation[2];
        this.organizationDescription = organizationInformation[3];
        this.logoId = organizationInformation[4];
        this.email = organizationInformation[5];
        this.phoneNumber = organizationInformation[6];
        this.website = organizationInformation[7];
        this.facebook = organizationInformation[8];
        this.instagram = organizationInformation[9];
        this.twitter = organizationInformation[10];
    }

    public String getOrganizationId() { return id; }
    public String getOrganizationName() { return organizationName; }
    public String getAddress() { return organizationAddress; }
    public String getDescription() { return organizationDescription; }
    public String getLogo() { return logoId; }
    public String getOrganizationEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getWebsite() { return website; }
    public String getFacebook() { return facebook; }
    public String getInstagram() { return instagram; }
    public String getTwitter() { return twitter; }

    /*The validate method can be made more extensive by returning adjusted arrays that
    * can easily be handled by the app. Here, we can convert blank input into empty strings.*/
    public void validate(String[] organizationInformation) {
        if (organizationInformation.length == 11) {
            return;
        } else {
            throw new IllegalArgumentException
                    ("Array Size of OrganizationInformation is incorrect.");
        }
    }
}
