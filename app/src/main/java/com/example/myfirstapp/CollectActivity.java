package com.example.myfirstapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.myfirstapp.adapter.CollectRVAdapter;
import com.example.myfirstapp.adapter.NewsRVAdapter;
import com.example.myfirstapp.bean.CollectData;
import com.example.myfirstapp.utils.MyDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollectActivity extends BaseActivity {
    MyDBHelper dbHelper;
    List<CollectData> collectList=new ArrayList<>();
    CollectRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        Toolbar toolbar=(Toolbar)findViewById(R.id.tool_bar_collect);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("收藏详情");
        dbHelper = new MyDBHelper(this,"MyUser.db",null,4);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rv_collect);
        collect();
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new CollectRVAdapter(this,collectList,getIntent().getStringExtra("userName"));
        recyclerView.setAdapter(adapter);
//        Toast.makeText(this, "userName="+getIntent().getStringExtra("userName"), Toast.LENGTH_SHORT).show();

    }
    private void collect() {
        String userName=getIntent().getStringExtra("userName");
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
        String collectType="";
        String nowTime="";
        Date date=new Date();
        SimpleDateFormat a=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        nowTime=a.format(date);
        if(cursor.moveToFirst()) {

            oldNewsId = cursor.getString(4);
            collectCount=cursor.getInt(5);
            collectType = cursor.getString(6);
            collectTitle=cursor.getString(7);
            collectAuthor=cursor.getString(8);
            collectUrl=cursor.getString(9);
            collectPhoto=cursor.getString(10);
            collectTime=cursor.getString(11);

        }
//        Toast.makeText(this, "type="+collectType, Toast.LENGTH_SHORT).show();
        String types1[]=collectType.split(",");
        String authors1[]=collectAuthor.split(",");

        String titles1[]=collectTitle.split(",");
        String urls1[]=collectUrl.split(",");
        String photos1[]=collectPhoto.split(",");
        String times1[]=collectTime.split(",");
        String ids1[]=oldNewsId.split(",");
try{
    for (int i = 0; i < types1.length; i++) {

        oldNewsId=ids1[i];
        collectTitle=titles1[i];
        collectUrl=urls1[i];
        collectPhoto=photos1[i];
        collectTime=times1[i];
        collectType=types1[i];
        collectAuthor=authors1[i];



        if (types1[i].equals("1")||types1[i].equals("4")||types1[i].equals("3")){
            if (collectTime==""){
                oldNewsId=ids1[i];
                collectTitle=titles1[i];
                collectUrl=urls1[i];
                collectPhoto=photos1[i];
                collectTime=times1[i];
                collectType=types1[i];
                collectAuthor=authors1[i];
            }
            else{
                oldNewsId=ids1[i]+","+oldNewsId;
                collectTitle=titles1[i]+","+collectTitle;
                collectUrl=urls1[i]+","+collectUrl;
                collectPhoto=photos1[i]+","+collectPhoto;
                collectTime=times1[i]+","+collectTime;
                collectType=types1[i]+","+collectType;
                collectAuthor=authors1[i]+","+collectAuthor;
            }
            if (oldNewsId!=""&&oldNewsId!=null){
                String ids[]=oldNewsId.split(",");
                String types[]=collectType.split(",");
                String titles[]=collectTitle.split(",");
                String urls[]=collectUrl.split(",");
                String photos[]=collectPhoto.split(",");
                String times[]=collectTime.split(",");
                String authors[]=collectAuthor.split(",");
//                    for (int i1 = 0; i1 < ids.length; i1++) {
////                    Toast.makeText(this, ids[i]+titles[i]+ urls[i]+ photos[i]+ times[i], Toast.LENGTH_LONG).show();
//                        collectList.add(new CollectData(ids[i1], titles[i1], types[i1], urls[i1], photos[i1], times[i1]));

//                    }
                collectList.add(new CollectData(ids[0], titles[0], types[0], urls[0], photos[0], times[0],authors[0]));

            }
        }else{
            oldNewsId=ids1[i];
            collectTitle=titles1[i];
            collectUrl=urls1[i];
            collectPhoto=photos1[i];
            collectTime=times1[i];
            collectType=types1[i];
            collectAuthor=authors1[i];

            String ids[]=oldNewsId.split(",");
            String types[]=collectType.split(",");
            String titles[]=collectTitle.split(",");
            String urls[]=collectUrl.split(",");
            String photos[]=collectPhoto.split(",");
            String times[]=collectTime.split(",");
            String authors[]=collectAuthor.split(",");

            collectList.add(new CollectData(ids[0], titles[0], types[0], urls[0], photos[0], times[0],authors[0]));

        }
//            else if (types1[i].equals("2")||types1[i].equals("3")){
//
//                if (collectTime==""){
//                    oldNewsId=ids1[i];
//                    collectTitle=titles1[i];
//                    collectUrl=urls1[i];
//                    collectPhoto=photos1[i];
//                    collectTime=times1[i];
//                    collectType=types1[i];
//                }
//                else{
//                    oldNewsId=ids1[i]+","+oldNewsId;
//                    collectTitle=titles1[i]+","+collectTitle;
//                    collectUrl=urls1[i]+","+collectUrl;
//                    collectPhoto=photos1[i]+","+collectPhoto;
//                    collectTime=times1[i]+","+collectTime;
//                    collectType=types1[i]+","+collectType;
//                }
//                if (oldNewsId!=""&&oldNewsId!=null){
//                    String ids[]=oldNewsId.split(",");
//                    String types[]=collectType.split(",");
//                    String titles[]=collectTitle.split(",");
//                    String urls[]=collectUrl.split(",");
//                    String photos[]=collectPhoto.split(",");
//                    String times[]=collectTime.split(",");
////                    for (int i1 = 0; i1 < ids.length; i1++) {
//////                    Toast.makeText(this, ids[i]+titles[i]+ urls[i]+ photos[i]+ times[i], Toast.LENGTH_LONG).show();
////                        collectList.add(new CollectData(ids[i1], titles[i1], types[i1], urls[i1], photos[i1], times[i1]));
////
////                    }
//                    collectList.add(new CollectData(ids[0], titles[0], types[0], urls[0], photos[0], times[0]));
//
//                }
//            }
    }
}catch (Exception e){
    e.printStackTrace();
}

//        Log.d("s", "collect: ");
    }

}
