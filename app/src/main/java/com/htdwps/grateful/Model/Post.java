package com.htdwps.grateful.Model;

/**
 * Created by HTDWPS on 12/10/17.
 */

public class Post {

    private User user;
    private String text;
    private String category;
    private Object timestamp;

    public Post() {
        // Empty POJO
    }

    public Post(User user, String text, String category, Object timestamp) {
        this.user = user;
        this.text = text;
        this.category = category;
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public Object getTimestamp() {
        return timestamp;
    }
}
