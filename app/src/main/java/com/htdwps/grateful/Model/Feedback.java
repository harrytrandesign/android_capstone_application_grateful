package com.htdwps.grateful.Model;

/**
 * Created by HTDWPS on 12/12/17.
 */

public class Feedback {

    private User user;
    private String text;
    private Object timestamp;

    public Feedback() {
        // Empty POJO
    }

    public Feedback(User user, String text, Object timestamp) {
        this.user = user;
        this.text = text;
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public Object getTimestamp() {
        return timestamp;
    }
}
