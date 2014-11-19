package com.janderson.gtnextbus.adapters;

import android.app.Activity;
import android.content.Context;
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

    public void setSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.dividerTop = convertView.findViewById(R.id.divider_top);
            holder.dividerBottom = convertView.findViewById(R.id.divider_bottom);
            holder.largeImage = (ImageView) convertView.findViewById(R.id.large_image_drawer);
            convertView.setTag(R.id.id_one, holder.txtTitle);
            convertView.setTag(R.id.id_two, holder.imgIcon);
            convertView.setTag(R.id.id_three, holder.dividerTop);
            convertView.setTag(R.id.id_four, holder.dividerBottom);
            convertView.setTag(R.id.id_six, holder.largeImage);
        } else {
            holder.txtTitle = (TextView) convertView.getTag(R.id.id_one);
            holder.imgIcon = (ImageView) convertView.getTag(R.id.id_two);
            holder.dividerTop = (View) convertView.getTag(R.id.id_three);
            holder.dividerBottom = (View) convertView.getTag(R.id.id_four);
            holder.largeImage = (ImageView) convertView.getTag(R.id.id_six);
        }
        holder.imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        holder.txtTitle.setText(navDrawerItems.get(position).getTitle());
        holder.txtTitle.setVisibility(View.VISIBLE);
        holder.imgIcon.setVisibility(View.VISIBLE);
        holder.dividerTop.setVisibility(View.VISIBLE);
        holder.dividerBottom.setVisibility(View.VISIBLE);

        if (position == mSelectedItem) {
            holder.txtTitle.setTypeface(null, Typeface.BOLD);
        } else {
            holder.txtTitle.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        }

        if (position == 0) {
            holder.largeImage.setImageResource(R.drawable.navigation_drawer_header);
            holder.imgIcon.setVisibility(View.GONE);
            holder.dividerTop.setVisibility(View.GONE);
            holder.dividerBottom.setVisibility(View.GONE);
            holder.txtTitle.setVisibility(View.GONE);
        } else {
            holder.largeImage.setVisibility(View.GONE);
        }

        if (position != 8) {
            holder.dividerTop.setVisibility(View.GONE);
            holder.dividerBottom.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txtTitle;
        ImageView imgIcon;
        View dividerTop;
        View dividerBottom;
        ImageView largeImage;
    }

}
