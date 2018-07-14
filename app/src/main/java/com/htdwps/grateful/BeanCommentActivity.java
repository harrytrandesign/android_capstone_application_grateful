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
import com.htdwps.grateful.Model.BeanPosts;
import com.htdwps.grateful.Model.UserProfile;
import com.htdwps.grateful.Model.CommentBean;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;
import com.htdwps.grateful.Viewholder.CommentLayoutViewHolder;

import java.util.HashMap;
import java.util.Map;

public class BeanCommentActivity extends AppCompatActivity implements View.OnClickListener {

    boolean isEditTextOpen = false;

    DatabaseReference commentForBeansDirectoryReference;
    FirebaseRecyclerAdapter<CommentBean, CommentLayoutViewHolder> commentsAdapter;
    BeanPosts bean;
    UserProfile user;

    Animation fabRotateOpen, fabRotateClose, fabReveal, fabHidden, editTextReveal, editTextHidden;
    LinearLayoutManager linearLayoutManager;
    RecyclerView commentRecyclerView;

    EditText commentEditText;
    FloatingActionButton commentOpenDialogBtn;
    FloatingActionButton commentSubmitBtn;

    TextView tvEmojiIconField;
    TextView tvPostTitleMoodText;
    TextView tvPostTagListText;
    TextView tvPostMainMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_comment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupLayout();

        bean = getIntent().getParcelableExtra(PrivateBeansFragment.BEAN_POST_PARAM);
        user = getIntent().getParcelableExtra(PrivateBeansFragment.CUSTOM_USER_PARAM);

        setTitle("Post: " + bean.getBeanText());

        String tagsLists = TextUtils.join(", ", bean.getTagList());

        tvEmojiIconField.setText(String.valueOf(Character.toChars(EmojiSelectUtil.emojiIconCodePoint[bean.getMoodValue()])));
        tvPostTitleMoodText.setText(EmojiSelectUtil.emojiIntConvertToString(bean.getMoodValue()));
        tvPostMainMessageText.setText(bean.getBeanText());
        tvPostTagListText.setText(tagsLists);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        commentRecyclerView = findViewById(R.id.rv_comments_list);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        commentForBeansDirectoryReference = FirebaseUtil.getCommentListRef().child(bean.getBeanPostKey());

        commentsAdapter = new FirebaseRecyclerAdapter<CommentBean, CommentLayoutViewHolder>(
                CommentBean.class,
                R.layout.item_comment_single_post,
                CommentLayoutViewHolder.class,
                commentForBeansDirectoryReference
        ) {
            @Override
            protected void populateViewHolder(CommentLayoutViewHolder viewHolder, CommentBean model, int position) {

                viewHolder.setCommentTextFields(model.getCommentText(), model.getUserProfile().getUserDisplayName(), DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());

            }

        };

        commentRecyclerView.setAdapter(commentsAdapter);

//        populateDatabaseWithFakeComments("-LGnhLBdsBsjkWyAufDz");

    }

    private void setupLayout() {
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
            String commentPushKey = commentForBeansDirectoryReference.push().getKey();
            CommentBean commentBean = new CommentBean(FirebaseUtil.getCurrentUser(), bean.getBeanPostKey(), commentPushKey, comment, ServerValue.TIMESTAMP);

            commentForBeansDirectoryReference.child(commentPushKey).setValue(commentBean).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void populateDatabaseWithFakeComments(String originalThreadPushId) {

        Map<String, Object> tempComments = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            String commentGenerateRandomPushId = FirebaseUtil.getCommentListRef().push().getKey();
            CommentBean commentBean = new CommentBean(user, originalThreadPushId, commentGenerateRandomPushId, "Fake comment " + i, ServerValue.TIMESTAMP);

            String temporaryCommentsDirectoryPath = StringConstantsUtil.COMMENT_FOR_BEANS_PATH + "/" + originalThreadPushId + "/" + commentGenerateRandomPushId;

            tempComments.put(temporaryCommentsDirectoryPath, commentBean);
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
//                MaterialHelperUtil.populateCommentBoxForPost(this, FirebaseUtil.getCurrentUser(), bean.getBeanPostKey(), commentForBeansDirectoryReference);

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
