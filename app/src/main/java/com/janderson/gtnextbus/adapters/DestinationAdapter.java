package com.janderson.gtnextbus.adapters;

/**
 * Created by JoelAnderson on 5/16/14.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JoelAnderson on 5/15/14.
 */


import android.graphics.Typeface;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.items.RouteItem;

public class DestinationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<RouteItem> destinationItems;
    private int color;
    private int headerSize;

    ViewHolder holder = new ViewHolder();

    public DestinationAdapter(Context context, ArrayList<RouteItem> destinationItems){
        this.context = context;
        this.destinationItems = destinationItems;
    }

    @Override
    public int getCount() {
        return destinationItems.size();
    }

    @Override
    public Object getItem(int position) {
        return destinationItems.get(position);
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
            holder.mText = (TextView) convertView.findViewById(R.id.stop);
            convertView.setTag(holder.mText);
        } else {
            holder.mText = (TextView) convertView.getTag();
        }

        holder.mText.setText(destinationItems.get(position).getTitle());

        if (holder.mText.getText().toString().equals("Red Route")) {
            color = Color.parseColor("#CC0000");
            headerSize = 30;
        }

        if (holder.mText.getText().toString().equals("Blue Route")) {
            color = Color.parseColor("#0000FF");
            headerSize = 30;
        }

        if (holder.mText.getText().toString().equals("Green Route Destinations")) {
            color = Color.parseColor("#669900");
            headerSize = 25;
        }

        if (holder.mText.getText().toString().equals("Tech Trolley Destinations")) {
            color = Color.parseColor("#FFBB33");
            headerSize = 25;
        }

        if (holder.mText.getText().toString().equals("Midnight Rambler Destinations")) {
            color = Color.parseColor("#9933CC");
            headerSize = 22;
        }

        if (holder.mText.getText().toString().equals("Emory Shuttle Destinations")) {
            color = Color.parseColor("#000080");
            headerSize = 25;
        }

        if (holder.mText.getText().toString().equals("Favorite Stops")) {
            color = Color.parseColor("#000000");
            headerSize = 25;
        }

        holder.mText.setTextColor(color);

        if (position == 0) {
            holder.mText.setTextSize(headerSize);
            holder.mText.setTypeface(null, Typeface.BOLD_ITALIC);
        } else {
            holder.mText.setTextSize(20);
            holder.mText.setTypeface(null, Typeface.ITALIC);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView mText;
    }
}
