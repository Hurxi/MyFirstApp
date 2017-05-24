package com.example.myfirstapp.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myfirstapp.HisDetailActivity;
import com.example.myfirstapp.ImageActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.bean.NewsResponse;
import com.example.myfirstapp.bean.SmileImageResponse;
import com.example.myfirstapp.utils.MyDBHelper;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 若希 on 2017/5/23.
 */

public class SmileImageAdapter extends RecyclerView.Adapter<SmileImageAdapter.ViewHolder> {

    Context context;
    List<SmileImageResponse.ResultBean.DataBean> smileImageList;
    String userName;
    private MyDBHelper dbHelper;
    private String[] textArray;
    public SmileImageAdapter(Context context, List<SmileImageResponse.ResultBean.DataBean> smileImageList, String userName) {
        this.context = context;
        this.smileImageList = smileImageList;
        this.userName = userName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_image_fra,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SmileImageResponse.ResultBean.DataBean smileImage=smileImageList.get(position);
        holder.tvTitle.setText(smileImage.getContent());
        Picasso.with(context).load(smileImage.getUrl())
                .placeholder(R.color.colorPrimary)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ImageActivity.class);
                intent.putExtra("url",smileImage.getUrl());
                context.startActivity(intent);
            }
        });
        String pubTime=smileImage.getUpdatetime();
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
        dbHelper = new MyDBHelper(context, "MyUser.db", null, 4);
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        Cursor cursor=db.query("UserInfo",null,
                "userName=? ",new String[]{userName},null,null,null);
        if(cursor.moveToFirst()) {
            String i = cursor.getString(4);
            if (i!=null){
                String ids[]=i.split(",");
                for (int i1 = 0; i1 < ids.length; i1++) {
                    if (ids[i1].equals(smileImage.getHashId())){
                        textArray[position]="1";
                    }
                }
            }
            else
            {
                textArray[position]="0";
            }
            if (position>=20){
                initTextArray();
            }

            if (textArray[position].equals("1")){
                holder.ibFav.setBackgroundResource(R.drawable.ic_favorite_red_500_24dp);
            }
            else {
                holder.ibFav.setBackgroundResource(R.drawable.ic_favorite_grey_400_24dp);
            }
        }
        holder.ibFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textArray[position].equals("0")){
                    holder.ibFav.setBackgroundResource(R.drawable.ic_favorite_red_500_24dp);
                    SQLiteDatabase db=dbHelper.getWritableDatabase();
                    //数据库 有则拿到 无则创建
                    Cursor cursor=db.query("UserInfo",null,
                            "userName=? ",new String[]{userName},null,null,null);
                    String oldNewsId="";
                    int collectCount=0;
                    String collectTitle="";
                    String collectTime="";
                    String collectPhoto="";
                    String nowTime="";
                    String collectType="";
                    String collectAuthor="";
                    String collectUrl="";
                    Date date=new Date();
                    SimpleDateFormat a=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    nowTime=a.format(date);
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
                    ContentValues values=new ContentValues();
                    if (oldNewsId==null){
                        values.put("collectId",smileImage.getHashId());
                        values.put("collectCount",collectCount+1);
                        values.put("collectType","4");
                        values.put("collectTitle",smileImage.getContent());
                        values.put("collectAuthor","***");
                        values.put("collectUrl","***");
                        values.put("collectPhoto",smileImage.getUrl());
                        values.put("collectTime",nowTime);
                    }
                    else {
                        values.put("collectId",smileImage.getHashId()+","+oldNewsId);
                        values.put("collectCount",collectCount+1);
                        values.put("collectType","4"+","+collectType);
                        values.put("collectTitle",smileImage.getContent()+","+collectTitle);
                        values.put("collectAuthor","***"+","+collectAuthor);
                        values.put("collectUrl","***"+","+collectUrl);
                        values.put("collectPhoto",smileImage.getUrl()+","+collectPhoto);
                        values.put("collectTime",nowTime+","+collectTime);
                    }

                    long id=db.update("UserInfo",values,"userName = ?",new String[]{userName});
                    if(id!=-1) {
                        Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context,"收藏失败",Toast.LENGTH_SHORT).show();
                    }
                    textArray[position]="1";

                }
                else {
                    holder.ibFav.setBackgroundResource(R.drawable.ic_favorite_grey_400_24dp);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    //数据库 有则拿到 无则创建
                    Cursor cursor = db.query("UserInfo", null,
                            "userName=? ", new String[]{userName}, null, null, null);
                    String oldNewsId = "";
                    int collectCount = 0;
                    String collectTitle = "";
                    String collectAuthor="";
                    String collectUrl="";
                    String collectPhoto="";
                    String collectTime = "";
                    String collectType = "";
                    if (cursor.moveToFirst()) {
                        oldNewsId = cursor.getString(4);
                        collectCount = cursor.getInt(5);
                        collectType = cursor.getString(6);
                        collectTitle = cursor.getString(7);
                        collectAuthor=cursor.getString(8);
                        collectUrl=cursor.getString(9);
                        collectPhoto=cursor.getString(10);
                        collectTime = cursor.getString(11);
                    }
                    int number = 0;
                    String numbers[] = oldNewsId.split(",");
                    for (int i = 0; i < numbers.length; i++) {
                        if (numbers[i].equals(smileImage.getHashId())) {
                            number = i;
                        }
                    }
                    String newNewsId = "";
                    String oldIds[] = oldNewsId.split(smileImage.getHashId() + ",");
                    for (int i = 0; i < oldIds.length; i++) {
                        if (number != i) {
                            if (newNewsId!=""){
                                newNewsId = oldIds[i]+"," + newNewsId;
                            }
                            else {
                                newNewsId = oldIds[i];
                            }
                        }
                    }
                    String newCollectType = "";
                    String collectTypes[] = collectType.split(",");
                    for (int i = 0; i < collectTypes.length; i++) {
                        if (number != i) {
                            if (newCollectType!=""){
                                newCollectType = collectTypes[i]+"," + newCollectType;
                            }
                            else {
                                newCollectType = collectTypes[i];
                            }
                        }
                    }

                    String newCollectTitle = "";
                    String collectTitles[] = collectTitle.split(smileImage.getContent() + ",");
                    for (int i = 0; i < collectTitles.length; i++) {
                        if (number != i) {
                            if (newCollectTitle!=""){
                                newCollectTitle = collectTitles[i]+"," + newCollectTitle;
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
                                newCollectAuthor = collectAuthors[i]+"," + newCollectAuthor;
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
                    String collectPhotos[]=collectPhoto.split(smileImage.getUrl()+",");
                    for (int i = 0; i < collectPhotos.length; i++) {
                        if (number!=i) {
                            if (newCollectPhoto!=""){
                                newCollectPhoto = collectPhotos[i]+"," + newCollectPhoto;
                            }
                            else {
                                newCollectPhoto = collectPhotos[i];
                            }
                        }
                    }

                    String newCollectTime = "";
                    String collectTimes[] = collectTime.split(",");
                    for (int i = 0; i < collectTimes.length; i++) {
                        if (number != i) {
                            if (newCollectTime!=""){
                                newCollectTime = collectTimes[i]+"," + newCollectTime;
                            }
                            else {
                                newCollectTime = collectTimes[i];
                            }
                        }
                    }
                    ContentValues values = new ContentValues();
                    values.put("collectId", newNewsId);
                    values.put("collectCount", collectCount - 1);
                    values.put("collectType", newCollectType);
                    values.put("collectTitle", newCollectTitle);
                    values.put("collectAuthor",newCollectAuthor);
                    values.put("collectUrl",newCollectUrl);
                    values.put("collectPhoto",newCollectPhoto);
                    values.put("collectTime", newCollectTime);
                    long id = db.update("UserInfo", values, "userName = ?", new String[]{userName});
                    if (id != -1) {
                        Toast.makeText(context, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "取消收藏失败", Toast.LENGTH_SHORT).show();
                    }
                    textArray[position]="0";
                }
            }
        });
        holder.ibEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMWeb web = new UMWeb("http://www.baidu.com");
                web.setTitle(smileImage.getContent());
                web.setDescription("分享自OSChina");
                new ShareAction((Activity)context)
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
                            }
                        }).open();
            }
        });

                }

    @Override
    public int getItemCount() {
        return smileImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTime,tvTitle;
        ImageView imageView;
        ImageButton ibFav,ibEmail;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView;
            tvTime=(TextView)itemView.findViewById(R.id.tv_time_image_smile);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title_image_smile);
            imageView=(ImageView)itemView.findViewById(R.id.iv_image_smile);
            ibFav=(ImageButton)itemView.findViewById(R.id.ib_fav_item_smile_image);
            ibEmail=(ImageButton)itemView.findViewById(R.id.ib_emil_item_smile_image);
            initTextArray();
        }
    }
    public  void initTextArray(){
        textArray = new String[smileImageList.size()];
        for (int i = 0; i < textArray.length; i++) {
            textArray[i]="0";
        }
    }
}
