package com.htdwps.grateful.Model;

/**
 * Created by HTDWPS on 4/5/18.
 */
public class GratefulPost {

    private User user;
    private String userDisplayName;
    private String gratefulMessage;
    private Object timestamp;
    private String photoUrlString;

    public GratefulPost() {
        // Empty POJO;
    }

    public GratefulPost(User user, String userDisplayName, String gratefulMessage, Object timestamp, String photoUrlString) {
        this.user = user;
        this.userDisplayName = userDisplayName;
        this.gratefulMessage = gratefulMessage;
        this.timestamp = timestamp;
        this.photoUrlString = photoUrlString;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getGratefulMessage() {
        return gratefulMessage;
    }

    public void setGratefulMessage(String gratefulMessage) {
        this.gratefulMessage = gratefulMessage;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhotoUrlString() {
        return photoUrlString;
    }

    public void setPhotoUrlString(String photoUrlString) {
        this.photoUrlString = photoUrlString;
    }
}
