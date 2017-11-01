package com.example.android.mobideastats.data;

/**
 * Created by W567 on 05.10.2017..
 */

public class DataItem {

    String mDate;
    int mHour;
    String mCampaign;
    int mCampaignId;
    String mOperator;
    String mCountry;
    double mRevenue;

    public DataItem(String mDate, int mHour, String mCampaign, int mCampaignId, String getmCampaignName, String mOperator, String mCountry, double mRevenue) {
        this.mDate = mDate;
        this.mHour = mHour;
        this.mCampaign = mCampaign;
        this.mCampaignId = mCampaignId;
        this.mOperator = mOperator;
        this.mCountry = mCountry;
        this.mRevenue = mRevenue;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }


    public DataItem() {
    }

    public void setmDate(String mDate) {

        this.mDate = mDate;
    }

    public void setmHour(int mHour) {
        this.mHour = mHour;
    }

    public void setmCampaign(String mCampaign) {
        this.mCampaign = mCampaign;
    }

    public void setmCampaignId(int mCampaignId) {
        this.mCampaignId = mCampaignId;
    }



    public void setmOperator(String mOperator) {
        this.mOperator = mOperator;
    }

    public void setmRevenue(double mRevenue) {
        this.mRevenue = mRevenue;
    }


    public String getmDate() {
        return mDate;
    }

    public int getmHour() {
        return mHour;
    }

    public String getmCampaign() {
        return mCampaign;
    }

    public int getmCampaignId() {
        return mCampaignId;
    }

    public String getmOperator() {
        return mOperator;
    }

    public String getmCountry() {
        return mCountry;
    }

    public double getmRevenue() {
        return mRevenue;
    }
}
