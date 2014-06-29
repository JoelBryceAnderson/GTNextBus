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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.items.StopItem;

import java.util.ArrayList;

public class StopAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StopItem> stopItems;
    private String color;

    public StopAdapter(Context context, ArrayList<StopItem> stopItems, String color) {
        this.context = context;
        this.stopItems = stopItems;
        this.color = color;
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
            TextView txtTitle = (TextView) convertView.findViewById(R.id.stop);
            txtTitle.setText(stopItems.get(position).getTitle());
            txtTitle.setTextColor(Color.parseColor(color));
            if (position == 0) {
                txtTitle.setTextSize(30);
                txtTitle.setTypeface(null, Typeface.BOLD_ITALIC);
            }
        }
        Animation animation =  AnimationUtils.loadAnimation(context, R.anim.fly_up);
        long offSet = (long) (position * 50);
        animation.setStartOffset(offSet);
        convertView.setAnimation(animation);
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            return false;
        }
        return true;
    }


}
