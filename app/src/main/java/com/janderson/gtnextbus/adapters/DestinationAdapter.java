package com.janderson.gtnextbus.adapters;

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
import com.janderson.gtnextbus.items.RouteItem;

import java.util.ArrayList;

public class DestinationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<RouteItem> destinationItems;
    private int color;
    private boolean headerIsDisabled;


    ViewHolder holder = new ViewHolder();

    public DestinationAdapter(Context context,
                              ArrayList<RouteItem> destinationItems, boolean headerIsDisabled){
        this.headerIsDisabled = headerIsDisabled;
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
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.card_image);
            holder.mText = (TextView) convertView.findViewById(R.id.stop);
            convertView.setTag(R.id.id_one, holder.mText);
            convertView.setTag(R.id.id_two, holder.imgIcon);
        } else {
            holder.mText = (TextView) convertView.getTag(R.id.id_one);
            holder.imgIcon = (ImageView) convertView.getTag(R.id.id_two);
        }
        holder.imgIcon.setImageResource(destinationItems.get(position).getIcon());
        holder.imgIcon.setVisibility(View.VISIBLE);
        holder.mText.setText(destinationItems.get(position).getTitle());

        boolean boldFirst = false;

        if (holder.mText.getText().toString().equals("Red Route")) {
            color = Color.parseColor("#F44336");
            boldFirst = true;
        } else  if (holder.mText.getText().toString().equals("Blue Route")) {
            color = Color.parseColor("#2196F3");
            boldFirst = true;
        } else if (holder.mText.getText().toString().equals("To Technology Enterprise Park")) {
            color = Color.parseColor("#4CAF50");
        } else if (holder.mText.getText().toString().equals("To Marta Station")) {
            color = Color.parseColor("#FFC107");
        } else if (holder.mText.getText().toString().equals("To CULC")) {
            color = Color.parseColor("#9C27B0");
        } else if (holder.mText.getText().toString().equals("To Emory")) {
            color = Color.parseColor("#000080");
        } else if (holder.mText.getText().toString().equals("Favorite Stops")) {
            color = Color.parseColor("#000000");
        }

        if(!destinationItems.get(position).getIconVisibility()){
            holder.imgIcon.setVisibility(View.GONE);
        }

        holder.mText.setTextColor(color);
        holder.mText.setTextSize(20);
        holder.mText.setTypeface(null, Typeface.ITALIC);

        if (boldFirst) {
            if (position == 0) {
                holder.mText.setTypeface(null, Typeface.BOLD_ITALIC);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView mText;
        ImageView imgIcon;
    }

    @Override
    public boolean isEnabled(int position) {
        if (headerIsDisabled) {
            if (position == 0) {
                return false;
            }
        }
        return true;
    }
}
