package com.htdwps.grateful;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.htdwps.grateful.Fragment.PrivateBeansFragment;
import com.htdwps.grateful.Model.Beans;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Model.GratefulComment;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.CommentsViewHolder;

import java.util.HashMap;
import java.util.Map;

public class BeanCommentActivity extends AppCompatActivity implements View.OnClickListener {

    Beans bean;
    CustomUser user;
    RecyclerView commentRecyclerView;
    DatabaseReference commentRef;
    FirebaseRecyclerAdapter<GratefulComment, CommentsViewHolder> commentsAdapter;

    EditText commentEditText;

    TextView tvEmojiIconField;
    TextView tvPostTitleMoodText;
    TextView tvPostTagListText;
    TextView tvPostMainMessageText;
    LinearLayoutManager linearLayoutManager;

    FloatingActionButton commentOpenDialogBtn;
    FloatingActionButton commentSubmitBtn;
    Animation fabRotateOpen, fabRotateClose, fabReveal, fabHidden, editTextReveal, editTextHidden;
    boolean isEditTextOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_comment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabRotateOpen = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_open);
        fabRotateClose = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_close);
        fabHidden = AnimationUtils.loadAnimation(this, R.anim.fab_hidden);
        fabReveal = AnimationUtils.loadAnimation(this, R.anim.fab_reveal);
        editTextHidden = AnimationUtils.loadAnimation(this, R.anim.edittext_hidden);
        editTextReveal = AnimationUtils.loadAnimation(this, R.anim.edittext_reveal);

        commentOpenDialogBtn = findViewById(R.id.fab_submit_comment_btn);
        commentOpenDialogBtn.setOnClickListener(this);
        commentSubmitBtn = findViewById(R.id.fab_submit_new_comment_post);
        commentSubmitBtn.setOnClickListener(this);
        commentEditText = findViewById(R.id.fragment_et_comment_typebox);

        tvEmojiIconField = findViewById(R.id.tv_emoji_icon_image_condensed);
        tvPostTitleMoodText = findViewById(R.id.tv_current_feeling_condensed);
        tvPostTagListText = findViewById(R.id.tv_tag_list_for_post_condensed);
        tvPostMainMessageText = findViewById(R.id.tv_main_message_field_condensed);

        bean = getIntent().getParcelableExtra(PrivateBeansFragment.BEAN_POST_PARAM);
        user = getIntent().getParcelableExtra(PrivateBeansFragment.CUSTOM_USER_PARAM);

        setTitle("Post: " + bean.getBeanText());

//        LinearLayoutManager llm = new LinearLayoutManager();
//        llm.setReverseLayout(true);
//        llm.setStackFromEnd(true);

//        String year = DateUtils.formatDateTime(this, (long) bean.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR);
//        String time = DateUtils.formatDateTime(this, (long) bean.getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
//        String dateTime = String.format("%s %s", year, time);

        String tagsLists = TextUtils.join(", ", bean.getTagList());

        tvEmojiIconField.setText(String.valueOf(Character.toChars(EmojiSelectUtil.emojiIconCodePoint[bean.getMoodValue()])));
        tvPostTitleMoodText.setText(EmojiSelectUtil.emojiIntConvertToString(bean.getMoodValue()));
        tvPostMainMessageText.setText(bean.getBeanText());
        tvPostTagListText.setText(tagsLists);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

//        commentEditText = findViewById(R.id.fragment_et_comment_typebox);
        commentRecyclerView = findViewById(R.id.rv_comments_list);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        commentRef = FirebaseUtil.getCommentListRef().child(bean.getBeanPostKey());

        commentsAdapter = new FirebaseRecyclerAdapter<GratefulComment, CommentsViewHolder>(
                GratefulComment.class,
                R.layout.item_comment_layout,
                CommentsViewHolder.class,
                commentRef
        ) {
            @Override
            protected void populateViewHolder(CommentsViewHolder viewHolder, GratefulComment model, int position) {

                viewHolder.setCommentTextFields(model.getCommentText(), model.getCustomUser().getUserDisplayName(), DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());

            }

        };

        commentRecyclerView.setAdapter(commentsAdapter);
//        commentsAdapter.notifyDataSetChanged();
//        commentRecyclerView.smoothScrollToPosition(commentsAdapter.getItemCount());

//        String displayName = user.getUserDisplayName();
//        tvPostTitleMoodText.setText(bean.getBeanText());
//        tvPostTagListText.setText(bean.getBeanPostKey());
//        if (user != null && displayName != null) {
//
//            tvPostMainMessageText.setText(displayName);
//
//        }

//        populateDatabaseWithFakeComments("-LGnhLBdsBsjkWyAufDz");

    }

    private void animateFabViews() {
        if (isEditTextOpen) {
            commentOpenDialogBtn.startAnimation(fabRotateClose);
            commentEditText.startAnimation(editTextHidden);
            commentSubmitBtn.startAnimation(fabHidden);
            commentSubmitBtn.setClickable(false);
            isEditTextOpen = false;
        } else {
            commentOpenDialogBtn.startAnimation(fabRotateOpen);
            commentEditText.startAnimation(editTextReveal);
            commentEditText.setVisibility(View.VISIBLE);
            commentSubmitBtn.startAnimation(fabReveal);
            commentSubmitBtn.setClickable(true);
            isEditTextOpen = true;
        }
    }

    public void submitNewComment(String comment) {

        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "Please create a comment before submitting", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.getTrimmedLength(comment) < 10) {
            commentEditText.requestFocus();
            commentEditText.setError("Comment is too short.");
        } else {
            // Submit to database
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(comment) || comment.length() < 10) {

            Toast.makeText(this, "Your comment was too short", Toast.LENGTH_SHORT).show();

        } else {

            // Submit to database
            String commentPushKey = commentRef.push().getKey();
            GratefulComment gratefulComment = new GratefulComment(FirebaseUtil.getCurrentUser(), comment, ServerValue.TIMESTAMP, commentPushKey, bean.getBeanPostKey());

            commentRef.child(commentPushKey).setValue(gratefulComment).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(BeanCommentActivity.this, "Comment Posted", Toast.LENGTH_SHORT).show();
                    commentEditText.setText("");
                    commentEditText.getText().clear();

                    commentRecyclerView.smoothScrollToPosition(commentsAdapter.getItemCount());

                }
            });

        }


    }

    // Used to create some fake comments for testing purposes only
    public void populateDatabaseWithFakeComments(String postKey) {
        String tempPostCommentPushKey = postKey;

        Map<String, Object> tempComments = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            String commentPushKey = FirebaseUtil.getCommentListRef().push().getKey();
            GratefulComment gratefulComment = new GratefulComment(user, "Fake comment " + i, ServerValue.TIMESTAMP, commentPushKey, tempPostCommentPushKey);

            String tempCommentsRef = "all_public_comment_threads/" + tempPostCommentPushKey + "/" + commentPushKey;

            tempComments.put(tempCommentsRef, gratefulComment);
        }

        FirebaseUtil.getBaseRef().updateChildren(tempComments, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(BeanCommentActivity.this, "Fake Comments Loaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.fab_submit_comment_btn:

                Toast.makeText(this, "Open Edittext Flipper Button", Toast.LENGTH_SHORT).show();

                animateFabViews();
//                MaterialHelperUtil.populateCommentBoxForPost(this, FirebaseUtil.getCurrentUser(), bean.getBeanPostKey(), commentRef);

                break;

            case R.id.fab_submit_new_comment_post:

                submitNewComment(commentEditText.getText().toString());

                Toast.makeText(this, "Submit a new comment button", Toast.LENGTH_SHORT).show();

                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
