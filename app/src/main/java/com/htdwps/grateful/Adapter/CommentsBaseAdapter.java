package com.htdwps.grateful.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.htdwps.grateful.Model.Beans;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Model.GratefulComment;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Viewholder.CommentsViewHolder;

/**
 * Created by HTDWPS on 7/7/18.
 */
public class CommentsBaseAdapter extends FirebaseRecyclerAdapter<GratefulComment, CommentsViewHolder> {

    Context mContext;
    Beans beans;
    CustomUser customUser;

    public CommentsBaseAdapter(Class<GratefulComment> modelClass, int modelLayout, Class<CommentsViewHolder> viewHolderClass, Query ref, Context context, Beans bean, CustomUser customUser) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = context;
        this.beans = bean;
        this.customUser = customUser;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == CommentsViewHolder.POST_HEADER_LAYOUT_TYPE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_layout, parent, false);

        } else if (viewType == CommentsViewHolder.COMMENT_VIEW_LAYOUT_TYPE) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_layout, parent, false);

        }

        return new CommentsViewHolder(view, viewType);
    }

    @Override
    protected void populateViewHolder(CommentsViewHolder viewHolder, GratefulComment model, int position) {

        if (position == 0) {
            // Retrieve whether owner wanted to remain anonymous
            viewHolder.setTopPostTextFields(beans.getBeanText(), customUser.getUserDisplayName());

        } else {

            viewHolder.setCommentTextFields(model.getCommentText(), model.getCustomUser().getUserDisplayName(), DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());

        }

    }

//    @Override
//    public void onBindViewHolder(CommentsViewHolder viewHolder, int position) {
//        super.onBindViewHolder(viewHolder, position);

//        int viewType = getItemViewType(position);
//
//        switch (viewType) {
//
//            case CommentsViewHolder.POST_HEADER_LAYOUT_TYPE:
//
//                viewHolder.setTopPostTextFields(beans.getBeanText(), customUser.getUserDisplayName());
//
//                break;
//
//            case CommentsViewHolder.COMMENT_VIEW_LAYOUT_TYPE:
//
//                viewHolder.setCommentTextFields(model.getCommentText(), model.getCustomUser().getUserDisplayName(), DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());
//
//                break;
//
//            default:
//
//                throw new IllegalArgumentException("Invalid view type");
//
//        }


//    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {

            return CommentsViewHolder.POST_HEADER_LAYOUT_TYPE;

        } else {

            return CommentsViewHolder.COMMENT_VIEW_LAYOUT_TYPE;

        }
    }

}





