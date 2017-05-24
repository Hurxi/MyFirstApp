package com.example.myfirstapp.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.HisDetailActivity;
import com.example.myfirstapp.ImageActivity;
import com.example.myfirstapp.NewsDetailActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.bean.CollectData;
import com.example.myfirstapp.bean.NewsResponse;
import com.example.myfirstapp.utils.MyDBHelper;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 若希 on 2017/5/17.
 */

public class CollectRVAdapter extends RecyclerView.Adapter<CollectRVAdapter.ViewHolder> {

    Context context;
    List<CollectData> collectList;
    String userName;
    public CollectRVAdapter(Context context, List<CollectData> collectList,String userName) {
        this.context=context;
        this.collectList=collectList;
        this.userName=userName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_collect,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CollectData collects=collectList.get(position);
        holder.tvTitle.setText(collects.getCollectTitle());
        if (!collects.getCollectPhoto().isEmpty()&&!collects.getCollectPhoto().equals("***")){
            Picasso.with(context).load(collects.getCollectPhoto())
                    .placeholder(R.color.colorPrimary)
                    .into(holder.ivNews);
            holder.ivNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,ImageActivity.class);
                    intent.putExtra("url",collects.getCollectPhoto());
                    context.startActivity(intent);
                }
            });
        }
        else {
            holder.ivNews.setVisibility(View.GONE);
        }
        if (collects.getCollectType().equals("1")){
            holder.tvType.setText("新闻");
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, NewsDetailActivity.class);
                    intent.putExtra("url",collects.getCollectUrl());
                    intent.putExtra("userName",userName);
                    intent.putExtra("newsId",collects.getCollectId());
                    intent.putExtra("newsTitle",collects.getCollectTitle());
                    intent.putExtra("newsPhoto",collects.getCollectPhoto());
                    intent.putExtra("newsType",collects.getCollectType());
                    intent.putExtra("newsUrl",collects.getCollectUrl());
                    context.startActivity(intent);
                }
            });
        }
        else if (collects.getCollectType().equals("2")){
            holder.tvType.setText("历史");
        }
        else if (collects.getCollectType().equals("3")){
            holder.tvType.setText("笑话");
        }
        else if (collects.getCollectType().equals("4")){
            holder.tvType.setText("趣图");
        }
        holder.tvTime.setText(collects.getCollectTime());

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("提示")
                        .setMessage("是否确定删除这条收藏？")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collectList.remove(position);
                                MyDBHelper dbHelper = new MyDBHelper(context,"MyUser.db",null,4);
                                SQLiteDatabase db=dbHelper.getWritableDatabase();
                                //数据库 有则拿到 无则创建
                                Cursor cursor=db.query("UserInfo",null,
                                        "userName=? ",new String[]{userName},null,null,null);
                                String oldNewsId="";
                                int collectCount=0;
                                String collectTitle="";
                                String collectAuthor="";
                                String collectUrl="";
                                String collectPhoto="";
                                String collectTime="";
                                String collectType="";
                                if(cursor.moveToFirst()) {
                                    oldNewsId = cursor.getString(4);
                                    collectCount=cursor.getInt(5);
                                    collectType=cursor.getString(6);
                                    collectTitle=cursor.getString(7);
                                    collectAuthor=cursor.getString(8);
                                    collectUrl=cursor.getString(9);
                                    collectPhoto=cursor.getString(10);
                                    collectTime=cursor.getString(11);
                                }
                                int number=0;
                                String numbers[]=oldNewsId.split(",");
                                for (int i = 0; i < numbers.length; i++) {
                                    if (numbers[i].equals(collects.getCollectId())){
                                        number=i;
                                    }
                                }
                                String newNewsId="";
                                String oldIds[]=oldNewsId.split(collects.getCollectId()+",");
                                for (int i = 0; i < oldIds.length; i++) {
                                    if (number!=i) {
                                        if (newNewsId!=""){
                                            newNewsId = oldIds[i] +","+  newNewsId;
                                        }
                                        else {
                                            newNewsId = oldIds[i];
                                        }
                                    }
                                }
                                String newCollectType="";
                                String collectTypes[]=collectType.split(",");
                                for (int i = 0; i < collectTypes.length; i++) {
                                    if (number!=i) {
                                        if (newCollectType!=""){
                                            newCollectType = collectTypes[i] +","+ newCollectType;
                                        }
                                        else {
                                            newCollectType = collectTypes[i];
                                        }
                                    }
                                }

                                String newCollectTitle="";
                                String collectTitles[]=collectTitle.split(collects.getCollectTitle()+",");
                                for (int i = 0; i < collectTitles.length; i++) {
                                    if (number!=i) {
                                        if (newCollectTitle!=""){
                                            newCollectTitle = collectTitles[i] +","+ newCollectTitle;
                                        }
                                        else {
                                            newCollectTitle = collectTitles[i];
                                        }
                                    }
                                }

                                String newCollectAuthor="";
                                String collectAuthors[]=collectAuthor.split(",");
                                for (int i = 0; i < collectAuthors.length; i++) {
                                    if (number!=i) {
                                        if (newCollectAuthor!=""){
                                            newCollectAuthor = collectAuthors[i] +","+ newCollectAuthor;
                                        }
                                        else {
                                            newCollectAuthor = collectAuthors[i];
                                        }
                                    }
                                }

                                String newCollectUrl="";
                                String collectUrls[]=collectUrl.split(",");
                                for (int i = 0; i < collectUrls.length; i++) {
                                    if (number!=i) {
                                        if (newCollectUrl!=""){
                                            newCollectUrl = collectUrls[i]+"," + newCollectUrl;
                                        }
                                        else {
                                            newCollectUrl = collectUrls[i];
                                        }
                                    }
                                }

                                String newCollectPhoto="";
                                String collectPhotos[]=collectPhoto.split(",");
                                for (int i = 0; i < collectPhotos.length; i++) {
                                    if (number!=i) {
                                        if (newCollectPhoto!=""){
                                            newCollectPhoto = collectPhotos[i]+"," + newCollectPhoto;
                                        }
                                        else{
                                            newCollectPhoto = collectPhotos[i];
                                        }
                                    }
                                }

                                String newCollectTime="";
                                String collectTimes[]=collectTime.split(",");
                                for (int i = 0; i < collectTimes.length; i++) {
                                    if (number!=i){
                                        if (newCollectTime!=""){
                                            newCollectTime=collectTimes[i]+","+newCollectTime;
                                        }
                                        else {
                                            newCollectTime=collectTimes[i];
                                        }
                                    }
                                }
                                ContentValues values=new ContentValues();
                                values.put("collectId",newNewsId);
                                values.put("collectCount",collectCount-1);
                                values.put("collectType",newCollectType);
                                values.put("collectTitle",newCollectTitle);
                                values.put("collectAuthor",newCollectAuthor);
                                values.put("collectUrl",newCollectUrl);
                                values.put("collectPhoto",newCollectPhoto);
                                values.put("collectTime",newCollectTime);
                                long id=db.update("UserInfo",values,"userName = ?",new String[]{userName});
                                if(id!=-1) {
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                                }

                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivNews;
        TextView tvTitle,tvType,tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView) itemView;
            ivNews=(ImageView)itemView.findViewById(R.id.iv_collect);
            tvTitle=(TextView) itemView.findViewById(R.id.tv_title_collect);
            tvType=(TextView) itemView.findViewById(R.id.tv_type_collect);
            tvTime=(TextView) itemView.findViewById(R.id.tv_time_collect);

        }
    }
}
