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
import com.janderson.gtnextbus.items.NavDrawerItem;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private TextView txtTitle;
    private int mSelectedItem;

    ViewHolder holder = new ViewHolder();

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }
    public void setSelectedItem(int selectedItem) {
        mSelectedItem = selectedItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.dividerTop = (View) convertView.findViewById(R.id.divider_top);
            holder.dividerBottom = (View) convertView.findViewById(R.id.divider_bottom);
            holder.txtSubTitle = (TextView) convertView.findViewById(R.id.footer_text);
            if (position == mSelectedItem) {
                holder.txtTitle.setTypeface(null, Typeface.BOLD);
            }
            convertView.setTag(R.id.id_one, holder.txtTitle);
            convertView.setTag(R.id.id_two, holder.imgIcon);
            convertView.setTag(R.id.id_three, holder.dividerTop);
            convertView.setTag(R.id.id_four, holder.dividerBottom);
            convertView.setTag(R.id.id_five, holder.txtSubTitle);
        } else {
            holder.txtTitle = (TextView) convertView.getTag(R.id.id_one);
            holder.imgIcon = (ImageView) convertView.getTag(R.id.id_two);
            holder.dividerTop = (View) convertView.getTag(R.id.id_three);
            holder.dividerBottom = (View) convertView.getTag(R.id.id_four);
            holder.txtSubTitle = (TextView) convertView.getTag(R.id.id_five);
        }

        holder.imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        holder.txtSubTitle.setText(navDrawerItems.get(position).getTitle());
        holder.txtTitle.setText(navDrawerItems.get(position).getTitle());
        holder.txtTitle.setVisibility(View.VISIBLE);
        holder.imgIcon.setVisibility(View.VISIBLE);
        holder.txtSubTitle.setVisibility(View.VISIBLE);
        holder.dividerTop.setVisibility(View.VISIBLE);
        holder.dividerBottom.setVisibility(View.VISIBLE);

        if(navDrawerItems.get(position).getIconVisibility()){
            holder.txtTitle.setVisibility(View.GONE);
            convertView.setBackgroundColor(Color.parseColor("#F7F7F7"));
        } else{
            holder.imgIcon.setVisibility(View.GONE);
            holder.txtSubTitle.setVisibility(View.GONE);
            holder.dividerTop.setVisibility(View.GONE);
            holder.dividerBottom.setVisibility(View.GONE);
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txtTitle;
        ImageView imgIcon;
        View dividerTop;
        View dividerBottom;
        TextView txtSubTitle;
    }

}
