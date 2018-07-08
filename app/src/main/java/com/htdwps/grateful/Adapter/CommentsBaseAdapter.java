package com.htdwps.grateful.Adapter;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.htdwps.grateful.Model.GratefulComment;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Viewholder.CommentsViewHolder;

/**
 * Created by HTDWPS on 7/7/18.
 */
public class CommentsBaseAdapter extends FirebaseRecyclerAdapter<GratefulComment, CommentsViewHolder> {

    public CommentsBaseAdapter(Class<GratefulComment> modelClass, int modelLayout, Class<CommentsViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
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

        int viewType = getItemViewType(position);

        switch (viewType) {

            case CommentsViewHolder.POST_HEADER_LAYOUT_TYPE:



                break;

            case CommentsViewHolder.COMMENT_VIEW_LAYOUT_TYPE:

                viewHolder.setCommentTextFields(model.getCommentText(), model.getCustomUser().getUserDisplayName(), DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());

                break;

            default:

                throw new IllegalArgumentException("Invalid view type");

        }

    }

    @Override
    public void onBindViewHolder(CommentsViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
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





