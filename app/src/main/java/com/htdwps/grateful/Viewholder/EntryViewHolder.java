package com.htdwps.grateful.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.GlideUtil;

/**
 * Created by HTDWPS on 12/15/17.
 */

public class EntryViewHolder extends RecyclerView.ViewHolder{

    private ImageView entryTypeImage;
    private TextView entryUserDisplayName;
    private TextView entryPostText;
    private TextView entryJournalText;
    private TextView entryTimestamp;
    private TextView entryCommentCount;

    public EntryViewHolder(View itemView) {
        super(itemView);

        entryTypeImage = itemView.findViewById(R.id.iv_icon_entry_type);
        entryUserDisplayName = itemView.findViewById(R.id.item_tv_post_author);
        entryPostText = itemView.findViewById(R.id.item_tv_post_text);
        entryJournalText = itemView.findViewById(R.id.item_tv_journal_text);
        entryTimestamp = itemView.findViewById(R.id.item_tv_post_time);
        entryCommentCount = itemView.findViewById(R.id.item_tv_comment_count);
    }

    public void setEntryTypeImage(String text) {
        switch (text) {
            case "Post":

                GlideUtil.loadImage(R.drawable.post, entryTypeImage);

                break;

            case "Journal":

                GlideUtil.loadImage(R.drawable.journal, entryTypeImage);

                break;
        }
    }

    public void setEntryUserDisplayName(String text) {
        this.entryUserDisplayName.setText(text);
    }

    public void setEntryPostText(String text) {
        this.entryPostText.setText(text);
    }

    public void setEntryJournalText(String text) {
        this.entryJournalText.setText(text);
    }

    public void setEntryTimestamp(String text) {
        this.entryTimestamp.setText(text);
    }

    public void setEntryCommentCount(String text) {
        this.entryCommentCount.setText(text);
    }
}
