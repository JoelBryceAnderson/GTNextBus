package com.janderson.gtnextbus.items;

/**
 * Created by JoelAnderson on 5/16/14.
 */
public class RouteItem {

    private String stop;
    private int icon;
    private boolean isIcon;


    public RouteItem(){}

    public RouteItem(String stop){
        this.stop = stop;
        this.isIcon = false;
    }

    public RouteItem(String title, int icon, boolean isIcon){
        this.stop = title;
        this.icon = icon;
        this.isIcon = isIcon;
    }


    public String getTitle(){
        return this.stop;
    }

    public int getIcon(){
        return this.icon;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public boolean getIconVisibility(){
        return this.isIcon;
    }

    public void setIconVisibility(boolean isIcon){
        this.isIcon = isIcon;
    }
}

