package com.htdwps.grateful.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by HTDWPS on 5/11/18.
 */
public class Beans implements Parcelable {

    private CustomUser customUser;
    private int moodValue;
    private String beanText;
    private Object timestamp;
    private ArrayList<String> tagList;
    private boolean isPublic; // Not changeable
    private String beanPostKey;

    public Beans() {
    }

    public Beans(CustomUser customUser, int moodValue, String beanText, Object timestamp, ArrayList<String> tagList, boolean isPublic, String beanPostKey) {
        this.customUser = customUser;
        this.moodValue = moodValue;
        this.beanText = beanText;
        this.timestamp = timestamp;
        this.tagList = tagList;
        this.isPublic = isPublic;
        this.beanPostKey = beanPostKey;
    }

    protected Beans(Parcel in) {
        moodValue = in.readInt();
        beanText = in.readString();
        tagList = in.createStringArrayList();
        isPublic = in.readByte() != 0;
        beanPostKey = in.readString();
    }

    public static final Creator<Beans> CREATOR = new Creator<Beans>() {
        @Override
        public Beans createFromParcel(Parcel in) {
            return new Beans(in);
        }

        @Override
        public Beans[] newArray(int size) {
            return new Beans[size];
        }
    };

    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
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

    public CustomUser getCustomUser() {
        return customUser;
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
        parcel.writeInt(moodValue);
        parcel.writeString(beanText);
        parcel.writeStringList(tagList);
        parcel.writeByte((byte) (isPublic ? 1 : 0));
        parcel.writeString(beanPostKey);
    }
}
