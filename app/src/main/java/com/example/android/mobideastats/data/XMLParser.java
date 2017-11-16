package com.example.android.mobideastats.data;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class XMLParser {

    public static ArrayList<DataItem> parseFeed(String content) {


        try {

            boolean inItemTag = false;
            String currentTagName = "";
            DataItem currentItem = null;
            ArrayList<DataItem> itemList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();

                        if (currentTagName.equals("StatItem")) {
                            inItemTag = true;
                            currentItem = new DataItem();
                            itemList.add(currentItem);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            inItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        String text = parser.getText();


                        if (inItemTag && currentItem != null) {
                            try {
                                switch (currentTagName) {

                                    case "Date":
                                        currentItem.setmDate(text);
                                        break;
                                    case "Hour":
                                        currentItem.setmHour(Integer.parseInt(text));
                                        break;
                                    case "Website":
                                        currentItem.setmCampaign(text);
                                        break;
                                    case "Operator":
                                        currentItem.setmOperator(text);
                                        break;
                                    case "Country":
                                        currentItem.setmCountry(text);
                                        break;
                                    case "Revenue":
                                        currentItem.setmRevenue(Double.parseDouble(text));
                                    default:
                                        break;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                }

                eventType = parser.next();

            } // end while loop

                return itemList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }
}