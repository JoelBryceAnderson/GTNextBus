package com.janderson.gtnextbus.adapters;

/**
 * Created by JoelAnderson on 5/15/14.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.items.StopItem;

import java.util.ArrayList;

public class FavoriteAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StopItem> stopItems;
    ViewHolder holder = new ViewHolder();
    View.OnTouchListener mTouchListener;

    public FavoriteAdapter(Context context, ArrayList<StopItem> stopItems) {
        this.context = context;
        this.stopItems = stopItems;
    }

    public FavoriteAdapter(Context context, ArrayList<StopItem> stopItems,
                           View.OnTouchListener listener) {
        this.context = context;
        this.stopItems = stopItems;
        mTouchListener = listener;
    }

    @Override
    public int getCount() {
        return stopItems.size();
    }

    @Override
    public Object getItem(int position) {
        return stopItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.card, null);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.card_image);
            holder.mText = (TextView) convertView.findViewById(R.id.stop);
            convertView.setTag(R.id.id_one, holder.mText);
            convertView.setTag(R.id.id_two, holder.imgIcon);
        } else {
            holder.mText = (TextView) convertView.getTag(R.id.id_one);
            holder.imgIcon = (ImageView) convertView.getTag(R.id.id_two);
        }
        holder.imgIcon.setImageResource(stopItems.get(position).getIcon());
        holder.imgIcon.setVisibility(View.VISIBLE);
        holder.mText.setText(stopItems.get(position).getTitle());
        holder.mText.setTextColor(Color.parseColor(stopItems.get(position).getColor()));
        if (position == 0) {
            holder.mText.setTypeface(null, Typeface.BOLD_ITALIC);
            holder.mText.setTextSize(30);
        } else {
            holder.mText.setTextSize(20);
            holder.mText.setTypeface(null, Typeface.ITALIC);
        }
        if (position == 0) {
            convertView.setEnabled(false);
        }
        if (isEnabled(position)) {
            convertView.setOnTouchListener(mTouchListener);
        }

        if(!stopItems.get(position).getIconVisibility()){
            holder.imgIcon.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView mText;
    }

    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            return false;
        }
        return true;
    }

}
