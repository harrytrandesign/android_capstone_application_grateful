package com.htdwps.grateful.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HTDWPS on 7/5/18.
 */
public class CommentBean implements Parcelable {

    private UserProfile userProfile;
    private String originalPostPushId;
    private String commentPostKey;
    private String commentText;
    private Object timestamp;

    public CommentBean() {
    }

    public CommentBean(UserProfile userProfile, String originalPostPushId, String commentPostKey, String commentText, Object timestamp) {
        this.userProfile = userProfile;
        this.originalPostPushId = originalPostPushId;
        this.commentPostKey = commentPostKey;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    protected CommentBean(Parcel in) {
        userProfile = in.readParcelable(UserProfile.class.getClassLoader());
        originalPostPushId = in.readString();
        commentPostKey = in.readString();
        commentText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userProfile, flags);
        dest.writeString(originalPostPushId);
        dest.writeString(commentPostKey);
        dest.writeString(commentText);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
        @Override
        public CommentBean createFromParcel(Parcel in) {
            return new CommentBean(in);
        }

        @Override
        public CommentBean[] newArray(int size) {
            return new CommentBean[size];
        }
    };

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getOriginalPostPushId() {
        return originalPostPushId;
    }

    public void setOriginalPostPushId(String originalPostPushId) {
        this.originalPostPushId = originalPostPushId;
    }

    public String getCommentPostKey() {
        return commentPostKey;
    }

    public void setCommentPostKey(String commentPostKey) {
        this.commentPostKey = commentPostKey;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
