package com.janderson.gtnextbus.items;

/**
 * Created by JoelAnderson on 5/16/14.
 */
public class StopItem {

    private String stop;
    private String color;
    private int icon;
    private boolean isIcon;

    public StopItem(){}

    public StopItem(String stop){
        this.stop = stop;
    }

    public StopItem(String stop, String color){
        this.stop = stop;
        this.color = color;
    }

    public StopItem(String stop, String color, int icon, boolean isIcon){
        this.stop = stop;
        this.color = color;
        this.icon = icon;
        this.isIcon = isIcon;
    }


    public String getTitle(){
        return this.stop;
    }

    public String getColor() {
        return this.color;
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
