package com.example.android.mobideastats.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mobideastats.R;

import java.util.ArrayList;


public class DailyListAdapter extends RecyclerView.Adapter<DailyListAdapter.ViewHolder> {

    private ArrayList<DailyDataItem> mItems;
    private Context mContext;

    public DailyListAdapter(Context context, ArrayList<DailyDataItem> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public DailyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_daily, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DailyListAdapter.ViewHolder holder, int position) {
        DailyDataItem item = mItems.get(position);


        holder.tv1.setText(item.getmDate());

        holder.tvRevenue.setText("$" + String.format("%.2f", item.getRevenue()));


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv1;
        public TextView tvRevenue;

        public ViewHolder(View itemView) {
            super(itemView);

            tv1 = (TextView) itemView.findViewById(R.id.tvDateOfConversion);
            tvRevenue = (TextView) itemView.findViewById(R.id.tvrevenue);
        }
    }
}