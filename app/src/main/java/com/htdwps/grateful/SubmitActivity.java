package com.htdwps.grateful;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.htdwps.grateful.Model.Entries;
import com.htdwps.grateful.Model.User;
import com.htdwps.grateful.Util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class SubmitActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mainReference;
    private DatabaseReference publicReference;
    private DatabaseReference personalReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText postText;
    EditText journalText;
    LinearLayout journalLayout;
    TextView postButton;
    TextView tvEntryHeader;
    TextView tvPostHint;
    TextView tvSubmitJournalHint;
    RadioButton radioButtonPost;
    RadioButton radioButtonJournal;
//    Switch publicSwitch;
    String entryType;
    String postString;
    String journalString;
    Boolean isPublic;

    Entries entries;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        // TODO: Hide the switch for now, all posts receive isPublic = true until later update
        isPublic = true;
        entryType = "Post";
        postString = "";
        journalString = "";

        setupFirebase();
        setupLayout();
    }

    public void setupFirebase() {
        mainReference = FirebaseUtil.getBaseRef();
        personalReference = FirebaseUtil.getUserPostRef();
        publicReference = FirebaseUtil.getAllPostRef();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void setCustomTypeface() {
        // Load the font asset
        Typeface headerFont = Typeface.createFromAsset(getAssets(), "fonts/kaushan.ttf");
        Typeface editTextFont = Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf");
        Typeface buttonFont = Typeface.createFromAsset(getAssets(), "fonts/passion.ttf");

        tvEntryHeader.setTypeface(headerFont);

        postText.setTypeface(editTextFont);
        journalText.setTypeface(editTextFont);
        radioButtonPost.setTypeface(editTextFont);
        radioButtonJournal.setTypeface(editTextFont);

        tvPostHint.setTypeface(buttonFont);
        tvSubmitJournalHint.setTypeface(buttonFont);
        postButton.setTypeface(buttonFont);
    }

    public void setupLayout() {
        journalLayout = findViewById(R.id.journal_layout);
        journalLayout.setVisibility(View.GONE);
        tvEntryHeader = findViewById(R.id.tv_submit_title);
        tvPostHint = findViewById(R.id.submit_hint_post);
        tvSubmitJournalHint = findViewById(R.id.submit_hint_journal);
        postText = findViewById(R.id.et_post_input);
        journalText = findViewById(R.id.et_journal_input);
        radioButtonPost = findViewById(R.id.radio_label_post);
        radioButtonPost.setChecked(true);
        radioButtonJournal = findViewById(R.id.radio_label_journal);
        postButton = findViewById(R.id.tv_submit_button);
        postButton.setOnClickListener(this);

        // Privacy check switch, switch between private and public posting
//        publicSwitch = findViewById(R.id.switch_private_post);
//        publicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                isPublic = b;
//
//                String onOffString;
//                if (isPublic) {
//                    isPublic = false;
//                    onOffString = getResources().getString(R.string.switch_label_private);
//                } else {
//                    isPublic = true;
//                    onOffString = getResources().getString(R.string.switch_label_public);
//                }
//                Toast.makeText(SubmitActivity.this, onOffString, Toast.LENGTH_SHORT).show();
//            }
//        });

        setCustomTypeface();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_label_post:
                if (checked) {

                    Toast.makeText(this, "Post selected", Toast.LENGTH_SHORT).show();
                    entryType = "Post";
                    journalLayout.setVisibility(View.GONE);
//                    publicSwitch.setEnabled(true);
                }
                break;
            case R.id.radio_label_journal:
                if (checked) {

                    Toast.makeText(this, "Journal selected", Toast.LENGTH_SHORT).show();
                    entryType = "Journal";
                    isPublic = true;
//                    publicSwitch.setChecked(false);
//                    publicSwitch.setEnabled(false);
                    if (journalLayout.getVisibility() == View.GONE) {
                        journalLayout.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    public void uploadPostToDatabase() {

        /*  If Journal entry, make sure they add in journal description, than upload to a single journal folder.
         *  Journal folder distinguished by user uploaded by key username and value auth.uid match.
         *  If Post entry, if private than only send to personal folder.
         *  else, if public is set send a copy to personal folder and a copy to public folder.
         */

        String randomPostKey = mainReference.push().getKey();

        user = FirebaseUtil.getCurrentUser();

        Map<String, Object> newPost = new HashMap<>();

        switch (entryType) {

            case "Post":
                postString = postText.getText().toString();

                entries = new Entries(user, user.getUserDisplayName(), entryType, postString, ServerValue.TIMESTAMP, "");

                if (isPublic) {
                    newPost.put("post_public_all/" + randomPostKey, entries);
                }

                newPost.put("post_private_user/" + user.getUserid() + "/" + randomPostKey, entries);

                publishToDatabase(newPost);
                
                break;

            case "Journal":
                postString = postText.getText().toString();
                journalString = journalText.getText().toString();

                entries = new Entries(user, user.getUserDisplayName(), entryType, postString, journalString, ServerValue.TIMESTAMP, "");

                newPost.put("journal_public_all/" + randomPostKey, entries);
                newPost.put("journal_private_user/" + user.getUserid() + "/" + randomPostKey, true);

                publishToDatabase(newPost);

                break;

            default:

                break;

        }

    }

    private void publishToDatabase(Map<String, Object> newPost) {
        FirebaseUtil.getBaseRef().updateChildren(newPost, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(SubmitActivity.this, "Stay Grateful", Toast.LENGTH_SHORT).show();

                    Intent completeIntent = new Intent(SubmitActivity.this, ListActivity.class);
                    completeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(completeIntent);
                } else {
                    Toast.makeText(SubmitActivity.this, "Sorry try again in a little", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_submit_button:

                uploadPostToDatabase();

                break;
        }
    }
}
