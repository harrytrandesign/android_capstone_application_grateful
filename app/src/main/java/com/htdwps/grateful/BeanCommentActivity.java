package com.htdwps.grateful;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.htdwps.grateful.Model.BeanPosts;
import com.htdwps.grateful.Model.CommentBean;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.GeneralActivityHelperUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;
import com.htdwps.grateful.Viewholder.CommentLayoutViewHolder;

public class BeanCommentActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isEditTextOpen = false;

    private static String emptyComment = "Please create a comment before submitting";
    private static String shortComment = "Comment is too short";

    private DatabaseReference commentForBeansDirectoryReference;
    private FirebaseRecyclerAdapter<CommentBean, CommentLayoutViewHolder> adapterCommentsList;

    private BeanPosts beanPosts;

    private Animation fabRotateOpen, fabRotateClose, fabReveal, fabHidden, editTextReveal, editTextHidden;
    private RecyclerView recyclerViewBeanCommentsList;

    private EditText etCommentTextInputBox;
    private FloatingActionButton fabCommentOpenDialogBtn, fabCommentSubmitBtn;

    // For the 1st Position of Comment Activity showing original Post details.
    private TextView tvEmojiIconField, tvPostTitleMoodText, tvPostTagListText, tvPostMainMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_comment);

        setupLayout();

        gatherIntentsFromMainFeedForPost();

        recyclerViewBeanCommentsList.setAdapter(createCommentsForPostAdapter(commentForBeansDirectoryReference));

//        populateDatabaseWithFakeComments("-LGnhLBdsBsjkWyAufDz");

    }

    private void gatherIntentsFromMainFeedForPost() {

        beanPosts = getIntent().getParcelableExtra(StringConstantsUtil.BEAN_POST_PARAM);

        setTitle("Post: " + beanPosts.getBeanText());

        commentForBeansDirectoryReference = FirebaseUtil.getCommentForBeansListDirectoryReference().child(beanPosts.getBeanPostKey());

        populateFirstPositionPostFieldsWithOriginalData(beanPosts);

    }

    public FirebaseRecyclerAdapter<CommentBean, CommentLayoutViewHolder> createCommentsForPostAdapter(DatabaseReference databaseReference) {

        adapterCommentsList = new FirebaseRecyclerAdapter<CommentBean, CommentLayoutViewHolder>(
                CommentBean.class,
                R.layout.item_comment_single_post,
                CommentLayoutViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(CommentLayoutViewHolder viewHolder, CommentBean model, int position) {

                viewHolder.setCommentTextFields(model.getCommentText(), model.getUserProfile().getUserDisplayName(), DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());

            }

        };

        return adapterCommentsList;

    }

    private void populateFirstPositionPostFieldsWithOriginalData(BeanPosts beanPosts) {

        int originalPostMoodValue = beanPosts.getMoodValue();
        String emojiIconFromString = String.valueOf(Character.toChars(EmojiSelectUtil.emojiIconCodePoint[originalPostMoodValue]));
        String originalMoodFeeling = String.format("FEELING %s", EmojiSelectUtil.emojiIntConvertToString(originalPostMoodValue));
        String originalPostText = beanPosts.getBeanText();
        String tagsContainedList = TextUtils.join(", ", beanPosts.getTagList());

        tvEmojiIconField.setText(emojiIconFromString);
        tvPostTitleMoodText.setText(originalMoodFeeling);
        tvPostMainMessageText.setText(originalPostText);
        tvPostTagListText.setText(tagsContainedList);

    }

    private void setupLayout() {

        loadAnimationUtils();

        ActionBar actionBar = BeanCommentActivity.this.getSupportActionBar();

        GeneralActivityHelperUtil.backButtonReturnToParentArrowSetup(actionBar);

        // First position at top details
        tvEmojiIconField = findViewById(R.id.tv_emoji_icon_image_condensed);
        tvPostTitleMoodText = findViewById(R.id.tv_current_feeling_condensed);
        tvPostTagListText = findViewById(R.id.tv_tag_list_for_post_condensed);
        tvPostMainMessageText = findViewById(R.id.tv_main_message_field_condensed);

        // Bottom position buttons and edittext views
        fabCommentOpenDialogBtn = findViewById(R.id.fab_submit_comment_btn);
        fabCommentOpenDialogBtn.setOnClickListener(this);
        fabCommentSubmitBtn = findViewById(R.id.fab_submit_new_comment_post);
        fabCommentSubmitBtn.setOnClickListener(this);
        etCommentTextInputBox = findViewById(R.id.fragment_et_comment_typebox);

        // Recyclerview
        recyclerViewBeanCommentsList = findViewById(R.id.rv_comments_list);
        recyclerViewBeanCommentsList.setLayoutManager(GeneralActivityHelperUtil.createVerticalLinearLayout(this, LinearLayoutManager.VERTICAL, true, true));

    }

    private void loadAnimationUtils() {

        fabRotateOpen = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_open);
        fabRotateClose = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_close);
        fabHidden = AnimationUtils.loadAnimation(this, R.anim.fab_hidden);
        fabReveal = AnimationUtils.loadAnimation(this, R.anim.fab_reveal);
        editTextHidden = AnimationUtils.loadAnimation(this, R.anim.edittext_hidden);
        editTextReveal = AnimationUtils.loadAnimation(this, R.anim.edittext_reveal);

    }

    private void animateFabViews() {

        if (isEditTextOpen) {

            fabCommentOpenDialogBtn.startAnimation(fabRotateClose);
            etCommentTextInputBox.startAnimation(editTextHidden);
            fabCommentSubmitBtn.startAnimation(fabHidden);
            fabCommentSubmitBtn.setClickable(false);
            isEditTextOpen = false;

        } else {

            fabCommentOpenDialogBtn.startAnimation(fabRotateOpen);
            etCommentTextInputBox.startAnimation(editTextReveal);
            etCommentTextInputBox.setVisibility(View.VISIBLE);
            fabCommentSubmitBtn.startAnimation(fabReveal);
            fabCommentSubmitBtn.setClickable(true);
            isEditTextOpen = true;

        }
    }

    private void editTextShowErrorMessage(EditText et, String message) {
        et.requestFocus();
        et.setError(message);
    }

    public void submitNewComment(String comment) {

        if (TextUtils.isEmpty(comment)) {

            editTextShowErrorMessage(etCommentTextInputBox, emptyComment);

        } else if (TextUtils.getTrimmedLength(comment) < 10) {

            editTextShowErrorMessage(etCommentTextInputBox, shortComment);

        } else {

            // Submit to database
            String commentPushKey = commentForBeansDirectoryReference.push().getKey();
            CommentBean commentBean = new CommentBean(FirebaseUtil.getCurrentUser(), beanPosts.getBeanPostKey(), commentPushKey, comment, ServerValue.TIMESTAMP);

            commentForBeansDirectoryReference.child(commentPushKey).setValue(commentBean).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(BeanCommentActivity.this, R.string.toast_comment_post_success, Toast.LENGTH_SHORT).show();

                    etCommentTextInputBox.setText("");
                    etCommentTextInputBox.getText().clear();

                    recyclerViewBeanCommentsList.smoothScrollToPosition(adapterCommentsList.getItemCount());

                }
            });

        }


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.fab_submit_comment_btn:

                animateFabViews();

                break;

            case R.id.fab_submit_new_comment_post:

                String userComment = etCommentTextInputBox.getText().toString();

                submitNewComment(userComment);

                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
