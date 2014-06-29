package com.janderson.gtnextbus.items;

/**
 * Created by JoelAnderson on 5/16/14.
 */
public class StopItem {

    private String stop;
    private String color;

    public StopItem(){}

    public StopItem(String stop){
        this.stop = stop;
    }

    public StopItem(String stop, String color){
        this.stop = stop;
        this.color = color;
    }


    public String getTitle(){
        return this.stop;
    }

    public String getColor() {
        return this.color;
    }
}
