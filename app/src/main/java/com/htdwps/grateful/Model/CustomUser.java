package com.htdwps.grateful.Model;

/**
 * Created by HTDWPS on 5/11/18.
 */
public class CustomUser {

    private String userid;
    private String userDisplayName;
    private String userEmail;

    public CustomUser() {
    }

    public CustomUser(String userid, String userDisplayName, String userEmail) {
        this.userid = userid;
        this.userDisplayName = userDisplayName;
        this.userEmail = userEmail;
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
}
