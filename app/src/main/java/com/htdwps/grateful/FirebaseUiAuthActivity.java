package com.htdwps.grateful;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.htdwps.grateful.Model.MoodCount;
import com.htdwps.grateful.Model.UserProfile;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class FirebaseUiAuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private DatabaseReference userProfileDetailsDirectoryReference = FirebaseUtil.getUserProfileDetailsDirectoryReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_firebase_ui_auth);

        // Add || Change the privacy and TOS links
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .setLogo(R.drawable.icon_grateful_app_signin_logo)
                .setTheme(R.style.SignInTheme)
//                .setTosUrl("http://imharry.me/privacy.html")
                .setPrivacyPolicyUrl("http://imharry.me/privacy.html")
                .setIsSmartLockEnabled(false, true)
                .build(), RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {

                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (firebaseUser != null) {

                    final String generatedUserKey = firebaseUser.getUid();

                    final UserProfile createThisNewUserInDatabase = new UserProfile(generatedUserKey, firebaseUser.getDisplayName(), firebaseUser.getEmail());

                    userProfileDetailsDirectoryReference.child(generatedUserKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {

                                Timber.d("This userProfile does not exist and so creating a new userProfile profile.");

                                Map<String, Object> generateNewUserProfile = new HashMap<>();

                                generateNewUserProfile.put(StringConstantsUtil.createUserProfileDirectoryPath(generatedUserKey), createThisNewUserInDatabase);

                                for (String mood_label_name : EmojiSelectUtil.emojiExpressionTextValue) {

                                    MoodCount moodCount = new MoodCount(mood_label_name, 0);

                                    generateNewUserProfile.put(StringConstantsUtil.createMoodTypeInitialZeroValuesPath(generatedUserKey, mood_label_name), moodCount);

                                }

                                FirebaseUtil.getBaseRef().updateChildren(generateNewUserProfile, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        startActivity(new Intent(FirebaseUiAuthActivity.this, MainWindowActivity.class));
                                        finish();

                                    }
                                });

                            } else {

                                Timber.d("User already exists, leaving FirebaseUIAuthActivity now.");

                                startActivity(new Intent(FirebaseUiAuthActivity.this, MainWindowActivity.class));
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Sign In Cancelled", Toast.LENGTH_SHORT).show();
                    Intent startIntent = new Intent(FirebaseUiAuthActivity.this, MainActivity.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(startIntent);
                    finish();

                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                return;

            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
