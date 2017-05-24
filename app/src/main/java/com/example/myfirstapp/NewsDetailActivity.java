package com.example.myfirstapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.myfirstapp.utils.MyDBHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsDetailActivity extends BaseActivity {
    WebView wvNewsDetail;
    boolean isCol=false;
    private String userName;
    private String newsId;
    private String newsTitle;
    private String newsPhoto;
    private String newsAuthor;
    private MyDBHelper dbHelper;
    private String newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        dbHelper = new MyDBHelper(this,"MyUser.db",null,4);
        wvNewsDetail = (WebView) findViewById(R.id.wv_news_detail);
        Toolbar toolbar=(Toolbar)findViewById(R.id.tool_bar_news_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("新闻详情");
        WebSettings webSettings = wvNewsDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        wvNewsDetail.setWebViewClient(new WebViewClient());//在本应用跳转
        String url=getIntent().getStringExtra("url");
        userName = getIntent().getStringExtra("userName");
        newsId = getIntent().getStringExtra("newsId");
        newsTitle = getIntent().getStringExtra("newsTitle");
        newsPhoto = getIntent().getStringExtra("newsPhoto");
        newsAuthor = getIntent().getStringExtra("newsAuthor");
        newsUrl = getIntent().getStringExtra("newsUrl");

        wvNewsDetail.loadUrl(url);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_news_detail,menu);

        SQLiteDatabase db= dbHelper.getWritableDatabase();
        Cursor cursor=db.query("UserInfo",null,
                "userName=? ",new String[]{userName},null,null,null);
        if(cursor.moveToFirst()) {
            String i = cursor.getString(4);
            if (i!=null){
                String ids[]=i.split(",");
                for (int i1 = 0; i1 < ids.length; i1++) {
                    if (ids[i1].equals(newsId)){
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
                        values.put("collectId",newsId);
                        values.put("collectCount",collectCount+1);
                        values.put("collectType","1");
                        values.put("collectTitle",newsTitle);
                        values.put("collectAuthor",newsAuthor);
                        values.put("collectUrl",newsUrl);
                        values.put("collectPhoto",newsPhoto);
                        values.put("collectTime",nowTime);
                    }
                    else {
                        values.put("collectId",newsId+","+oldNewsId);
                        values.put("collectCount",collectCount+1);
                        values.put("collectType","1"+","+collectType);
                        values.put("collectTitle",newsTitle+","+collectTitle);
                        values.put("collectAuthor",newsAuthor+","+collectAuthor);
                        values.put("collectUrl",newsUrl+","+collectUrl);
                        values.put("collectPhoto",newsPhoto+","+collectPhoto);
                        values.put("collectTime",nowTime+","+collectTime);
                    }

                    long id=db.update("UserInfo",values,"userName = ?",new String[]{userName});
                    if(id!=-1) {
                        Toast.makeText(NewsDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(NewsDetailActivity.this,"收藏失败",Toast.LENGTH_SHORT).show();
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
                        if (numbers[i].equals(newsId)){
                            number=i;
                        }
                    }
                    String newNewsId="";
                    String oldIds[]=oldNewsId.split(newsId+",");
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
                    String collectTitles[]=collectTitle.split(newsTitle+",");
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
                    String collectAuthors[]=collectAuthor.split(newsAuthor+",");
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
                    String collectUrls[]=collectUrl.split(newsUrl+",");
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
                    String collectPhotos[]=collectPhoto.split(newsPhoto+",");
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
                    values.put("collectAuthor",newCollectAuthor);
                    values.put("collectUrl",newCollectUrl);
                    values.put("collectPhoto",newCollectPhoto);
                    values.put("collectTime",newCollectTime);
                    long id=db.update("UserInfo",values,"userName = ?",new String[]{userName});
                    if(id!=-1) {
                        Toast.makeText(NewsDetailActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(NewsDetailActivity.this,"取消收藏失败",Toast.LENGTH_SHORT).show();
                    }
                    isCol=false;
                }
                break;
            case R.id.share_news_detail:
                UMWeb web = new UMWeb(newsUrl);
                web.setTitle(newsTitle);
                web.setDescription("分享自OSChina");
                new ShareAction(NewsDetailActivity.this)
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
                                        Toast.makeText(NewsDetailActivity.this, "取消分享", Toast.LENGTH_SHORT).show();
                                    }
                                }).open();
                break;
            default:
        }
        return true;
    }
}
