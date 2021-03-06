package com.htdwps.grateful.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.htdwps.grateful.R;

/**
 * Created by HTDWPS on 7/7/18.
 */
public class CommentLayoutViewHolder extends RecyclerView.ViewHolder {

    private TextView tvCommentText;
    private TextView tvCommentUserName;
    private TextView tvCommentTimestamp;

    public CommentLayoutViewHolder(View itemView) {
        super(itemView);

        tvCommentText = itemView.findViewById(R.id.tv_comment_main_text_field);
        tvCommentUserName = itemView.findViewById(R.id.tv_comment_creator_field);
        tvCommentTimestamp = itemView.findViewById(R.id.tv_comment_date_posted_field);

    }

    public void setCommentTextFields(String text, String username, String time) {

        tvCommentText.setText(text);
        tvCommentUserName.setText(username);
        tvCommentTimestamp.setText(time);

    }

}
