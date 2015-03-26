package com.janderson.gtnextbus.items;

public class NavDrawerItem {

    private String title;
    private int icon;
    private boolean isIcon;

    public NavDrawerItem(String title, int icon, boolean isIcon){
        this.title = title;
        this.icon = icon;
        this.isIcon = isIcon;
    }

    public NavDrawerItem(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public boolean getIconVisibility(){
        return this.isIcon;
    }
}