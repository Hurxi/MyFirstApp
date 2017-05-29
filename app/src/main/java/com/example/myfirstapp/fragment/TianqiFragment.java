package com.example.myfirstapp.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.CityActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.adapter.TianqiDailyRVAdapter;
import com.example.myfirstapp.bean.WeatherDailyResponse;
import com.example.myfirstapp.bean.WeatherLifeResponse;
import com.example.myfirstapp.bean.WeatherNowResponse;
import com.example.myfirstapp.utils.JuheApi;
import com.example.myfirstapp.utils.KeyValue;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 若希 on 2017/5/18.
 */

public class TianqiFragment extends Fragment {

    private List<WeatherNowResponse.ResultsBean> weaherNowList;
    private TextView tvName;

    TextView tvText;
    TextView tvTempera;
    TextView tvYesterday,tvWind,tvRain,tvTemp,tvWindName;
    TextView tvLvyou,tvGanmao,tvXiche,tvChuanyi,tvZiwaixian,tvYundong;
    RecyclerView rvDaily;
    ImageView ivName;
    ImageButton ibCity;
    private String city="江苏淮安";
    private TianqiDailyRVAdapter adapter;
    private List<WeatherDailyResponse.ResultsBean.DailyBean> dailyBeanList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tianqi,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weaherNowList = new ArrayList<>();
        dailyBeanList=new ArrayList<>();
        getDate();

        tvName =(TextView)view.findViewById(R.id.tv_name_tianqi);
        tvWind =(TextView)view.findViewById(R.id.tv_wind_num_tianqi);
        tvRain =(TextView)view.findViewById(R.id.tv_rain_num_tianqi);
        tvTemp =(TextView)view.findViewById(R.id.tv_temp_num_tianqi);
        tvWindName =(TextView)view.findViewById(R.id.tv_wind_name_tianqi);
        tvText=(TextView)view.findViewById(R.id.tv_text_tianqi);
        tvTempera=(TextView)view.findViewById(R.id.tv_temperature_tianqi);
        tvYesterday=(TextView)view.findViewById(R.id.tv_yesterday_tianqi);
        tvLvyou=(TextView)view.findViewById(R.id.tv_lvyou_tianqi);
        tvGanmao=(TextView)view.findViewById(R.id.tv_ganmao_tianqi);
        tvXiche=(TextView)view.findViewById(R.id.tv_xiche_tianqi);
        tvChuanyi=(TextView)view.findViewById(R.id.tv_chuanyi_tianqi);
        tvZiwaixian=(TextView)view.findViewById(R.id.tv_ziwaixian_tianqi);
        tvYundong=(TextView)view.findViewById(R.id.tv_yundong_tianqi);
        ivName=(ImageView)view.findViewById(R.id.iv_name_tianqi);
        rvDaily=(RecyclerView)view.findViewById(R.id.rv_deily_tianqi);
        ibCity=(ImageButton)view.findViewById(R.id.ib_city_tianqi);
        ibCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CityActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDate() {
        OkGo.get(JuheApi.WEATHER_NOW)
                .tag(this)
                .params("key",KeyValue.KEY_TIANQI)
                .params("location",city)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("s=",s);
                        Gson gson=new Gson();
                        WeatherNowResponse a=gson.fromJson(s,WeatherNowResponse.class);

                        weaherNowList.addAll(a.getResults());
//                        a.getResults().get(0).getNow();
//                        a.getResults().get(0).getLocation();
//                        a.getResults().get(0).getLast_update();
//                        Toast.makeText(getContext(), weaherNowList.get(0).getLast_update(), Toast.LENGTH_SHORT).show();
                        tvName.setText(weaherNowList.get(0).getLocation().getName());
                        tvText.setText(weaherNowList.get(0).getNow().getText());
                        tvTempera.setText(weaherNowList.get(0).getNow().getTemperature());
                        if (weaherNowList.get(0).getNow().getText().equals("晴")){
                            ivName.setImageResource(R.mipmap.a);
                        }
                        else if (weaherNowList.get(0).getNow().getText().equals("阴")){
                            ivName.setImageResource(R.mipmap.j);
                        }
                        else {
                            ivName.setImageResource(R.mipmap.f);
                        }

                    }
                });
        OkGo.get(JuheApi.WEATHER_DAILY)
                .tag(this)
                .params("key",KeyValue.KEY_TIANQI)
                .params("location",city)
                .params("start",-1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("s=",s);
                        Gson gson=new Gson();
                        WeatherDailyResponse weatherDailyResponse=gson
                                .fromJson(s,WeatherDailyResponse.class);
                        dailyBeanList = weatherDailyResponse.getResults().get(0).getDaily();
                        int oldTem=Integer.parseInt(weatherDailyResponse.getResults().get(0).getDaily().get(0).getHigh());
                        int nowTem=Integer.parseInt(weatherDailyResponse.getResults().get(0).getDaily().get(1).getHigh());


                        if (oldTem>nowTem){
                            tvYesterday.setText("今天比昨天低"+(oldTem-nowTem)+"℃");
                        }
                        else if (oldTem==nowTem){
                            tvYesterday.setText("今天与昨天一样");
                        }
                        else {
                            tvYesterday.setText("今天比昨天高"+(nowTem-oldTem)+"℃");
                        }
                        String windName=weatherDailyResponse.getResults().get(0).getDaily().get(1).getWind_direction();
                        String windNum=weatherDailyResponse.getResults().get(0).getDaily().get(1).getWind_scale();
                        String rain=weatherDailyResponse.getResults().get(0).getDaily().get(1).getPrecip();
                        tvWindName.setText(windName+"风");
                        tvWind.setText(windNum+"级");
                        tvRain.setText(rain+"0%");
                        tvTemp.setText((oldTem+nowTem)/2+"℃");
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                        rvDaily.setLayoutManager(layoutManager);
                        adapter=new TianqiDailyRVAdapter(getContext(),dailyBeanList);
                        rvDaily.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
        OkGo.get(JuheApi.WEATHER_LIFE)
                .tag(this)
                .params("key",KeyValue.KEY_TIANQI)
                .params("location",city)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d("s=",s);
                        Gson gson=new Gson();
                        WeatherLifeResponse weatherLifeResponse=gson.fromJson(s,WeatherLifeResponse.class);
                        WeatherLifeResponse.ResultsBean.SuggestionBean suggestionBean=weatherLifeResponse.getResults().get(0).getSuggestion();
                        tvLvyou.setText(suggestionBean.getTravel().getBrief());
                        tvGanmao.setText(suggestionBean.getFlu().getBrief());
                        tvXiche.setText(suggestionBean.getCar_washing().getBrief());
                        tvChuanyi.setText(suggestionBean.getDressing().getBrief());
                        tvZiwaixian.setText(suggestionBean.getUv().getBrief());
                        tvYundong.setText(suggestionBean.getSport().getBrief());

                    }
                });

    }
}
