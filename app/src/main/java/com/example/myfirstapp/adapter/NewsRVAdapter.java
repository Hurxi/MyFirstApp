package com.example.myfirstapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfirstapp.NewsDetailActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.bean.NewsResponse;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 若希 on 2017/5/17.
 */

public class NewsRVAdapter extends RecyclerView.Adapter<NewsRVAdapter.ViewHolder> {

    Context context;
    List<NewsResponse.ResultBean.DataBean> newsList;
    String userName;
    public NewsRVAdapter(Context context, List<NewsResponse.ResultBean.DataBean> newsList,String userName) {
        this.context=context;
        this.newsList=newsList;
        this.userName=userName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_news,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NewsResponse.ResultBean.DataBean news=newsList.get(position);
        holder.tvTitle.setText(news.getTitle());
        if (news.getThumbnail_pic_s()!=null){
            holder.ivNews.setVisibility(View.VISIBLE);
            Picasso.with(context).load(news.getThumbnail_pic_s())
                    .placeholder(R.color.colorPrimary)
                    .into(holder.ivNews);
        }

        holder.tvAuthor.setText(news.getAuthor_name());
        String pubTime=news.getDate();
        SimpleDateFormat a=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        Date dateNow=new Date();
        try {
            date=a.parse(pubTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar=Calendar.getInstance();
        Calendar calendarNow=Calendar.getInstance();
        calendar.setTime(date);
        calendarNow.setTime(dateNow);
        int years=calendarNow.get(Calendar.YEAR)-calendar.get(Calendar.YEAR);
        int months=calendarNow.get(Calendar.MONTH)-calendar.get(Calendar.MONTH);
        int days=calendarNow.get(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH);
        int hours=calendarNow.get(Calendar.HOUR_OF_DAY)-calendar.get(Calendar.HOUR_OF_DAY);
        int minutes=calendarNow.get(Calendar.MINUTE)-calendar.get(Calendar.MINUTE);
        int seconds=calendarNow.get(Calendar.SECOND)-calendar.get(Calendar.SECOND);
        if (years>0){
            holder.tvTime.setText(years+"年前");
        }
        else {
            if (months>0){
                holder.tvTime.setText(months+"月前");
            }
            else {
                if (days>0){
                    holder.tvTime.setText(days+"日前");
                }
                else {
                    if (hours>0){
                        holder.tvTime.setText(hours+"小时前");
                    }
                    else {
                        if (minutes>0){
                            holder.tvTime.setText(minutes+"分前");
                        }
                        else
                            holder.tvTime.setText("刚刚");
                    }
                }
            }
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, NewsDetailActivity.class);
                intent.putExtra("url",news.getUrl());
                intent.putExtra("userName",userName);
                intent.putExtra("newsId",news.getUniquekey());
                intent.putExtra("newsTitle",news.getTitle());
                if (news.getThumbnail_pic_s()==null){
                    intent.putExtra("newsPhoto","");
                }
                else {
                    intent.putExtra("newsPhoto",news.getThumbnail_pic_s());
                }

                intent.putExtra("newsAuthor",news.getAuthor_name());
                intent.putExtra("newsUrl",news.getUrl());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivNews;
        TextView tvTitle,tvAuthor,tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView) itemView;
            ivNews=(ImageView)itemView.findViewById(R.id.iv_news);
            tvTitle=(TextView) itemView.findViewById(R.id.tv_title_news);
            tvAuthor=(TextView) itemView.findViewById(R.id.tv_author_news);
            tvTime=(TextView) itemView.findViewById(R.id.tv_time_news);

        }
    }
}
