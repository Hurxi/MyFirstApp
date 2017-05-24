package com.example.myfirstapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.utils.MyDBHelper;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HisDetailActivity extends BaseActivity {
    TextView tvTitle, tvTime, tvLunar, tvContent;
    ImageView iv;
    private String title;
    private String des;
    private String pic;
    private int year;
    private int month;
    private int day;
    boolean isCol = false;
    private MyDBHelper dbHelper;
    private String userName;
    private String hisId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_his_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("历史详情");
        tvTitle = (TextView) findViewById(R.id.tv_title_his_detail);
        tvTime = (TextView) findViewById(R.id.tv_time_his_detail);
        tvLunar = (TextView) findViewById(R.id.tv_lunar_his_detail);
        tvContent = (TextView) findViewById(R.id.tv_content_his_detail);
        iv = (ImageView) findViewById(R.id.iv_his_detail);
        dbHelper = new MyDBHelper(this, "MyUser.db", null, 4);
        title = getIntent().getStringExtra("title");
        des = getIntent().getStringExtra("des");
        pic = getIntent().getStringExtra("pic");
        year = getIntent().getIntExtra("year", 1);
        month = getIntent().getIntExtra("month", 1);
        day = getIntent().getIntExtra("day", 1);
        userName = getIntent().getStringExtra("userName");
        hisId = getIntent().getStringExtra("id");
//        Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();
        tvTitle.setText(title);
        tvTime.setText(year + "年" + month + "月" + day + "日");
        tvLunar.setText(getIntent().getStringExtra("lunar"));
        tvContent.setText(des);
        if (pic != "" && !pic.isEmpty()) {
            iv.setVisibility(View.VISIBLE);
            Picasso.with(this).load(getIntent().getStringExtra("pic"))
                    .placeholder(R.color.colorPrimary).into(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(HisDetailActivity.this,ImageActivity.class);
                    intent.putExtra("url",getIntent().getStringExtra("pic"));
                    startActivity(intent);
                }
            });
        } else {
            iv.setVisibility(View.GONE);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_news_detail, menu);
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        Cursor cursor=db.query("UserInfo",null,
                "userName=? ",new String[]{userName},null,null,null);
        if(cursor.moveToFirst()) {
            String i = cursor.getString(4);
            if (i!=null){
                String ids[]=i.split(",");
                for (int i1 = 0; i1 < ids.length; i1++) {
                    if (ids[i1].equals(hisId)){
                        isCol=true;
                    }
                }
            }

        }
        if (isCol==true){
            menu.getItem(0).setIcon(R.drawable.ic_favorite_red_500_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.collet_news_detail:
                if (isCol==false){
                    item.setIcon(R.drawable.ic_favorite_red_500_24dp);
                    SQLiteDatabase db=dbHelper.getWritableDatabase();
                    //数据库 有则拿到 无则创建
                    Cursor cursor=db.query("UserInfo",null,
                            "userName=? ",new String[]{userName},null,null,null);
                    String oldNewsId="";
                    int collectCount=0;
                    String collectTitle="";
                    String collectTime="";
                    String collectAuthor="";
                    String collectUrl="";
                    String collectPhoto="";
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
                        values.put("collectId",hisId);
                        values.put("collectCount",collectCount+1);
                        values.put("collectType","2");
                        values.put("collectTitle",title);
                        values.put("collectAuthor","***");
                        values.put("collectUrl","***");
                        values.put("collectPhoto",pic);
                        values.put("collectTime",nowTime);
                    }
                    else {
                        values.put("collectId",hisId+","+oldNewsId);
                        values.put("collectCount",collectCount+1);
                        values.put("collectType","2"+","+collectType);
                        values.put("collectTitle",title+","+collectTitle);
                        values.put("collectAuthor","***"+","+collectAuthor);
                        values.put("collectUrl","***"+","+collectUrl);
                        values.put("collectPhoto",pic+","+collectPhoto);
                        values.put("collectTime",nowTime+","+collectTime);
                    }

                    long id=db.update("UserInfo",values,"userName = ?",new String[]{userName});
                    if(id!=-1) {
                        Toast.makeText(HisDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(HisDetailActivity.this,"收藏失败",Toast.LENGTH_SHORT).show();
                    }
                    isCol=true;

                }
                else {
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
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
                        if (numbers[i].equals(hisId)){
                            number=i;
                        }
                    }
                    String newNewsId="";
                    String oldIds[]=oldNewsId.split(hisId+",");
                    for (int i = 0; i < oldIds.length; i++) {
                        if (number!=i) {
                            if (newNewsId!=""){
                                newNewsId = oldIds[i]+"," + newNewsId;
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
                                newCollectType = collectTypes[i]+"," + newCollectType;
                            }
                            else {
                                newCollectType = collectTypes[i];
                            }
                        }
                    }

                    String newCollectTitle="";
                    String collectTitles[]=collectTitle.split(title+",");
                    for (int i = 0; i < collectTitles.length; i++) {
                        if (number!=i) {
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
                    String collectPhotos[]=collectPhoto.split(pic+",");
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
                    values.put("collectUrl",newCollectUrl);
                    values.put("collectPhoto",newCollectPhoto);
                    values.put("collectAuthor",newCollectAuthor);
                    values.put("collectTime",newCollectTime);
                    long id=db.update("UserInfo",values,"userName = ?",new String[]{userName});
                    if(id!=-1) {
                        Toast.makeText(HisDetailActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(HisDetailActivity.this,"取消收藏失败",Toast.LENGTH_SHORT).show();
                    }
                    isCol=false;
                }
                break;
            case R.id.share_news_detail:
                    UMWeb web = new UMWeb("http://www.baidu.com");
                    web.setTitle(title);
                    web.setDescription("分享自OSChina");
                    new ShareAction(HisDetailActivity.this)
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
                                    Toast.makeText(HisDetailActivity.this, "取消分享", Toast.LENGTH_SHORT).show();
                                }
                            }).open();
                break;
            default:
        }
        return true;
    }
}
