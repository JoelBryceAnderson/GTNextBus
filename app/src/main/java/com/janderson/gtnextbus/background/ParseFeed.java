package com.janderson.gtnextbus.background;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ParseFeed {

    public String secondTime;
    private String stop;
    protected String url;
    public String time;
    public String thirdTime;

    public ParseFeed(String url, String stop, String time, String secondTime, String thirdTime) {
        this.url = url;
        this.stop = stop;
        this.secondTime = secondTime;
        this.time = time;
        this.thirdTime = thirdTime;
        fetchXML();
    }

    public void fetchXML() {
        try {
            time = "No Current Prediction";
            URL feed = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)
                    feed.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
                    , false);
            myparser.setInput(stream, null);
            parseXML(myparser);
            stream.close();
        } catch (Exception e) {
            time = "Error connecting to server. Try refreshing.";
        }
    }

    public void parseXML(XmlPullParser xpp) {
        int event;
        boolean keepGoing = false;
        boolean notYetFirst = true;
        boolean notYetSecond = true;
        boolean notYetThird = true;
        try {
            event = xpp.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name=xpp.getName();
                String att = xpp.getAttributeValue(null, "stopTag");
                String predict = xpp.getAttributeValue(null, "minutes");
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals("predictions") && att.equals(stop)) {
                            keepGoing = true;
                        }
                        if (name.equals("prediction") && keepGoing && notYetFirst) {
                            time = predict + " minutes";

                            notYetFirst = false;
                        } else if (name.equals("prediction") && keepGoing && notYetSecond) {
                            secondTime = predict + " minutes";
                            notYetSecond = false;
                        } else if (name.equals("prediction") && keepGoing && notYetThird) {
                            thirdTime = predict + " minutes";
                            notYetThird = false;
                        }
                        else{
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("predictions") && keepGoing) {
                            keepGoing = false;
                        }
                }
                event = xpp.next();
            }
        } catch (Exception e) {
            time = "Error getting bus times. Try refreshing.";
        }
    }
}


