package com.example.myfirstapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfirstapp.HisDetailActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.bean.HistoryResponse;

import java.util.List;

/**
 * Created by 若希 on 2017/5/22.
 */

public class HistoryRVAdapter extends RecyclerView.Adapter<HistoryRVAdapter.ViewHolder> {
    List<HistoryResponse.ResultBean> historyList;
    Context context;
    String userName;
    public HistoryRVAdapter(List<HistoryResponse.ResultBean> historyList, Context context,
                            String userName) {
        this.historyList = historyList;
        this.context = context;
        this.userName=userName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_his_fragment,parent,false);
        ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HistoryResponse.ResultBean history=historyList.get(position);
        holder.tvTime.setText(history.getYear()+"年");
        holder.tvTitle.setText(history.getTitle());
        if (position==historyList.size()-1){
            holder.llLast.setVisibility(View.VISIBLE);
        }
        else {
            holder.llLast.setVisibility(View.GONE);
        }
        holder.llHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, HisDetailActivity.class);
                intent.putExtra("title",history.getTitle());
                intent.putExtra("lunar",history.getLunar());
                intent.putExtra("des",history.getDes());
                intent.putExtra("id",history.get_id());
                intent.putExtra("pic",history.getPic());
                intent.putExtra("year",history.getYear());
                intent.putExtra("month",history.getMonth());
                intent.putExtra("day",history.getDay());
                intent.putExtra("userName",userName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime,tvTitle;
        LinearLayout llHis,llLast;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTime=(TextView)itemView.findViewById(R.id.tv_time_item_his);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_content_item_his);
            llHis=(LinearLayout)itemView.findViewById(R.id.ll_item_his);
            llLast=(LinearLayout)itemView.findViewById(R.id.ll_last_item_his);
        }
    }
}
