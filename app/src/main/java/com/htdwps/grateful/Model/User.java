package com.htdwps.grateful.Model;

/**
 * Created by HTDWPS on 12/10/17.
 */

public class User {

    private String userid;
    private String userDisplayName;
    private String userEmail;
    private String userPhoto;

    public User() {
        // Empty POJO
    }

    public User(String userid, String userDisplayName, String userEmail, String userPhoto) {
        this.userid = userid;
        this.userDisplayName = userDisplayName;
        this.userEmail = userEmail;
        this.userPhoto = userPhoto;
    }

    public String getUserid() {
        return userid;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhoto() {
        return userPhoto;
    }
}
