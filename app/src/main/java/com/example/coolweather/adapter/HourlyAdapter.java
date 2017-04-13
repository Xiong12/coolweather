package com.example.coolweather.adapter;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.db.HourlyWeather;

import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder>{

    private List<HourlyWeather> mHourlyList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView hourlyData;
        TextView hourlyTxt;
        TextView hourlyTmp;

        public ViewHolder(View view) {
            super(view);
            hourlyData = (TextView) view.findViewById(R.id.hourly_text);
            hourlyTxt = (TextView) view.findViewById(R.id.weather_text);
            hourlyTmp= (TextView) view.findViewById(R.id.tmp_text);
        }
    }

    public HourlyAdapter(List<HourlyWeather> hourlyList) {
        mHourlyList = hourlyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourlyweather, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourlyWeather hourly = mHourlyList.get(position);
        holder.hourlyData.setText(hourly.getData());
        holder.hourlyTmp.setText(hourly.getTmp());
        holder.hourlyTxt.setText( hourly.getTxt() );
    }

    @Override
    public int getItemCount() {
        return mHourlyList.size();
    }

}