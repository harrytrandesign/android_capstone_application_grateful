package com.htdwps.grateful.Util;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.htdwps.grateful.MainActivity;

/**
 * Created by HTDWPS on 6/22/18.
 */
public class FirebaseHelper {

    public static void signOffUser(Context context) {
        FirebaseAuth.getInstance().signOut();

        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(mainIntent);

    }

}
