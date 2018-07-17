package com.htdwps.grateful.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by HTDWPS on 5/11/18.
 */
public class BeanPosts implements Parcelable {

    private UserProfile userProfile;
    private int moodValue;
    private String beanText;
    private Object timestamp;
    private ArrayList<String> tagList;
    private boolean isPublic; // Not changeable
    private String beanPostKey;

    public BeanPosts() {
    }

    public BeanPosts(UserProfile userProfile, int moodValue, String beanText, Object timestamp, ArrayList<String> tagList, boolean isPublic, String beanPostKey) {
        this.userProfile = userProfile;
        this.moodValue = moodValue;
        this.beanText = beanText;
        this.timestamp = timestamp;
        this.tagList = tagList;
        this.isPublic = isPublic;
        this.beanPostKey = beanPostKey;
    }

    protected BeanPosts(Parcel in) {
        userProfile = in.readParcelable(UserProfile.class.getClassLoader());
        moodValue = in.readInt();
        beanText = in.readString();
        tagList = in.createStringArrayList();
        isPublic = in.readByte() != 0;
        beanPostKey = in.readString();
    }

    public static final Creator<BeanPosts> CREATOR = new Creator<BeanPosts>() {
        @Override
        public BeanPosts createFromParcel(Parcel in) {
            return new BeanPosts(in);
        }

        @Override
        public BeanPosts[] newArray(int size) {
            return new BeanPosts[size];
        }
    };

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setMoodValue(int moodValue) {
        this.moodValue = moodValue;
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

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public int getMoodValue() {
        return moodValue;
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

    public String getBeanPostKey() {
        return beanPostKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(userProfile, i);
        parcel.writeInt(moodValue);
        parcel.writeString(beanText);
        parcel.writeStringList(tagList);
        parcel.writeByte((byte) (isPublic ? 1 : 0));
        parcel.writeString(beanPostKey);
    }
}
