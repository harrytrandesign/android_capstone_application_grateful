package com.htdwps.grateful.Model;

/**
 * Created by HTDWPS on 12/10/17.
 */

public class Post {

    private User user;
    private String postText;
    private String journalText;
    private String postType;
    private Object timestamp;
    private String postImageUrl;

    public Post() {
        // Empty POJO
    }

    public Post(User user, String postText, String postType, Object timestamp, String postImageUrl) {
        this.user = user;
        this.postText = postText;
        this.postType = postType;
        this.timestamp = timestamp;
        this.postImageUrl = postImageUrl;
    }

    public Post(User user, String postText, String journalText, String postType, Object timestamp, String postImageUrl) {
        this.user = user;
        this.postText = postText;
        this.journalText = journalText;
        this.postType = postType;
        this.timestamp = timestamp;
        this.postImageUrl = postImageUrl;
    }

    public User getUser() {
        return user;
    }

    public String getPostText() {
        return postText;
    }

    public String getJournalText() {
        return journalText;
    }

    public String getPostType() {
        return postType;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }
}
