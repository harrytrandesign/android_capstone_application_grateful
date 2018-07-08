package com.htdwps.grateful.Util;

import android.content.Context;
import android.view.View;

/**
 * Created by HTDWPS on 7/7/18.
 */
public class RecyclerViewClickTest implements View.OnClickListener {

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecyclerViewClickTest(Context context, OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {

    }
}
