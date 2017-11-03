package com.example.android.mobideastats.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mobideastats.R;

import java.util.ArrayList;


public class ConversionListAdapter extends RecyclerView.Adapter<ConversionListAdapter.ViewHolder> {

    private ArrayList<DataItem> mItems;
    private Context mContext;

    public ConversionListAdapter(Context context, ArrayList<DataItem> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public ConversionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_conversion, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ConversionListAdapter.ViewHolder holder, int position) {
        DataItem item = mItems.get(position);


        holder.tv1.setText(item.getmDate());
//        holder.tv2.setText(item.getmCampaign());
        holder.tv3.setText(item.getmCountry());
        holder.tvRevenue.setText("$" + String.format("%.2f", item.getmRevenue()));


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tvRevenue;

        public ViewHolder(View itemView) {
            super(itemView);

            tv1 = (TextView) itemView.findViewById(R.id.tvDateOfConversion);
//            tv2 = (TextView) itemView.findViewById(R.id.textView2);
            tv3 = (TextView) itemView.findViewById(R.id.tvCountryCode);
            tvRevenue = (TextView) itemView.findViewById(R.id.tvrevenue);
        }
    }
}