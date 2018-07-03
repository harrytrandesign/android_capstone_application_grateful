package com.htdwps.grateful.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.EmojiSelectUtil;

import java.util.ArrayList;

/**
 * Created by HTDWPS on 6/8/18.
 */
public class BeanPostViewHolder extends RecyclerView.ViewHolder {

    private BeanPostViewHolder.ClickListener mClickListener;

    LinearLayout llPublicStats;
    TextView tvFeeling;
    TextView tvDatePost;
    TextView tvEmojiIcon;
    TextView tvMainMessage;
    TextView tvTagList;
    TextView tvIsPublic;
    TextView tvPostOwner;
    TextView tvCommentLink;

    public BeanPostViewHolder(View itemView) {
        super(itemView);

        llPublicStats = itemView.findViewById(R.id.linear_layout_only_show_public_posts);
        llPublicStats.setVisibility(View.INVISIBLE);
        tvFeeling = itemView.findViewById(R.id.tv_current_feeling);
        tvDatePost = itemView.findViewById(R.id.tv_date_posted);
        tvEmojiIcon = itemView.findViewById(R.id.tv_emoji_icon_image);
        tvMainMessage = itemView.findViewById(R.id.tv_main_message_field);
        tvTagList = itemView.findViewById(R.id.tv_tag_list_for_post);
        tvIsPublic = itemView.findViewById(R.id.tv_public_post_indicator);
        tvPostOwner = itemView.findViewById(R.id.tv_post_submitted_by);
        tvCommentLink = itemView.findViewById(R.id.tv_comment_on_post_link);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

    }

    public void setBeanPostFields(int feelingValue, String timestamp, String mainText, ArrayList<String> tags, boolean publicStatus, String postOwner, boolean onPublicFeed) {

        String value = String.valueOf(feelingValue);

//        StringBuilder tagsLists = new StringBuilder();
        String tagsLists = TextUtils.join(", ", tags);

        tvFeeling.setText(EmojiSelectUtil.emojiExpressionTextValue[feelingValue]);
//        tvFeeling.setText(EmojiSelectUtil.expressionTextStringEmojiType(feelingValue));
        tvEmojiIcon.setText(String.valueOf(Character.toChars(EmojiSelectUtil.emojiIconCodePoint[feelingValue])));
        tvDatePost.setText(timestamp);
        tvMainMessage.setText(mainText);

        if (TextUtils.isEmpty(tagsLists)) {
            tvTagList.setText(R.string.string_no_tags_found);
        } else {
            tvTagList.setText(tagsLists);
        }

//        String postStatus = (publicStatus) ? "Public" : "Personal";
        tvIsPublic.setText(R.string.string_public_status_text);
        if (publicStatus && onPublicFeed) {
            tvIsPublic.setVisibility(View.VISIBLE);
            llPublicStats.setVisibility(View.VISIBLE);
            tvPostOwner.setText(String.format("By %s", postOwner));
        } else {
            tvIsPublic.setVisibility(View.INVISIBLE);
            llPublicStats.setVisibility(View.INVISIBLE);
        }

    }

    // Interface to send callbacks
    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(BeanPostViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

}
