package com.htdwps.grateful;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private AuthUI authUI;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAnalytics mFirebaseAnalytics;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        plantTimberTreeDebug();

        initiateFirebaseServices();

        initiateSwitchActivity(mFirebaseUser);

    }

    private void initiateFirebaseServices() {

        authUI = AuthUI.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    public void plantTimberTreeDebug() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public void initiateSwitchActivity(FirebaseUser firebaseUser) {

        Intent switchIntent = checkAlreadyAuth(firebaseUser);
        switchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(switchIntent);
        finish();

    }

    public Intent checkAlreadyAuth(FirebaseUser firebaseUser) {
        // If user is already logged on send them to the main feed page, else send them to the sign up page.
        if (firebaseUser != null) {

            // If user ISN'T null, send them to the main feed page.
            return new Intent(this, MainWindowActivity.class);

        } else {

            // If user IS null, send to sign in page to create a new account.
            return new Intent(this, FirebaseUiAuthActivity.class);

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
