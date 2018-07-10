package com.htdwps.grateful;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.htdwps.grateful.Fragment.PrivateBeansFragment;
import com.htdwps.grateful.Model.Beans;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Model.GratefulComment;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.MaterialHelperUtil;
import com.htdwps.grateful.Viewholder.CommentsViewHolder;

import java.util.HashMap;
import java.util.Map;

public class BeanCommentActivity extends AppCompatActivity implements View.OnClickListener {

    Beans bean;
    CustomUser user;
    RecyclerView commentRecyclerView;
    //    CommentsBaseAdapter commentsBaseAdapter;
    DatabaseReference commentRef;

    EditText commentEditText;

    TextView tvEmojiIcon;
    TextView tvPostText;
    TextView tvPostPushKey;
    TextView tvPostUserDisplayName;

    FloatingActionButton commentFabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_comment);

        commentFabBtn = findViewById(R.id.fab_submit_comment_btn);
        commentFabBtn.setOnClickListener(this);

        tvEmojiIcon = findViewById(R.id.tv_emoji_icon_image);
        tvPostText = findViewById(R.id.tv_current_feeling);
        tvPostPushKey = findViewById(R.id.tv_tag_list_for_post);
        tvPostUserDisplayName = findViewById(R.id.tv_main_message_field);

        bean = getIntent().getParcelableExtra(PrivateBeansFragment.BEAN_POST_PARAM);
        user = getIntent().getParcelableExtra(PrivateBeansFragment.CUSTOM_USER_PARAM);

        setTitle("Post: " + bean.getBeanText());

//        String year = DateUtils.formatDateTime(this, (long) bean.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR);
//        String time = DateUtils.formatDateTime(this, (long) bean.getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
//        String dateTime = String.format("%s %s", year, time);

        String tagsLists = TextUtils.join(", ", bean.getTagList());

        tvEmojiIcon.setText(String.valueOf(Character.toChars(EmojiSelectUtil.emojiIconCodePoint[bean.getMoodValue()])));
        tvPostText.setText(EmojiSelectUtil.emojiIntConvertToString(bean.getMoodValue()));
        tvPostUserDisplayName.setText(bean.getBeanText());
        tvPostPushKey.setText(tagsLists);

//        commentEditText = findViewById(R.id.fragment_et_comment_typebox);
        commentRecyclerView = findViewById(R.id.rv_comments_list);
        commentRef = FirebaseUtil.getCommentListRef().child(bean.getBeanPostKey());
//        commentsBaseAdapter = new CommentsBaseAdapter(
//                GratefulComment.class, R.layout.item_comment_layout, CommentsViewHolder.class, commentRef, this, bean, user);

        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        commentRecyclerView.setAdapter(commentsBaseAdapter);
        FirebaseRecyclerAdapter<GratefulComment, CommentsViewHolder> commentsAdapter = new FirebaseRecyclerAdapter<GratefulComment, CommentsViewHolder>(
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

//        String displayName = user.getUserDisplayName();
//        tvPostText.setText(bean.getBeanText());
//        tvPostPushKey.setText(bean.getBeanPostKey());
//        if (user != null && displayName != null) {
//
//            tvPostUserDisplayName.setText(displayName);
//
//        }

//        populateDatabaseWithFakeComments("-LGnhLBdsBsjkWyAufDz");

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

                MaterialHelperUtil.populateCommentBoxForPost(this, FirebaseUtil.getCurrentUser(), bean.getBeanPostKey(), commentRef);

                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
