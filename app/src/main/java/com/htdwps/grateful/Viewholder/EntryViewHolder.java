package com.htdwps.grateful.Viewholder;

import android.graphics.Typeface;
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
    private Typeface listTypeface;

    public EntryViewHolder(View itemView) {
        super(itemView);

        listTypeface = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/raleway.ttf");

        entryTypeImage = itemView.findViewById(R.id.iv_icon_entry_type);
        entryUserDisplayName = itemView.findViewById(R.id.item_tv_post_author);
        entryPostText = itemView.findViewById(R.id.item_tv_post_text);
        entryJournalText = itemView.findViewById(R.id.item_tv_journal_text);
        entryTimestamp = itemView.findViewById(R.id.item_tv_post_time);
        entryCommentCount = itemView.findViewById(R.id.item_tv_comment_count);

        this.entryPostText.setTypeface(listTypeface);
        this.entryJournalText.setTypeface(listTypeface);
        this.entryTimestamp.setTypeface(listTypeface);
        this.entryCommentCount.setTypeface(listTypeface);
    }

    public void setViewObjects(String text1, String text2, String text3, String text4, String text5) {
        setEntryTypeImage(text1);
        setEntryUserDisplayName(text2);
        setEntryPostText(text3);
        setEntryJournalText(text4);
        setEntryTimestamp(text5);
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
        this.entryUserDisplayName.setText(text + " wrote...");
    }

    public void setEntryPostText(String text) {
        this.entryPostText.setText(text);
    }

    public void setEntryJournalText(String text) {
        if ("".equals(text)) {
            this.entryJournalText.setVisibility(View.GONE);
        } else {
            this.entryJournalText.setText(text);
        }
    }

    public void setEntryTimestamp(String text) {
        this.entryTimestamp.setText(text);
    }

    public void setEntryCommentCount(String text) {
        this.entryCommentCount.setText(text);
    }
}
