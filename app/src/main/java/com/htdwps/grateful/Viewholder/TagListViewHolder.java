package com.htdwps.grateful.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.htdwps.grateful.R;

/**
 * Created by HTDWPS on 6/27/18.
 */
public class TagListViewHolder extends RecyclerView.ViewHolder {

    TextView tvTagName;

    public TagListViewHolder(View itemView) {
        super(itemView);

        tvTagName = itemView.findViewById(R.id.tv_tag_text_label);

    }

    public void setTagName(String text) {
        tvTagName.setText(text);
    }

}
