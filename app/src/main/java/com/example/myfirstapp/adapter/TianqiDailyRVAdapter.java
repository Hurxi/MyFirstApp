package com.example.myfirstapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.bean.WeatherDailyResponse;

import java.util.List;

/**
 * Created by 若希 on 2017/5/18.
 */

public class TianqiDailyRVAdapter extends RecyclerView.Adapter<TianqiDailyRVAdapter.ViewHolder> {
    Context context;
    List<WeatherDailyResponse.ResultsBean.DailyBean> dailyList;
    public TianqiDailyRVAdapter(Context context, List<WeatherDailyResponse.ResultsBean.DailyBean> dailyList) {
        this.context=context;
        this.dailyList=dailyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_daily_tianqi,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherDailyResponse.ResultsBean.DailyBean daily=dailyList.get(position);
        String a;
        if (position==0){
            a="今天";
        }
        else if (position==1){
            a="明天";
        }
        else {
            a="后天";
        }
        String dates[]=daily.getDate().split("-");
        String date=dates[1]+"-"+dates[2];
        holder.tvDate.setText(a+" "+date);
        if (daily.getCode_day().equals("0")){
            holder.imageView.setImageResource(R.mipmap.a);
            holder.tvName.setText("  " +daily.getText_day());
        }
        else if (daily.getCode_day().equals("4")){
            holder.imageView.setImageResource(R.mipmap.f);
            holder.tvName.setText(daily.getText_day());
        }
        else {
            holder.imageView.setImageResource(R.mipmap.j);
            holder.tvName.setText("  " +daily.getText_day());
        }
        holder.tvTemp.setText(daily.getHigh()+"℃ ~ "+daily.getLow()+"℃");
    }

    @Override
    public int getItemCount() {
        return dailyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvName,tvTemp;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tv_date_daily_tianqi);
            tvName=(TextView)itemView.findViewById(R.id.tv_name_daily_tianqi);
            tvTemp=(TextView)itemView.findViewById(R.id.tv_temp_daily_tianqi);
            imageView=(ImageView)itemView.findViewById(R.id.iv_item_daily_tianqi);
        }
    }
}
