package com.htdwps.grateful.Util;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by HTDWPS on 7/15/18.
 */
public class DatabaseReferenceHelperUtil {

    // Return the directory for user's posts;
    public static DatabaseReference getUserPostsMatchingPostKeyDirectoryRef(String user_id) {
        return FirebaseUtil.getPrivateUserBeanPostDirectoryReference().child(user_id);
    }

    // Return DatabaseReference that tracks the mood counts: happy : count = 2;
    public static DatabaseReference getMoodCountDirectoryRef(String user_id) {
        return FirebaseUtil.getMoodCounterDirectoryReference().child(user_id);
    }

    // Return DatabaseReference that tracks true post_keys under a certain mood type;
    public static DatabaseReference getMoodPostBooleanDirectoryRef(String user_id, String mood_name) {
        return FirebaseUtil.getMoodBeanListBooleanDirectoryReference().child(user_id).child(mood_name);
    }

    public static DatabaseReference getTaggedPostBooleanDirectoryRef(String user_id, String tag_name) {
        return FirebaseUtil.getTagsBeanBooleanDirectoryReference().child(user_id).child(tag_name);
    }

}
