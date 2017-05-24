package com.example.myfirstapp.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.HisDetailActivity;
import com.example.myfirstapp.NewsDetailActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.bean.NewsResponse;
import com.example.myfirstapp.bean.SmileTextResponse;
import com.example.myfirstapp.utils.MyDBHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 若希 on 2017/5/22.
 */

public class SmileTextRVAdapter extends RecyclerView.Adapter<SmileTextRVAdapter.ViewHolder> {
    Context context;
    List<SmileTextResponse.ResultBean.DataBean> smileTextList;
    String userName;
    private MyDBHelper dbHelper;
    private String[] textArray;


    public SmileTextRVAdapter(Context context, List<SmileTextResponse.ResultBean.DataBean> smileTextList, String userName) {
        this.context = context;
        this.smileTextList = smileTextList;
        this.userName = userName;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_smile_text_fra,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final SmileTextResponse.ResultBean.DataBean smileText=smileTextList.get(position);
        holder.tvContent.setText(smileText.getContent());
        String pubTime=smileText.getUpdatetime();
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
//        Toast.makeText(context, userName, Toast.LENGTH_SHORT).show();
        dbHelper = new MyDBHelper(context, "MyUser.db", null, 4);
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        Cursor cursor=db.query("UserInfo",null,
                "userName=? ",new String[]{userName},null,null,null);
        if(cursor.moveToFirst()) {
            String i = cursor.getString(4);
            if (i!=null){
                String ids[]=i.split(",");
                for (int i1 = 0; i1 < ids.length; i1++) {
                    if (ids[i1].equals(smileText.getHashId())){
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
//        Toast.makeText(context, "textArray="+textArray[position], Toast.LENGTH_SHORT).show();

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
                    String collectAuthor="";
                    String collectUrl="";
                    String collectPhoto="";
                    String collectTime="";
                    String nowTime="";
                    String collectType="";
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
                        values.put("collectId",smileText.getHashId());
                        values.put("collectCount",collectCount+1);
                        values.put("collectType","3");
                        values.put("collectTitle",smileText.getContent());
                        values.put("collectAuthor","***");
                        values.put("collectUrl","***");
                        values.put("collectPhoto","***");
                        values.put("collectTime",nowTime);
                    }
                    else {
                        values.put("collectId",smileText.getHashId()+","+oldNewsId);
                        values.put("collectCount",collectCount+1);
                        values.put("collectType","3"+","+collectType);
                        values.put("collectTitle",smileText.getContent()+","+collectTitle);
                        values.put("collectAuthor","***"+","+collectAuthor);
                        values.put("collectUrl","***"+","+collectUrl);
                        values.put("collectPhoto","***"+","+collectPhoto);
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
                    String collectTime = "";
                    String collectType = "";
                    String collectAuthor="";
                    String collectUrl="";
                    String collectPhoto="";
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
                        if (numbers[i].equals(smileText.getHashId())) {
                            number = i;
                        }
                    }
                    String newNewsId = "";
                    String oldIds[] = oldNewsId.split(smileText.getHashId() + ",");
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
                    String collectTitles[] = collectTitle.split(smileText.getContent() + ",");
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
                    String collectPhotos[]=collectPhoto.split(",");
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
                    values.put("collectUrl",newCollectUrl);
                    values.put("collectPhoto",newCollectPhoto);
                    values.put("collectAuthor",newCollectAuthor);
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
                web.setTitle(smileText.getContent());
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
        return smileTextList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvContent,tvTime;
        ImageButton ibFav,ibEmail;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView;
            tvContent=(TextView)itemView.findViewById(R.id.tv_content_item_smile_text);
            tvTime=(TextView)itemView.findViewById(R.id.tv_time_item_smile_text);
            ibFav=(ImageButton)itemView.findViewById(R.id.ib_fav_item_smile_text);
            ibEmail=(ImageButton)itemView.findViewById(R.id.ib_emil_item_smile_text);

            initTextArray();
        }
    }
    public  void initTextArray(){
        textArray = new String[smileTextList.size()];
        for (int i = 0; i < textArray.length; i++) {
            textArray[i]="0";
        }
    }
}
