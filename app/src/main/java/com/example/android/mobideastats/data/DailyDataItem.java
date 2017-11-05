package com.example.android.mobideastats.data;

/**
 * Created by W567 on 03.11.2017..
 */

public class DailyDataItem {
    private String mDate;
    private double revenue;


    public DailyDataItem(String mDate, double revenue) {
        this.mDate = mDate;
        this.revenue = revenue;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public String getmDate() {
        return mDate;
    }
}