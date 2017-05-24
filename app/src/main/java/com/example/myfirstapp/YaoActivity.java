package com.example.myfirstapp;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.bean.NewsResponse;
import com.example.myfirstapp.bean.SmileImageResponse;
import com.example.myfirstapp.utils.JuheApi;
import com.example.myfirstapp.utils.KeyValue;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;

public class YaoActivity extends BaseActivity {
    Toolbar toolBar;
    TextView title;
    TextView time;
    LinearLayout item;
    ImageButton btnGift,btnInfo;
    ImageView iv;
    TextView tvGift,tvInfo,tvFind;
    SensorManager sensorManager;
    List<NewsResponse.ResultBean.DataBean> newsList;
    List<SmileImageResponse.ResultBean.DataBean>  smileImageList;

    View viewFind;
//    Animation mAnimation,mAnimation2;
    boolean isStart=false;
    int choose=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yao);
        toolBar = (Toolbar) findViewById(R.id.tool_bar_yao);
        btnGift=(ImageButton) findViewById(R.id.ib_gift_find);
        btnInfo=(ImageButton) findViewById(R.id.ib_info_find);
        tvGift=(TextView) findViewById(R.id.tv_gift_find);
        tvInfo=(TextView) findViewById(R.id.tv_info_find);
        tvFind=(TextView) findViewById(R.id.tv_find);
        viewFind=(View)findViewById(R.id.view_find);
        title= (TextView) findViewById(R.id.tv_title_find);
        time= (TextView)findViewById(R.id.tv_time_find);
        item=(LinearLayout)findViewById(R.id.item_find);
        iv=(ImageView)findViewById(R.id.iv_yao);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("摇一摇");

        smileImageList=new ArrayList<>();
        newsList=new ArrayList<>();


        btnGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFind.setText("摇一摇获取笑话");
                btnGift.setImageResource(R.mipmap.btn_shake_gift_actived);
                tvGift.setTextColor(getResources().getColor(R.color.colorPrimary));

                btnInfo.setImageResource(R.mipmap.btn_shake_info);
                tvInfo.setTextColor(Color.GRAY);
                choose=1;
                item.setVisibility(View.GONE);
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFind.setText("摇一摇获取资讯");
                btnInfo.setImageResource(R.mipmap.btn_shake_info_actived);
                tvInfo.setTextColor(getResources().getColor(R.color.colorPrimary));


                btnGift.setImageResource(R.mipmap.btn_shake_gift);
                tvGift.setTextColor(Color.GRAY);
                choose=2;
                item.setVisibility(View.GONE);
            }
        });
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        //通过sensorManager注册相关传感器
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        //参数：监听器 传感器类型 传感器接收的频率(此例子中是加速度传感器）
    }
    private SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //参数值 event包含一些传感器的信息 此例中包含一些加速度的值
            float[] values=event.values;
            float x=values[0];//x轴方向的加速度值
            float y=values[1];//y轴方向的加速度值
            float z=values[2];//z轴方向的加速度值
            int medumValue=15;
            if (Math.abs(x)>medumValue||Math.abs(y)>medumValue||Math.abs(z)>medumValue){
//                Toast.makeText(YaoActivity.this, "摇一摇", Toast.LENGTH_SHORT).show();
                if (isStart==false) {
                    if (choose==1){
                        yaoyiyao();

                    }
                    else {
                        yaoyiyao2();
                    }
                }
            }
//            Log.e("values:","x="+x+",y="+y+",z="+z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void yaoyiyao2() {
        isStart=true;
        getData1();
        item.setVisibility(View.VISIBLE);
        viewFind.setVisibility(View.VISIBLE);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isStart=false;
//            }
//        },5000);
    }



    private void yaoyiyao() {

        isStart=true;
        getData();
        item.setVisibility(View.VISIBLE);
        viewFind.setVisibility(View.VISIBLE);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isStart=false;
//            }
//        },10000);
    }
    private void getData1() {
        OkGo.get(JuheApi.TOUTIAO)
                .tag(this)
                .params("key",KeyValue.KEY)
                .params("type","top")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d("s=",s);
                        Gson gson=new Gson();
                        Random random=new Random();
                        int index1=random.nextInt(19);
                        NewsResponse newsData=gson.fromJson(
                                s,NewsResponse.class);
                        newsList.addAll(newsData.getResult().getData());
                        setData1(newsList.get(index1));


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isStart=false;
                            }
                        },2000);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        isStart=false;
                    }
                });
    }

    private void setData1(final NewsResponse.ResultBean.DataBean index) {
        String pubTime = index.getDate();//转换成小时

//        String time1=pubTime.split(" ")[1];
        SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Date dateNow = new Date();
        try {
            date = a.parse(pubTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        Calendar calendarNow = Calendar.getInstance();
        calendar.setTime(date);
        calendarNow.setTime(dateNow);
        int years = calendarNow.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        int months = calendarNow.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
        int days = calendarNow.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendarNow.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendarNow.get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE);
        int seconds = calendarNow.get(Calendar.SECOND) - calendar.get(Calendar.SECOND);
        if (years > 0) {
            time.setText(years + "年前");
        } else {
            if (months > 0) {
                time.setText(months + "月前");
            } else {
                if (days > 0) {
                    time.setText(days + "日前");
                } else {
                    if (hours > 0) {
                        time.setText(hours + "小时前");
                    } else {
                        if (minutes > 0) {
                            time.setText(minutes + "秒前");
                        } else
                            time.setText("刚刚");
                    }
                }
            }
        }

        title.setText(index.getTitle());
        Picasso.with(this).load(index.getThumbnail_pic_s())
                .placeholder(R.color.colorPrimary)
                .into(iv);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(YaoActivity.this, NewsDetailActivity.class);
                intent.putExtra("url",index.getUrl());
                intent.putExtra("userName",getIntent().getStringExtra("userName"));
                intent.putExtra("newsId",index.getUniquekey());
                intent.putExtra("newsTitle",index.getTitle());
                if (index.getThumbnail_pic_s()==null){
                    intent.putExtra("newsPhoto","");
                }
                else {
                    intent.putExtra("newsPhoto",index.getThumbnail_pic_s());
                }

                intent.putExtra("newsAuthor",index.getAuthor_name());
                intent.putExtra("newsUrl",index.getUrl());
                startActivity(intent);
            }
        });
    }

    private void getData() {
        OkGo.get(JuheApi.SMILE_image)
                .tag(this)
                .params("page",1)
                .params("pagesize",20)
                .params("key", KeyValue.KEY_XIAOHUA)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("s:",s);
                        Gson gson=new Gson();
                        Random random=new Random();
                        int index=random.nextInt(19);
                        SmileImageResponse smileImageResponse=gson.fromJson(s,SmileImageResponse.class);
                        smileImageList.addAll(smileImageResponse.getResult().getData());
                        setData(smileImageList.get(index));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isStart=false;
                            }
                        },2000);

                    }
                             @Override
                             public void onError(Call call, Response response, Exception e) {
                                 super.onError(call, response, e);
                                 isStart=false;

                             }
                         }
                );

    }

    private void setData(final SmileImageResponse.ResultBean.DataBean index){
        String pubTime = index.getUpdatetime();//转换成小时

//        String time1=pubTime.split(" ")[1];
        SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Date dateNow = new Date();
        try {
            date = a.parse(pubTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        Calendar calendarNow = Calendar.getInstance();
        calendar.setTime(date);
        calendarNow.setTime(dateNow);
        int years = calendarNow.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        int months = calendarNow.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
        int days = calendarNow.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendarNow.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendarNow.get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE);
        int seconds = calendarNow.get(Calendar.SECOND) - calendar.get(Calendar.SECOND);
        if (years > 0) {
            time.setText(years + "年前");
        } else {
            if (months > 0) {
                time.setText(months + "月前");
            } else {
                if (days > 0) {
                    time.setText(days + "日前");
                } else {
                    if (hours > 0) {
                       time.setText(hours + "小时前");
                    } else {
                        if (minutes > 0) {
                            time.setText(minutes + "秒前");
                        } else
                           time.setText("刚刚");
                    }
                }
            }
        }

        title.setText(index.getContent());
        Picasso.with(this).load(index.getUrl())
                .placeholder(R.color.colorPrimary)
                .into(iv);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转对应的详情页
//                    Toast.makeText(context, "点击了"+news.getId(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(YaoActivity.this, ImageActivity.class);
                intent.putExtra("url",index.getUrl());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销
        sensorManager.unregisterListener(sensorEventListener);
    }
}
