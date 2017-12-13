package com.htdwps.grateful.Model;

/**
 * Created by HTDWPS on 12/12/17.
 */

public class Entries {

    private User    user;
    private String  username;           // firebaseuser.getdisplayname
    private String  entryType;          // "Post" or "Journal"
    private String  postText;
    private String  journalText;        // Only available if entrytype = "Journal"
    private Object  timestamp;
    private String  entryImageUrl;

    public Entries() {
        // Empty POJO
    }

    public Entries(User user, String username, String entryType, String postText, Object timestamp, String entryImageUrl) {
        this.user = user;
        this.username = username;
        this.entryType = entryType;
        this.postText = postText;
        this.timestamp = timestamp;
        this.entryImageUrl = entryImageUrl;
    }

    public Entries(User user, String username, String entryType, String postText, String journalText, Object timestamp, String entryImageUrl) {
        this.user = user;
        this.username = username;
        this.entryType = entryType;
        this.postText = postText;
        this.journalText = journalText;
        this.timestamp = timestamp;
        this.entryImageUrl = entryImageUrl;
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getPostText() {
        return postText;
    }

    public String getJournalText() {
        return journalText;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getEntryImageUrl() {
        return entryImageUrl;
    }
}
