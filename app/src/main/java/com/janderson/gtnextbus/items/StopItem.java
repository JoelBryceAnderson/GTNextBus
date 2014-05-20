package com.janderson.gtnextbus.items;

/**
 * Created by JoelAnderson on 5/16/14.
 */
public class StopItem {

    private String stop;

    public StopItem(){}

    public StopItem(String stop){
        this.stop = stop;
    }


    public String getTitle(){
        return this.stop;
    }
}
