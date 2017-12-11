package com.htdwps.grateful;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.htdwps.grateful.Model.Post;
import com.htdwps.grateful.Model.User;
import com.htdwps.grateful.Util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class SubmitActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference publicReference;
    private DatabaseReference privateReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText postText;
    Button postButton;
    Switch publicSwitch;
    String postString;
    Boolean isPublic;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        setupFirebase();
        setupLayout();

        isPublic = false;
    }

    public void setupFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        publicReference = FirebaseUtil.getPublicListRef();
        privateReference = FirebaseUtil.getPrivateListRef();
    }

    public void setupLayout() {
        postText = findViewById(R.id.et_submit_input);
        postButton = findViewById(R.id.btn_submit_button);
        postButton.setOnClickListener(this);
        publicSwitch = findViewById(R.id.switch_private_post);
        publicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isPublic = b;
            }
        });
    }

    public void uploadPostToDatabase() {

        String randomPostKey = publicReference.push().getKey();

        user = FirebaseUtil.getCurrentUser();

        postString = postText.getText().toString();

        Post post = new Post(user, postString, "Test", ServerValue.TIMESTAMP);

        Map<String, Object> newPost = new HashMap<>();

        if (isPublic) {
            newPost.put("personal_list_items/" + user.getUserid() + "/" + randomPostKey, post);
            newPost.put("public_list_items/" + randomPostKey, post);
        } else {
            newPost.put("personal_list_items/" + user.getUserid() + "/" + randomPostKey, post);
        }

        FirebaseUtil.getBaseRef().updateChildren(newPost, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(SubmitActivity.this, "Stay Grateful", Toast.LENGTH_SHORT).show();

                Intent completeIntent = new Intent(SubmitActivity.this, ListActivity.class);
                completeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(completeIntent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_submit_button:
                uploadPostToDatabase();
                break;
        }
    }
}
