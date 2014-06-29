package com.janderson.gtnextbus.items;

/**
 * Created by JoelAnderson on 5/16/14.
 */
public class RouteItem {

    private String stop;

    public RouteItem(){}

    public RouteItem(String stop){
        this.stop = stop;
    }


    public String getTitle(){
        return this.stop;
    }
}
