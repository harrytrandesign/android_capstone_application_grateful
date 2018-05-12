package com.htdwps.grateful.Model;

/**
 * Created by HTDWPS on 12/12/17.
 */

public class Feedback {

    private CustomUser user;
    private String feedback;
    private Object timestamp;
    private Boolean adminread;

    public Feedback() {
    }

    public Feedback(CustomUser user, String feedback, Object timestamp, Boolean adminread) {
        this.user = user;
        this.feedback = feedback;
        this.timestamp = timestamp;
        this.adminread = adminread;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getAdminread() {
        return adminread;
    }

    public void setAdminread(Boolean adminread) {
        this.adminread = adminread;
    }
}
