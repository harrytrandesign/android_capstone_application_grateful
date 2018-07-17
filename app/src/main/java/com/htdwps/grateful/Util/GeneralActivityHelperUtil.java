package com.htdwps.grateful.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.htdwps.grateful.R;

/**
 * Created by HTDWPS on 7/15/18.
 */
public class GeneralActivityHelperUtil {

    public static void backButtonReturnToParentArrowSetup(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static LinearLayoutManager createVerticalLinearLayout(Context context, int orientation, boolean stackFromEnd, boolean reverseLayout) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(orientation);
        linearLayoutManager.setStackFromEnd(stackFromEnd);
        linearLayoutManager.setReverseLayout(reverseLayout);

        return linearLayoutManager;

    }

    public static GridLayoutManager createGridLayoutManager(Context context, int spanCount) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount);

        return gridLayoutManager;

    }

    public static Intent sendInvitationWindow(Context context) {
        return new AppInviteInvitation.IntentBuilder(context.getString(R.string.invite_title_label))
                .setMessage(context.getString(R.string.invite_message_label))
                .setDeepLink(Uri.parse("/link"))
                .build();
        // TODO: On completion of task or cancel of task send back to loadHomeFragment with navItemIndex of 0
    }

}
