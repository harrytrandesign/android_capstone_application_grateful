package com.htdwps.grateful.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.htdwps.grateful.R;

/**
 * Created by HTDWPS on 7/7/18.
 */
public class CommentsViewHolder extends RecyclerView.ViewHolder {

    public static final int POST_HEADER_LAYOUT_TYPE = 0;
    public static final int COMMENT_VIEW_LAYOUT_TYPE = 1;

    TextView tvOriginalPostText;
    TextView tvOriginalPostByUser;

    TextView tvCommentText;
    TextView tvCommentUserName;
    TextView tvCommentTimestamp;

    public CommentsViewHolder(View itemView) {
        super(itemView);

        tvCommentText = itemView.findViewById(R.id.tv_comment_main_text);
        tvCommentUserName = itemView.findViewById(R.id.tv_comment_creator_username);
        tvCommentTimestamp = itemView.findViewById(R.id.tv_comment_date_posted);

    }

    public CommentsViewHolder(View itemView, int itemType) {
        super(itemView);

        if (itemType == POST_HEADER_LAYOUT_TYPE) {

            // 1st position of recyclerview
            tvOriginalPostText = itemView.findViewById(R.id.tv_comment_main_text);
            tvOriginalPostByUser = itemView.findViewById(R.id.tv_comment_creator_username);

        } else if (itemType == COMMENT_VIEW_LAYOUT_TYPE) {

            tvCommentText = itemView.findViewById(R.id.tv_comment_main_text);
            tvCommentUserName = itemView.findViewById(R.id.tv_comment_creator_username);
            tvCommentTimestamp = itemView.findViewById(R.id.tv_comment_date_posted);

        }

    }

    public void setTopPostTextFields(String text, String username) {
        tvOriginalPostText.setText(text);
        tvOriginalPostByUser.setText(username);
    }

    public void setCommentTextFields(String text, String username, String time) {
        tvCommentText.setText(text);
        tvCommentUserName.setText(username);
        tvCommentTimestamp.setText(time);
    }

}
