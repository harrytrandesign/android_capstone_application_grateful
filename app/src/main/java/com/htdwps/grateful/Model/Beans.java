package com.htdwps.grateful.Model;

import java.util.ArrayList;

/**
 * Created by HTDWPS on 5/11/18.
 */
public class Beans {

    private CustomUser customUser;
    private int moodType;
    private String beanText;
    private Object timestamp;
    private ArrayList<String> tagList;
    private boolean isPublic;

    public Beans() {
    }

    public Beans(CustomUser customUser, int moodType, String beanText, Object timestamp, ArrayList<String> tagList, boolean isPublic) {
        this.customUser = customUser;
        this.moodType = moodType;
        this.beanText = beanText;
        this.timestamp = timestamp;
        this.tagList = tagList;
        this.isPublic = isPublic;
    }

    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
    }

    public void setMoodType(int moodType) {
        this.moodType = moodType;
    }

    public void setBeanText(String beanText) {
        this.beanText = beanText;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public void setTagList(ArrayList<String> tagList) {
        this.tagList = tagList;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public CustomUser getCustomUser() {
        return customUser;
    }

    public int getMoodType() {
        return moodType;
    }

    public String getBeanText() {
        return beanText;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getTagList() {
        return tagList;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
