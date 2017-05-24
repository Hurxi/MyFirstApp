package com.example.myfirstapp.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.adapter.HistoryRVAdapter;
import com.example.myfirstapp.bean.HistoryResponse;
import com.example.myfirstapp.utils.DateString;
import com.example.myfirstapp.utils.JuheApi;
import com.example.myfirstapp.utils.KeyValue;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 若希 on 2017/5/19.
 */

public class HistoryFragment extends Fragment {
    TextView tvYearTitle,tvMonthTitle,tvDayTitle;
    TextView tvNameDate1,tvNameDate2,tvNameDate3,tvNameDate4,tvNameDate5,tvNameDate6,tvNameDate7;
    TextView tvNumDate1,tvNumDate2,tvNumDate3,tvNumDate4,tvNumDate5,tvNumDate6,tvNumDate7;
    TextView tvWhat;
    ImageButton ibBefore,ibNext;
    private Date dNow;
    List<HistoryResponse.ResultBean> historyList;
    HistoryRVAdapter adapter;
    RecyclerView rvHis;

    private String userName;

    public static HistoryFragment newInstance(String  userName) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle b    = getArguments();
        userName = (String) b.get("userName");


        tvYearTitle=(TextView)view.findViewById(R.id.tv_title_year_his);
        tvMonthTitle=(TextView)view.findViewById(R.id.tv_title_month_his);
        tvDayTitle=(TextView)view.findViewById(R.id.tv_title_day_his);
        tvNameDate1=(TextView)view.findViewById(R.id.tv_date1_name_his);
        tvNameDate2=(TextView)view.findViewById(R.id.tv_date2_name_his);
        tvNameDate3=(TextView)view.findViewById(R.id.tv_date3_name_his);
        tvNameDate4=(TextView)view.findViewById(R.id.tv_date4_name_his);
        tvNameDate5=(TextView)view.findViewById(R.id.tv_date5_name_his);
        tvNameDate6=(TextView)view.findViewById(R.id.tv_date6_name_his);
        tvNameDate7=(TextView)view.findViewById(R.id.tv_date7_name_his);
        tvNumDate1=(TextView)view.findViewById(R.id.tv_date1_num_his);
        tvNumDate2=(TextView)view.findViewById(R.id.tv_date2_num_his);
        tvNumDate3=(TextView)view.findViewById(R.id.tv_date3_num_his);
        tvNumDate4=(TextView)view.findViewById(R.id.tv_date4_num_his);
        tvNumDate5=(TextView)view.findViewById(R.id.tv_date5_num_his);
        tvNumDate6=(TextView)view.findViewById(R.id.tv_date6_num_his);
        tvNumDate7=(TextView)view.findViewById(R.id.tv_date7_num_his);
        tvWhat=(TextView)view.findViewById(R.id.tv_what_his);
        ibBefore=(ImageButton)view.findViewById(R.id.ib_title_left_his);
        ibNext=(ImageButton)view.findViewById(R.id.ib_title_right_his);
        historyList=new ArrayList<>();
        rvHis=(RecyclerView)view.findViewById(R.id.rv_his);
        String dates[]=DateString.StringDate().split(",");

//        Log.d("s",dates[1]);
        setDate(dates);
        dNow = new Date();
        ibBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前时间

                Calendar calendar=Calendar.getInstance();//得到日历的时间
                calendar.setTime(dNow);//把当前时间赋给日历
                calendar.add(Calendar.DAY_OF_MONTH,-1);//设置-1 得到前一天

//                Toast.makeText(getContext(), BeforeDate(calendar), Toast.LENGTH_SHORT).show();

                String dates1[]=BeforeDate(calendar).split(",");
                setDate(dates1);
                historyList.clear();
                getData(dates1);
                dNow =calendar.getTime();
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();//得到日历的时间
                calendar.setTime(dNow);//把当前时间赋给日历
                calendar.add(Calendar.DAY_OF_MONTH,1);//设置-1 得到前一天
                String dates1[]=BeforeDate(calendar).split(",");
                setDate(dates1);
                historyList.clear();
                getData(dates1);
                dNow =calendar.getTime();
            }
        });
        adapter=new HistoryRVAdapter(historyList,getContext(),userName);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        rvHis.setLayoutManager(layoutManager);
        rvHis.setAdapter(adapter);
        getData(dates);
    }

    private void getData(String[] dates) {
        int mMonth,mDay;
        mMonth=Integer.parseInt(dates[1]);
        mDay=Integer.parseInt(dates[2]);
        OkGo.get(JuheApi.HISTORY)
                .tag(this)
                .params("key", KeyValue.KEY_LISHI)
                .params("v","1.0")
                .params("month",mMonth)
                .params("day",mDay)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d("s=",s);
                        Gson gson=new Gson();
                        HistoryResponse historyResponse=gson.fromJson(s,HistoryResponse.class);
                        historyList.addAll(historyResponse.getResult());
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    private String BeforeDate(Calendar calendar){
        String mYear,mMonth,mDay,mWeek;
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear=String.valueOf(calendar.get(Calendar.YEAR));

        mMonth=String.valueOf(calendar.get(Calendar.MONTH)+1);
        mDay=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        mWeek=String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWeek)){
            mWeek="日";
        }
        else if ("2".equals(mWeek)){
            mWeek="一";
        }
        else if ("3".equals(mWeek)){
            mWeek="二";
        }
        else if ("4".equals(mWeek)){
            mWeek="三";
        }
        else if ("5".equals(mWeek)){
            mWeek="四";
        }
        else if ("6".equals(mWeek)){
            mWeek="五";
        }
        else if ("7".equals(mWeek)){
            mWeek="六";
        }

//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String before=sdf.format(dBefore);
//        mYear=before.split("年")[0];
//        mMonth
        return mYear+","+mMonth+","+mDay+","+mWeek;
    }

    private void setDate(String dates[]) {
        tvYearTitle.setText(dates[0]);
        tvMonthTitle.setText(dates[1]);
        tvDayTitle.setText(dates[2]);
        tvWhat.setText("历史上的"+dates[1]+"月"+dates[2]+"日发生了什么");
//        Toast.makeText(getContext(), dates[3], Toast.LENGTH_SHORT).show();
        int day=getMonthDay(dates[0],dates[1]);
        int dayNow=Integer.parseInt(dates[2]);



        if (tvNameDate1.getText().equals(dates[3])){

            tvNumDate1.setText(dates[2]);
//            android:background="@drawable/shape2"
            tvNumDate1.setBackgroundResource(R.drawable.shape2);
            tvNumDate2.setBackground(tvNameDate2.getBackground());
            tvNumDate3.setBackground(tvNameDate2.getBackground());
            tvNumDate4.setBackground(tvNameDate2.getBackground());
            tvNumDate5.setBackground(tvNameDate2.getBackground());
            tvNumDate6.setBackground(tvNameDate2.getBackground());
            tvNumDate7.setBackground(tvNameDate2.getBackground());
            tvNumDate1.setTextColor(Color.WHITE);
            tvNumDate7.setTextColor(Color.BLACK);
            tvNumDate2.setTextColor(Color.BLACK);
            tvNumDate3.setTextColor(Color.BLACK);
            tvNumDate4.setTextColor(Color.BLACK);
            tvNumDate5.setTextColor(Color.BLACK);
            tvNumDate6.setTextColor(Color.BLACK);
            if (dayNow+6<=day){
                tvNumDate2.setText(dayNow+1+"");
                tvNumDate3.setText(dayNow+2+"");
                tvNumDate4.setText(dayNow+3+"");
                tvNumDate5.setText(dayNow+4+"");
                tvNumDate6.setText(dayNow+5+"");
                tvNumDate7.setText(dayNow+6+"");


            }
            else if (dayNow+5==day){
                tvNumDate2.setText(dayNow+1+"");
                tvNumDate3.setText(dayNow+2+"");
                tvNumDate4.setText(dayNow+3+"");
                tvNumDate5.setText(dayNow+4+"");
                tvNumDate6.setText(dayNow+5+"");
                tvNumDate7.setText(1+"");
            }
            else if (dayNow+4==day){
                tvNumDate2.setText(dayNow+1+"");
                tvNumDate3.setText(dayNow+2+"");
                tvNumDate4.setText(dayNow+3+"");
                tvNumDate5.setText(dayNow+4+"");
                tvNumDate6.setText(1+"");
                tvNumDate7.setText(2+"");
            }
            else if (dayNow+3==day){
                tvNumDate2.setText(dayNow+1+"");
                tvNumDate3.setText(dayNow+2+"");
                tvNumDate4.setText(dayNow+3+"");
                tvNumDate5.setText(1+"");
                tvNumDate6.setText(2+"");
                tvNumDate7.setText(3+"");
            }
            else if (dayNow+2==day){
                tvNumDate2.setText(dayNow+1+"");
                tvNumDate3.setText(dayNow+2+"");
                tvNumDate4.setText(1+"");
                tvNumDate5.setText(2+"");
                tvNumDate6.setText(3+"");
                tvNumDate7.setText(4+"");
            }
            else if (dayNow+1==day){
                tvNumDate2.setText(dayNow+1+"");
                tvNumDate3.setText(1+"");
                tvNumDate4.setText(2+"");
                tvNumDate5.setText(3+"");
                tvNumDate6.setText(4+"");
                tvNumDate7.setText(5+"");
            }
            else if (dayNow==day){
                tvNumDate2.setText(1+"");
                tvNumDate3.setText(2+"");
                tvNumDate4.setText(3+"");
                tvNumDate5.setText(4+"");
                tvNumDate6.setText(5+"");
                tvNumDate7.setText(6+"");
            }
        }
        else if (tvNameDate2.getText().equals(dates[3])){
            tvNumDate2.setText(dates[2]);
            tvNumDate2.setBackgroundResource(R.drawable.shape2);
            tvNumDate1.setBackground(tvNameDate1.getBackground());
            tvNumDate3.setBackground(tvNameDate1.getBackground());
            tvNumDate4.setBackground(tvNameDate1.getBackground());
            tvNumDate5.setBackground(tvNameDate1.getBackground());
            tvNumDate6.setBackground(tvNameDate1.getBackground());
            tvNumDate7.setBackground(tvNameDate1.getBackground());
            tvNumDate2.setTextColor(Color.WHITE);
            tvNumDate1.setTextColor(Color.BLACK);
            tvNumDate7.setTextColor(Color.BLACK);
            tvNumDate3.setTextColor(Color.BLACK);
            tvNumDate4.setTextColor(Color.BLACK);
            tvNumDate5.setTextColor(Color.BLACK);
            tvNumDate6.setTextColor(Color.BLACK);
            if (dayNow-1==0){
                int a=Integer.parseInt(dates[1])-1;
                int day1=0;
                if (a>0){
                    day1=getMonthDay(dates[0],a-1+"");
                }
                else {
                    day1=getMonthDay(dates[0],"12");
                }
                tvNumDate1.setText(day1+"");
                tvNumDate3.setText(dayNow+1+"");
                tvNumDate4.setText(dayNow+2+"");
                tvNumDate5.setText(dayNow+3+"");
                tvNumDate6.setText(dayNow+4+"");
                tvNumDate7.setText(dayNow+5+"");


            }
            else if (dayNow+5<=day){
                tvNumDate1.setText(dayNow-1+"");
                tvNumDate3.setText(dayNow+1+"");
                tvNumDate4.setText(dayNow+2+"");
                tvNumDate5.setText(dayNow+3+"");
                tvNumDate6.setText(dayNow+4+"");
                tvNumDate7.setText(dayNow+5+"");
            }
            else if (dayNow+4==day){
                tvNumDate1.setText(dayNow-1+"");
                tvNumDate3.setText(dayNow+1+"");
                tvNumDate4.setText(dayNow+2+"");
                tvNumDate5.setText(dayNow+3+"");
                tvNumDate6.setText(dayNow+4+"");
                tvNumDate7.setText(1+"");
            }
            else if (dayNow+3==day){
                tvNumDate1.setText(dayNow-1+"");
                tvNumDate3.setText(dayNow+1+"");
                tvNumDate4.setText(dayNow+2+"");
                tvNumDate5.setText(dayNow+3+"");
                tvNumDate6.setText(1+"");
                tvNumDate7.setText(2+"");
            }
            else if (dayNow+2==day){
                tvNumDate1.setText(dayNow-1+"");
                tvNumDate3.setText(dayNow+1+"");
                tvNumDate4.setText(dayNow+2+"");
                tvNumDate5.setText(1+"");
                tvNumDate6.setText(2+"");
                tvNumDate7.setText(3+"");
            }
            else if (dayNow+1==day){
                tvNumDate1.setText(dayNow-1+"");
                tvNumDate3.setText(dayNow+1+"");
                tvNumDate4.setText(1+"");
                tvNumDate5.setText(2+"");
                tvNumDate6.setText(3+"");
                tvNumDate7.setText(4+"");
            }
            else if (dayNow==day){
                tvNumDate1.setText(dayNow-1+"");
                tvNumDate3.setText(1+"");
                tvNumDate4.setText(2+"");
                tvNumDate5.setText(3+"");
                tvNumDate6.setText(4+"");
                tvNumDate7.setText(5+"");
            }

        }
        else if (tvNameDate3.getText().equals(dates[3])){
            tvNumDate3.setText(dates[2]);
            tvNumDate3.setBackgroundResource(R.drawable.shape2);
            tvNumDate2.setBackground(tvNameDate2.getBackground());
            tvNumDate1.setBackground(tvNameDate2.getBackground());
            tvNumDate4.setBackground(tvNameDate2.getBackground());
            tvNumDate5.setBackground(tvNameDate2.getBackground());
            tvNumDate6.setBackground(tvNameDate2.getBackground());
            tvNumDate7.setBackground(tvNameDate2.getBackground());
            tvNumDate3.setTextColor(Color.WHITE);
            tvNumDate1.setTextColor(Color.BLACK);
            tvNumDate2.setTextColor(Color.BLACK);
            tvNumDate7.setTextColor(Color.BLACK);
            tvNumDate4.setTextColor(Color.BLACK);
            tvNumDate5.setTextColor(Color.BLACK);
            tvNumDate6.setTextColor(Color.BLACK);
            if (dayNow-1==1){
                int a=Integer.parseInt(dates[1])-1;
                int day1=0;
                if (a>0){
                    day1=getMonthDay(dates[0],a-1+"");
                }
                else {
                    day1=getMonthDay(dates[0],"12");
                }
                tvNumDate1.setText(day1-1+"");
                tvNumDate2.setText(day1+"");
                tvNumDate4.setText(dayNow+1+"");
                tvNumDate5.setText(dayNow+2+"");
                tvNumDate6.setText(dayNow+3+"");
                tvNumDate7.setText(dayNow+4+"");
            }
            else if (dayNow-1==0){
                int a=Integer.parseInt(dates[1])-1;
                int day1=0;
                if (a>0){
                    day1=getMonthDay(dates[0],a-1+"");
                }
                else {
                    day1=getMonthDay(dates[0],"12");
                }
                tvNumDate1.setText(day1+"");
                tvNumDate2.setText(dayNow-1+"");
                tvNumDate4.setText(dayNow+1+"");
                tvNumDate5.setText(dayNow+2+"");
                tvNumDate6.setText(dayNow+3+"");
                tvNumDate7.setText(dayNow+4+"");
            }
            else if (dayNow+4<=day){
                tvNumDate1.setText(dayNow-2+"");
                tvNumDate2.setText(dayNow-1+"");
                tvNumDate4.setText(dayNow+1+"");
                tvNumDate5.setText(dayNow+2+"");
                tvNumDate6.setText(dayNow+3+"");
                tvNumDate7.setText(dayNow+4+"");
            }
            else if (dayNow+3==day){
                tvNumDate1.setText(dayNow-2+"");
                tvNumDate2.setText(dayNow-1+"");
                tvNumDate4.setText(dayNow+1+"");
                tvNumDate5.setText(dayNow+2+"");
                tvNumDate6.setText(dayNow+3+"");
                tvNumDate7.setText(1+"");
            }
            else if (dayNow+2==day){
                tvNumDate1.setText(dayNow-2+"");
                tvNumDate2.setText(dayNow-1+"");
                tvNumDate4.setText(dayNow+1+"");
                tvNumDate5.setText(dayNow+2+"");
                tvNumDate6.setText(1+"");
                tvNumDate7.setText(2+"");
            }
            else if (dayNow+1==day){
                tvNumDate1.setText(dayNow-2+"");
                tvNumDate2.setText(dayNow-1+"");
                tvNumDate4.setText(dayNow+1+"");
                tvNumDate5.setText(1+"");
                tvNumDate6.setText(2+"");
                tvNumDate7.setText(3+"");
            }
            else if (dayNow==day){
                tvNumDate1.setText(dayNow-2+"");
                tvNumDate2.setText(dayNow-1+"");
                tvNumDate4.setText(1+"");
                tvNumDate5.setText(2+"");
                tvNumDate6.setText(3+"");
                tvNumDate7.setText(4+"");
            }
        }
        else if (tvNameDate4.getText().equals(dates[3])){
            tvNumDate4.setText(dates[2]);
            tvNumDate4.setBackgroundResource(R.drawable.shape2);
            tvNumDate2.setBackground(tvNameDate2.getBackground());
            tvNumDate3.setBackground(tvNameDate2.getBackground());
            tvNumDate1.setBackground(tvNameDate2.getBackground());
            tvNumDate5.setBackground(tvNameDate2.getBackground());
            tvNumDate6.setBackground(tvNameDate2.getBackground());
            tvNumDate7.setBackground(tvNameDate2.getBackground());
            tvNumDate4.setTextColor(Color.WHITE);
            tvNumDate1.setTextColor(Color.BLACK);
            tvNumDate2.setTextColor(Color.BLACK);
            tvNumDate3.setTextColor(Color.BLACK);
            tvNumDate7.setTextColor(Color.BLACK);
            tvNumDate5.setTextColor(Color.BLACK);
            tvNumDate6.setTextColor(Color.BLACK);
            int a=Integer.parseInt(dates[1])-1;
            int day1=0;
            if (a>0){
                day1=getMonthDay(dates[0],a-1+"");
            }
            else {
                day1=getMonthDay(dates[0],"12");
            }
            if (dayNow-1==2){
                tvNumDate1.setText(day1+"");
                tvNumDate2.setText(dayNow-2+"");
                tvNumDate3.setText(dayNow-1+"");
                tvNumDate5.setText(dayNow+1+"");
                tvNumDate6.setText(dayNow+2+"");
                tvNumDate7.setText(dayNow+3+"");
            }
            else if (dayNow-1==1){
                tvNumDate1.setText(day1-1+"");
                tvNumDate2.setText(day1+"");
                tvNumDate3.setText(dayNow-1+"");
                tvNumDate5.setText(dayNow+1+"");
                tvNumDate6.setText(dayNow+2+"");
                tvNumDate7.setText(dayNow+3+"");
            }
            else if (dayNow-1==0){
                tvNumDate1.setText(day1+2+"");
                tvNumDate2.setText(day1+1+"");
                tvNumDate3.setText(day1+"");
                tvNumDate5.setText(dayNow+1+"");
                tvNumDate6.setText(dayNow+2+"");
                tvNumDate7.setText(dayNow+3+"");
            }
            else if (dayNow+3<=day){
                tvNumDate1.setText(dayNow-3+"");
                tvNumDate2.setText(dayNow-2+"");
                tvNumDate3.setText(dayNow-1+"");
                tvNumDate5.setText(dayNow+1+"");
                tvNumDate6.setText(dayNow+2+"");
                tvNumDate7.setText(dayNow+3+"");
            }
            else if (dayNow+2==day){
                tvNumDate1.setText(dayNow-3+"");
                tvNumDate2.setText(dayNow-2+"");
                tvNumDate3.setText(dayNow-1+"");
                tvNumDate5.setText(dayNow+1+"");
                tvNumDate6.setText(dayNow+2+"");
                tvNumDate7.setText(1+"");
            }
            else if (dayNow+1==day){
                tvNumDate1.setText(dayNow-3+"");
                tvNumDate2.setText(dayNow-2+"");
                tvNumDate3.setText(dayNow-1+"");
                tvNumDate5.setText(dayNow+1+"");
                tvNumDate6.setText(1+"");
                tvNumDate7.setText(2+"");
            }
            else if (dayNow==day){
                tvNumDate1.setText(dayNow-3+"");
                tvNumDate2.setText(dayNow-2+"");
                tvNumDate3.setText(dayNow-1+"");
                tvNumDate5.setText(1+"");
                tvNumDate6.setText(2+"");
                tvNumDate7.setText(3+"");
            }
        }
        else if (tvNameDate5.getText().equals(dates[3])){
            tvNumDate5.setText(dates[2]);
            tvNumDate5.setBackgroundResource(R.drawable.shape2);
            tvNumDate2.setBackground(tvNameDate2.getBackground());
            tvNumDate3.setBackground(tvNameDate2.getBackground());
            tvNumDate4.setBackground(tvNameDate2.getBackground());
            tvNumDate1.setBackground(tvNameDate2.getBackground());
            tvNumDate6.setBackground(tvNameDate2.getBackground());
            tvNumDate7.setBackground(tvNameDate2.getBackground());
            tvNumDate5.setTextColor(Color.WHITE);
            tvNumDate1.setTextColor(Color.BLACK);
            tvNumDate2.setTextColor(Color.BLACK);
            tvNumDate3.setTextColor(Color.BLACK);
            tvNumDate4.setTextColor(Color.BLACK);
            tvNumDate7.setTextColor(Color.BLACK);
            tvNumDate6.setTextColor(Color.BLACK);
            int a=Integer.parseInt(dates[1])-1;
            int day1=0;
            if (a>0){
                day1=getMonthDay(dates[0],a-1+"");
            }
            else {
                day1=getMonthDay(dates[0],"12");
            }
            if (dayNow-1==3){
                tvNumDate1.setText(day1+"");
                tvNumDate2.setText(dayNow-3+"");
                tvNumDate3.setText(dayNow-2+"");
                tvNumDate4.setText(dayNow-1+"");
                tvNumDate6.setText(dayNow+1+"");
                tvNumDate7.setText(dayNow+2+"");
            }
            else if (dayNow-1==2){
                tvNumDate1.setText(day1-1+"");
                tvNumDate2.setText(day1+"");
                tvNumDate3.setText(dayNow-2+"");
                tvNumDate4.setText(dayNow-1+"");
                tvNumDate6.setText(dayNow+2+"");
                tvNumDate7.setText(dayNow+3+"");
            }
            else if (dayNow-1==1){
                tvNumDate1.setText(day1-2+"");
                tvNumDate2.setText(day1-1+"");
                tvNumDate3.setText(day1+"");
                tvNumDate4.setText(dayNow-1+"");
                tvNumDate6.setText(dayNow+2+"");
                tvNumDate7.setText(dayNow+3+"");
            }
            else if (dayNow-1==0){
                tvNumDate1.setText(day1-3+"");
                tvNumDate2.setText(day1-2+"");
                tvNumDate3.setText(day1-1+"");
                tvNumDate4.setText(day1+"");
                tvNumDate6.setText(dayNow+1+"");
                tvNumDate7.setText(dayNow+2+"");
            }
            else if (dayNow+2<=day){
                tvNumDate1.setText(dayNow-4+"");
                tvNumDate2.setText(dayNow-3+"");
                tvNumDate3.setText(dayNow-2+"");
                tvNumDate4.setText(dayNow-1+"");
                tvNumDate6.setText(dayNow+1+"");
                tvNumDate7.setText(dayNow+2+"");
            }
            else if (dayNow+1==day){
                tvNumDate1.setText(dayNow-4+"");
                tvNumDate2.setText(dayNow-3+"");
                tvNumDate3.setText(dayNow-2+"");
                tvNumDate4.setText(dayNow-1+"");
                tvNumDate6.setText(dayNow+1+"");
                tvNumDate7.setText(1+"");
            }
            else if (dayNow==day){
                tvNumDate1.setText(dayNow-4+"");
                tvNumDate2.setText(dayNow-3+"");
                tvNumDate3.setText(dayNow-2+"");
                tvNumDate5.setText(dayNow-1+"");
                tvNumDate6.setText(1+"");
                tvNumDate7.setText(2+"");
            }
        }
        else if (tvNameDate6.getText().equals(dates[3])){
            tvNumDate6.setText(dates[2]);
            tvNumDate6.setBackgroundResource(R.drawable.shape2);
            tvNumDate2.setBackground(tvNameDate2.getBackground());
            tvNumDate3.setBackground(tvNameDate2.getBackground());
            tvNumDate4.setBackground(tvNameDate2.getBackground());
            tvNumDate5.setBackground(tvNameDate2.getBackground());
            tvNumDate1.setBackground(tvNameDate2.getBackground());
//            tvNumDate7.setBackgroundResource(R.color.colorWhite);
            tvNumDate7.setBackground(tvNameDate2.getBackground());
            tvNumDate6.setTextColor(Color.WHITE);
            tvNumDate1.setTextColor(Color.BLACK);
            tvNumDate2.setTextColor(Color.BLACK);
            tvNumDate3.setTextColor(Color.BLACK);
            tvNumDate4.setTextColor(Color.BLACK);
            tvNumDate5.setTextColor(Color.BLACK);
            tvNumDate7.setTextColor(Color.BLACK);
            int a=Integer.parseInt(dates[1])-1;
            int day1=0;
            if (a>0){
                day1=getMonthDay(dates[0],a-1+"");
            }
            else {
                day1=getMonthDay(dates[0],"12");
            }
            if (dayNow-1==4){
                tvNumDate1.setText(day1+"");
                tvNumDate2.setText(dayNow-4+"");
                tvNumDate3.setText(dayNow-3+"");
                tvNumDate4.setText(dayNow-2+"");
                tvNumDate5.setText(dayNow-1+"");
                tvNumDate7.setText(dayNow+1+"");
            }
            else if (dayNow-1==3){
                tvNumDate1.setText(day1-1+"");
                tvNumDate2.setText(day1+"");
                tvNumDate3.setText(dayNow-3+"");
                tvNumDate4.setText(dayNow-2+"");
                tvNumDate5.setText(dayNow-1+"");
                tvNumDate7.setText(dayNow+1+"");
            }
            else if (dayNow-1==2){
                tvNumDate1.setText(day1-2+"");
                tvNumDate2.setText(day1-1+"");
                tvNumDate3.setText(day1+"");
                tvNumDate4.setText(dayNow-2+"");
                tvNumDate5.setText(dayNow-1+"");
                tvNumDate7.setText(dayNow+1+"");
            }
            else if (dayNow-1==1){
                tvNumDate1.setText(day1-3+"");
                tvNumDate2.setText(day1-2+"");
                tvNumDate3.setText(day1-1+"");
                tvNumDate4.setText(day1+"");
                tvNumDate5.setText(dayNow-1+"");
                tvNumDate7.setText(dayNow+1+"");
            }
            else if (dayNow-1==0){
                tvNumDate1.setText(day1-4+"");
                tvNumDate2.setText(day1-3+"");
                tvNumDate3.setText(day1-2+"");
                tvNumDate4.setText(day1-1+"");
                tvNumDate5.setText(day1+"");
                tvNumDate7.setText(dayNow+1+"");
            }
            else if (dayNow+1<=day){
                tvNumDate1.setText(dayNow-5+"");
                tvNumDate2.setText(dayNow-4+"");
                tvNumDate3.setText(dayNow-3+"");
                tvNumDate4.setText(dayNow-2+"");
                tvNumDate5.setText(dayNow-1+"");
                tvNumDate7.setText(dayNow+1+"");
            }
            else if (dayNow==day){
                tvNumDate1.setText(dayNow-5+"");
                tvNumDate2.setText(dayNow-4+"");
                tvNumDate3.setText(dayNow-3+"");
                tvNumDate4.setText(dayNow-2+"");
                tvNumDate5.setText(dayNow-1+"");
                tvNumDate7.setText(1+"");
            }
        }
        else if (tvNameDate7.getText().equals(dates[3])){
            tvNumDate7.setText(dates[2]);
            tvNumDate7.setBackgroundResource(R.drawable.shape2);
            tvNumDate2.setBackground(tvNameDate2.getBackground());
            tvNumDate3.setBackground(tvNameDate2.getBackground());
            tvNumDate4.setBackground(tvNameDate2.getBackground());
            tvNumDate5.setBackground(tvNameDate2.getBackground());
            tvNumDate6.setBackground(tvNameDate2.getBackground());
            tvNumDate1.setBackground(tvNameDate2.getBackground());
            tvNumDate7.setTextColor(Color.WHITE);
            tvNumDate1.setTextColor(Color.BLACK);
            tvNumDate2.setTextColor(Color.BLACK);
            tvNumDate3.setTextColor(Color.BLACK);
            tvNumDate4.setTextColor(Color.BLACK);
            tvNumDate5.setTextColor(Color.BLACK);
            tvNumDate6.setTextColor(Color.BLACK);
            int a=Integer.parseInt(dates[1])-1;
            int day1=0;
            if (a>0){
                day1=getMonthDay(dates[0],a-1+"");
            }
            else {
                day1=getMonthDay(dates[0],"12");
            }
            if (dayNow-1==5){
                tvNumDate1.setText(day1+"");
                tvNumDate2.setText(dayNow-5+"");
                tvNumDate3.setText(dayNow-4+"");
                tvNumDate4.setText(dayNow-3+"");
                tvNumDate5.setText(dayNow-2+"");
                tvNumDate6.setText(dayNow-1+"");
            }
            if (dayNow-1==4){
                tvNumDate1.setText(day1-1+"");
                tvNumDate2.setText(day1+"");
                tvNumDate3.setText(dayNow-4+"");
                tvNumDate4.setText(dayNow-3+"");
                tvNumDate5.setText(dayNow-2+"");
                tvNumDate6.setText(dayNow-1+"");
            }
            else if (dayNow-1==3){
                tvNumDate1.setText(day1-2+"");
                tvNumDate2.setText(day1-1+"");
                tvNumDate3.setText(day1+"");
                tvNumDate4.setText(dayNow-3+"");
                tvNumDate5.setText(dayNow-2+"");
                tvNumDate6.setText(dayNow-1+"");
            }
            else if (dayNow-1==2){
                tvNumDate1.setText(day1-3+"");
                tvNumDate2.setText(day1-2+"");
                tvNumDate3.setText(day1-1+"");
                tvNumDate4.setText(day1+"");
                tvNumDate5.setText(dayNow-2+"");
                tvNumDate6.setText(dayNow-1+"");
            }
            else if (dayNow-1==1){
                tvNumDate1.setText(day1-4+"");
                tvNumDate2.setText(day1-3+"");
                tvNumDate3.setText(day1-2+"");
                tvNumDate4.setText(day1-1+"");
                tvNumDate5.setText(day1+"");
                tvNumDate6.setText(dayNow-1+"");
            }
            else if (dayNow-1==0){
                tvNumDate1.setText(day1-5+"");
                tvNumDate2.setText(day1-4+"");
                tvNumDate3.setText(day1-3+"");
                tvNumDate4.setText(day1-2+"");
                tvNumDate5.setText(day1-1+"");
                tvNumDate6.setText(day1+"");
            }
            else if (dayNow<=day){
                tvNumDate1.setText(dayNow-6+"");
                tvNumDate2.setText(dayNow-5+"");
                tvNumDate3.setText(dayNow-4+"");
                tvNumDate4.setText(dayNow-3+"");
                tvNumDate5.setText(dayNow-2+"");
                tvNumDate6.setText(dayNow-1+"");
            }
        }


    }

    public int getMonthDay(String a,String b){
        int year=Integer.parseInt(a);
        int month=Integer.parseInt(b);
        int day=0;
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day=31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                day=30;
                break;
            case 2:
                if ((year%4==0&&year%100!=0)||year%400==0){
                    day=29;
                }
                else {
                    day=28;
                }
                break;
        }
        return day;
    }

}
