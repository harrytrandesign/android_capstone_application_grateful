package com.htdwps.grateful.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.EmojiSelectUtil;

import java.util.ArrayList;

/**
 * Created by HTDWPS on 6/8/18.
 */
public class BeanPostViewHolder extends RecyclerView.ViewHolder {

    TextView tvFeeling;
    TextView tvDatePost;
    TextView tvEmojiIcon;
    TextView tvMainMessage;
    TextView tvTagList;
    TextView tvIsPublic;

    public BeanPostViewHolder(View itemView) {
        super(itemView);

        tvFeeling = itemView.findViewById(R.id.tv_current_feeling);
        tvDatePost = itemView.findViewById(R.id.tv_date_posted);
        tvEmojiIcon = itemView.findViewById(R.id.tv_emoji_icon_image);
        tvMainMessage = itemView.findViewById(R.id.tv_main_message_field);
        tvTagList = itemView.findViewById(R.id.tv_tag_list_for_post);
        tvIsPublic = itemView.findViewById(R.id.tv_public_post_indicator);

    }

    public void setBeanPostFields(int feelingValue, String timestamp, String mainText, ArrayList<String> tags, boolean publicStatus) {

        String value = String.valueOf(feelingValue);

//        StringBuilder tagsLists = new StringBuilder();
        String tagsLists = TextUtils.join(", ", tags);

        tvFeeling.setText(EmojiSelectUtil.emojiExpressionTextValue[feelingValue]);
        tvEmojiIcon.setText(String.valueOf(Character.toChars(EmojiSelectUtil.emojiIconCodePoint[feelingValue])));
        tvDatePost.setText(timestamp);
        tvMainMessage.setText(mainText);
        tvTagList.setText(tagsLists);

        String postStatus = (publicStatus) ? "Public" : "Personal";
        tvIsPublic.setText(postStatus);

    }

}
