package com.htdwps.grateful.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HTDWPS on 5/11/18.
 */
public class UserProfile implements Parcelable {

    private String userid;
    private String userDisplayName;
    private String userEmail;

    public UserProfile() {
    }

    public UserProfile(String userid, String userDisplayName, String userEmail) {
        this.userid = userid;
        this.userDisplayName = userDisplayName;
        this.userEmail = userEmail;
    }

    protected UserProfile(Parcel in) {
        userid = in.readString();
        userDisplayName = in.readString();
        userEmail = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public String getUserid() {
        return userid;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userid);
        parcel.writeString(userDisplayName);
        parcel.writeString(userEmail);
    }
}
