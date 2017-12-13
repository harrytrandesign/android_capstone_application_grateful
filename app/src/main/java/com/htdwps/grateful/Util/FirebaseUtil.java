package com.htdwps.grateful.Util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.htdwps.grateful.Model.User;

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

    public static User getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return null;

        return new User(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString());
    }

    public static DatabaseReference getPrivateListRef() {
        return getBaseRef().child("personal_list_items");
    }

    public static DatabaseReference getPublicListRef() {
        return getBaseRef().child("public_list_items");
    }

    public static DatabaseReference getJournalListRef() {
        return getBaseRef().child("journal_entries");
    }

    public static DatabaseReference getPersonalJournalEntriesRef() {
        return getBaseRef().child("personal_journal_items");
    }

    public static DatabaseReference getPublicJournalEntriesRef() {
        return getBaseRef().child("public_journal_items");
    }

    public static DatabaseReference getCommentListRef() {
        return getBaseRef().child("public_comment_list");
    }

    public static DatabaseReference getFeedbackRef() {
        return getBaseRef().child("feedback");
    }

    public static DatabaseReference getReportRef() {
        return getBaseRef().child("report");
    }

}
