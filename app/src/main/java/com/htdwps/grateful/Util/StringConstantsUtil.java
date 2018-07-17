package com.htdwps.grateful.Util;

/**
 * Created by HTDWPS on 4/20/18.
 */
public class StringConstantsUtil {

    // Hold Constant int values.
    public static final int REQUEST_INVITE = 0;
    public static final int OFFSET = 16;

    // Hold Constant string types for use in other Classes.
    public static final String SAVE_INSTANCE_PARAM = "public_feed_status";
    public static final String PUBLIC_PARAM = "public_feed";
    public static final String PRIVATE_PARAM = "private_feed";
    public static final String TAG_WORD_KEY_PARAM = "tag_param";

    public static final String TERMS_LABEL = "TERMS";
    public static final String PRIVACY_LABEL = "PRIVACY";
    public static final String STATEMENT_TYPE = "StatementType";

    public static final String USER_PROFILE_DETAILS_PATH = "user_profile_details";

    public static final String PUBLICLY_SHARED_BEANS_PATH = "publicly_shared_beans";
    public static final String PERSONAL_BEANS_LIST_PATH = "personal_beans_list";

    public static final String MOOD_TYPE_NAME_COUNTER_PATH = "mood_type_name_counter";
    public static final String POST_EXISTS_MOOD_TYPE_BOOLEAN_PATH = "post_by_mood_type";
    public static final String TAG_NAME_USED_PATH = "tag_name_used";
    public static final String POST_EXISTS_TAG_NAME_BOOLEAN_PATH = "post_by_tag_name";
    public static final String COMMENT_FOR_BEANS_PATH = "comment_for_beans";
    public static final String FEEDBACK_FOR_DEVELOPER_PATH = "feedback_for_developer";

    public static final String MOOD_TYPE_KEY_PARAM = "mood_param";
    public static final String MOOD_INCREMENT_VALUE_COUNT = "valueCount";

    // Use this method to populate a new user's profile with their name, email, and firebase generated user_key
    public static String createUserProfileDirectoryPath(String user_id) {
        return USER_PROFILE_DETAILS_PATH + "/" + user_id;
    }

    public static String createPostForUserDirectoryPath(String user_id, String postKey) {
        return PERSONAL_BEANS_LIST_PATH + "/" + user_id + "/" + postKey;
    }

    public static String createMoodTypeInitialZeroValuesPath(String user_id, String mood_name) {
        return MOOD_TYPE_NAME_COUNTER_PATH + "/" + user_id + "/" + mood_name;
    }



}
