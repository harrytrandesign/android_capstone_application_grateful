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
public class BeanLayoutViewHolder extends RecyclerView.ViewHolder {

    private BeanLayoutViewHolder.ClickListener mClickListener;

    private LinearLayout llPublicStats;
    private TextView tvFeeling;
    private TextView tvDatePost;
    private TextView tvEmojiIcon;
    private TextView tvMainMessage;
    private TextView tvTagList;
    private TextView tvIsPublic;
    private TextView tvPostOwner;
    private TextView tvCommentLink;

    public BeanLayoutViewHolder(View itemView) {
        super(itemView);

        itemView.setClickable(false);

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
        tvCommentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

    }

    public void setBeanPostFields(int feelingValue, String timestamp, String mainText, ArrayList<String> tags, boolean publicStatus, String postOwner, boolean onPublicFeed) {

        String tagsLists = TextUtils.join(", ", tags);

        tvFeeling.setText(String.format("FEELING %s", EmojiSelectUtil.emojiExpressionTextValue[feelingValue]));
        tvEmojiIcon.setText(String.valueOf(Character.toChars(EmojiSelectUtil.emojiIconCodePoint[feelingValue])));
        tvDatePost.setText(timestamp);
        tvMainMessage.setText(mainText);

        if (TextUtils.isEmpty(tagsLists)) {

            tvTagList.setText(R.string.tags_none_used_message_text);

        } else {

            tvTagList.setText(tagsLists);

        }

        tvIsPublic.setText(R.string.public_status_text_indicator);

        if (!onPublicFeed) {

            llPublicStats.setVisibility(View.GONE);

            if (!publicStatus) {

                tvIsPublic.setVisibility(View.GONE);

            }

        } else {

            llPublicStats.setVisibility(View.VISIBLE);
            tvPostOwner.setText(String.format("By %s", postOwner));

        }

    }

    // Interface to send callbacks
    public interface ClickListener {

        void onItemClick(View view, int position);

    }

    public void setOnClickListener(BeanLayoutViewHolder.ClickListener clickListener) {

        mClickListener = clickListener;

    }

}
