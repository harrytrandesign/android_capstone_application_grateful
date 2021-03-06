package com.htdwps.grateful;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.htdwps.grateful.Util.ProgressDialogUtil;

public class        AuthActivity
        extends     AppCompatActivity
        implements  View.OnClickListener,
                    GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = AuthActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private static GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ProgressDialog progressDialog;

    private TextView applicationLogo;
    private TextView signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_auth);

        setInitialize();
        setupLayout();
    }

    public void setInitialize() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleApiClient.connect();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void setCustomTypeface() {

        Typeface logoTypeface = Typeface.createFromAsset(getAssets(), "fonts/sacramento.ttf");
        Typeface buttonTypeface = Typeface.createFromAsset(getAssets(), "fonts/passion.ttf");

        applicationLogo.setTypeface(logoTypeface);
        signinButton.setTypeface(buttonTypeface);

    }

    public void setupLayout() {

        applicationLogo = findViewById(R.id.tv_application_logo);
        signinButton = findViewById(R.id.tv_signin_button);
        signinButton.setOnClickListener(this);

        setCustomTypeface();

    }

    private void signInNow() {
        if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient);
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            progressDialog = ProgressDialogUtil.showProgressDialog(this, getResources().getString(R.string.dialog_message_loading));

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount userAccount = result.getSignInAccount();
            firebaseAuthWithGoogle(userAccount);

        } else {

            progressDialog.dismiss();

            Toast.makeText(this, R.string.toast_signin_failure, Toast.LENGTH_SHORT).show();

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            progressDialog.dismiss();

                            Intent switchIntent = new Intent(AuthActivity.this, MainWindowActivity.class);
                            switchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(switchIntent);

                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_signin_button:

                signInNow();

                break;

            default:

                break;
        }
    }
}
