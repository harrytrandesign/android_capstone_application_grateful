package com.htdwps.grateful.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.EmojiSelectUtil;

/**
 * Created by HTDWPS on 7/5/18.
 */
public class MoodCountViewHolder extends RecyclerView.ViewHolder {

    TextView tvMoodEmojiIcon;
    TextView tvMoodValueCount;

    public MoodCountViewHolder(View itemView) {
        super(itemView);

        tvMoodEmojiIcon = itemView.findViewById(R.id.tv_emoji_type_icon_placeholder);
        tvMoodValueCount = itemView.findViewById(R.id.tv_mood_count_value_placeholder);

    }

    public void setMoodValue(String emoji, int count) {
        tvMoodEmojiIcon.setText(String.valueOf(Character.toChars(EmojiSelectUtil.emojiIconCodePoint[EmojiSelectUtil.emojiStringConvertToInt(emoji)])));
        tvMoodValueCount.setText(String.valueOf(count));
    }


}
