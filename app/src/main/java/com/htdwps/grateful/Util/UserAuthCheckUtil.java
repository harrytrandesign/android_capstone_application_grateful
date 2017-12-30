package com.htdwps.grateful.Util;

import android.content.Context;
import android.content.Intent;

import com.htdwps.grateful.AuthActivity;

/**
 * Created by HTDWPS on 12/30/17.
 */

public class UserAuthCheckUtil {

    public static void authUserVerification(Context context) {

        Intent signOnIntent = new Intent(context, AuthActivity.class);
        signOnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(signOnIntent);
    }

}
