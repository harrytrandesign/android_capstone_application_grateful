package com.htdwps.grateful.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.GlideUtil;

/**
 * Created by HTDWPS on 4/6/18.
 */
public class GratePostViewHolder extends RecyclerView.ViewHolder {

    private TextView tvUserDisplayName;
    private TextView tvEntryMessage;
    private ImageView ivImageBackground;

    public GratePostViewHolder(View itemView) {
        super(itemView);

        tvUserDisplayName = itemView.findViewById(R.id.tv_grateful_author);
        tvEntryMessage = itemView.findViewById(R.id.tv_grateful_message);
        ivImageBackground = itemView.findViewById(R.id.iv_image_bg);

    }

    public void populatePostEntry(String authorString, String messageString, String imageString) {
        setTvUserDisplayName("Posted By: " + authorString);
        setTvEntryMessage(messageString);
        setIvImageBackground(imageString);
    }

    private void setTvUserDisplayName(String name) {
        this.tvUserDisplayName.setText(name);
    }

    private void setTvEntryMessage(String message) {
        this.tvEntryMessage.setText(message);
    }

    private void setIvImageBackground(String image) {
        GlideUtil.loadImage(image, this.ivImageBackground);
    }
}
