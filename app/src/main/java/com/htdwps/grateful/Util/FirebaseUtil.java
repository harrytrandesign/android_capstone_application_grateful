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

    // all_usernames --> user_id --> user pojo;
    public static DatabaseReference getUserProfileDetailsDirectoryReference() {
        return getBaseRef().child(StringConstantsUtil.USER_PROFILE_DETAILS_PATH);
    }

    // public_beans_posts --> post_key;
    public static DatabaseReference getBeanPublicReference() {
        return getBaseRef().child(StringConstantsUtil.PUBLICLY_SHARED_BEANS_PATH);
    }

    // private_bean_posts --> user_id --> post_key;
    public static DatabaseReference getPrivateUserBeanPostReference() {
        return getBaseRef().child(StringConstantsUtil.PERSONAL_BEANS_LIST_PATH);
    }

    public static DatabaseReference getMoodBeanListReference() {
        return getBaseRef().child(StringConstantsUtil.POSTBY_MOOD_TYPE_PATH);
    }

    // mood_type_counter_values --> user_id --> mood : int 0, mood2 : int 0;
    public static DatabaseReference getMoodCounterReference() {
        return getBaseRef().child(StringConstantsUtil.MOOD_TYPE_NAME_COUNTER_PATH);
    }

    // post_tags_list --> user_id --> tag_word --> post_key : true;
    public static DatabaseReference getTagsBeanReference() {
        return getBaseRef().child(StringConstantsUtil.POSTBY_TAG_NAME_PATH);
    }

    // ref --> user_id --> tag_word --> tag pojo
    public static DatabaseReference getTagsPostsWithTagReference() {
        return getBaseRef().child(StringConstantsUtil.TAG_NAME_USED_PATH);
    }

    // all_public_comment_threads --> post_key --> comment_key --> comment.class;
    public static DatabaseReference getCommentListRef() {
        return getBaseRef().child(StringConstantsUtil.COMMENT_FOR_BEANS_PATH);
    }

    public static DatabaseReference getFeedbackRef() {
        return getBaseRef().child(StringConstantsUtil.FEEDBACK_FOR_DEVELOPER_PATH);
    }

}
