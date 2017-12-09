package com.htdwps.grateful;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htdwps.grateful.Util.ProgressDialogUtil;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // If user is already logged on send them to the main feed page, else send them to the sign up page.
        if (mFirebaseUser != null) {
            // If user ISN'T null, send them to the main feed page.
            progressDialog = ProgressDialogUtil.showProgressDialog(this, getResources().getString(R.string.dialog_message_loading));

            Intent mainIntent = new Intent(this, AuthActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();

        } else {
            // If user IS null, send to sign in page to create a new account.
            progressDialog = ProgressDialogUtil.showProgressDialog(this, getResources().getString(R.string.dialog_message_loading));

            Intent signInIntent = new Intent(this, AuthActivity.class);
            signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signInIntent);
            finish();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the progress dialog if it is still open.
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }
}
