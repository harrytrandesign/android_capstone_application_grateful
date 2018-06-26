package com.htdwps.grateful.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.htdwps.grateful.R;

/**
 * Created by HTDWPS on 6/22/18.
 */
public class CustomSpinnerArrayAdapter extends ArrayAdapter<String> {

    private LayoutInflater mInflater;
    private Context mContext;
    private String[] mFeelings;
    private int mResource;

    public CustomSpinnerArrayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mFeelings = objects;

    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        TextView feelingText = view.findViewById(R.id.tv_mood_description_text);

        String feeling = mFeelings[position];

        feelingText.setText(feeling);

        return view;
    }
}
