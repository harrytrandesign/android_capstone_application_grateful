package com.htdwps.grateful.Util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.htdwps.grateful.Model.CustomUser;

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

    public static DatabaseReference getCurrentUserRef() {
        String uid = getCurrentUserId();
        if (uid != null) {
            return getBaseRef().child("usernames_list").child(getCurrentUserId());
        }
        return null;
    }

    public static CustomUser getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return null;

        return new CustomUser(user.getUid(), user.getDisplayName(), user.getEmail());
    }

    public static DatabaseReference getGratefulPostsRef() {
        return getBaseRef().child("grateful_posts_public");
    }

    public static DatabaseReference getGratefulPersonalRef() {
        return getBaseRef().child("grateful_personal_posts");
    }

    public static DatabaseReference getUsernamesRef() {
        return getBaseRef().child("all_usernames");
    }

    public static DatabaseReference getUserPostRef() {
        return getBaseRef().child("post_private_user");
    }

    public static DatabaseReference getAllPostRef() {
        return getBaseRef().child("post_public_all");
    }

    public static DatabaseReference getUserJournalRef() {
        return getBaseRef().child("journal_private_user");
    }

    public static DatabaseReference getAllJournalRef() {
        return getBaseRef().child("journal_public_all");
    }

    public static DatabaseReference getJournalListRef() {
        return getBaseRef().child("journal_entries");
    }

    public static DatabaseReference getCommentListRef() {
        return getBaseRef().child("all_comment");
    }

    public static DatabaseReference getFeedbackRef() {
        return getBaseRef().child("feedback");
    }

    public static DatabaseReference getReportRef() {
        return getBaseRef().child("report");
    }

}
