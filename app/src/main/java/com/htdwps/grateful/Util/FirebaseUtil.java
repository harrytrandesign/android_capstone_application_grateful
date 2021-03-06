package com.htdwps.grateful.Util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.htdwps.grateful.Model.UserProfile;

/**
 * Created by HTDWPS on 12/8/17.
 */

public class FirebaseUtil {

    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    private static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public static UserProfile getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return null;

        return new UserProfile(user.getUid(), user.getDisplayName(), user.getEmail());
    }

    // ref --> user_id --> user pojo;
    public static DatabaseReference getUserProfileDetailsDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.USER_PROFILE_DETAILS_PATH);
    }

    // ref --> post_key;
    public static DatabaseReference getBeanPublicDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.PUBLICLY_SHARED_BEANS_PATH);
    }

    // ref --> user_id --> post_key;
    public static DatabaseReference getPrivateUserBeanPostDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.PERSONAL_BEANS_LIST_PATH);
    }

    // Posts directory holds all instances of posts that come up;
    // DatabaseReference MoodPostsDirectoryRef;
    // ref --> user_id --> mood_name --> post_key : true;
    public static DatabaseReference getMoodBeanListBooleanDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.POST_EXISTS_MOOD_TYPE_BOOLEAN_PATH);
    }

    // While the other folder tracks the actual counts
    // DatabaseReference MoodCountDirectoryRef;
    // ref --> user_id --> mood : int 4, mood2 : int 10;
    public static DatabaseReference getMoodCounterDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.MOOD_TYPE_NAME_COUNTER_PATH);
    }

    // DatabaseReference TagPostsDirectoryRef;
    // ref --> user_id --> tag_word --> post_key : true;
    public static DatabaseReference getTagsBeanBooleanDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.POST_EXISTS_TAG_NAME_BOOLEAN_PATH);
    }

    // DatabaseReference TagNameDirectoryRef;
    // ref --> user_id --> tag_word --> tag pojo
    public static DatabaseReference getTagsPostsWithTagDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.TAG_NAME_USED_PATH);
    }

    // DatabaseReference CommentForBeansDirectoryRef;
    // ref --> post_key --> comment_key --> comment.class;
    public static DatabaseReference getCommentForBeansListDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.COMMENT_FOR_BEANS_PATH);
    }

    public static DatabaseReference getFeedbackForDeveloperDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.FEEDBACK_FOR_DEVELOPER_PATH);
    }

}
