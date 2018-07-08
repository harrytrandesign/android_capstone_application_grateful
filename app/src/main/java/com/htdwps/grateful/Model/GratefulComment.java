package com.htdwps.grateful.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HTDWPS on 7/5/18.
 */
public class GratefulComment implements Parcelable {

    private CustomUser customUser;
    private String commentText;
    private Object timestamp;
    private String commentPostKey;
    private String originalPostPushId;

    public GratefulComment() {
    }

    public GratefulComment(CustomUser customUser, String commentText, Object timestamp, String commentPostKey, String originalPostPushId) {
        this.customUser = customUser;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.commentPostKey = commentPostKey;
        this.originalPostPushId = originalPostPushId;
    }

    protected GratefulComment(Parcel in) {
        customUser = in.readParcelable(CustomUser.class.getClassLoader());
        commentText = in.readString();
        commentPostKey = in.readString();
        originalPostPushId = in.readString();
    }

    public static final Creator<GratefulComment> CREATOR = new Creator<GratefulComment>() {
        @Override
        public GratefulComment createFromParcel(Parcel in) {
            return new GratefulComment(in);
        }

        @Override
        public GratefulComment[] newArray(int size) {
            return new GratefulComment[size];
        }
    };

    public CustomUser getCustomUser() {
        return customUser;
    }

    public String getCommentText() {
        return commentText;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getCommentPostKey() {
        return commentPostKey;
    }

    public String getOriginalPostPushId() {
        return originalPostPushId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(customUser, i);
        parcel.writeString(commentText);
        parcel.writeString(commentPostKey);
        parcel.writeString(originalPostPushId);
    }
}
