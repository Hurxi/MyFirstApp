package com.example.myfirstapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.adapter.NewsRVAdapter;
import com.example.myfirstapp.bean.NewsResponse;
import com.example.myfirstapp.utils.JuheApi;
import com.example.myfirstapp.utils.KeyValue;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 若希 on 2017/5/17.
 */

public class HostNewsFragment extends Fragment {
    String userName;
    String type;
    public static HostNewsFragment newInstance(String  userName,String type) {
        HostNewsFragment fragment = new HostNewsFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        args.putString("type", type);

        fragment.setArguments(args);
        return fragment;
    }

    List<NewsResponse.ResultBean.DataBean> newsList;
    NewsRVAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_host_news,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b    = getArguments();
        userName = (String) b.get("userName");
        type=(String)b.get("type");
        newsList=new ArrayList<>();
        final SpringView springView=(SpringView)view.findViewById(R.id.spring_host_news);
        springView.setHeader(new DefaultHeader(getContext()));//设置头布局
        springView.setFooter(new DefaultFooter(getContext()));//设置尾布局
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                    newsList.clear();
                    getData();
            }

            @Override
            public void onLoadmore() {
                //上拉加载更多
                getData();
            }
        });
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.rv_host_news);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new NewsRVAdapter(getContext(),newsList,userName);
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        OkGo.get(JuheApi.TOUTIAO)
                .tag(this)
                .params("key",KeyValue.KEY)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d("s=",s);
                        Gson gson=new Gson();
                        NewsResponse newsData=gson.fromJson(
                                s,NewsResponse.class);
                        newsList.addAll(newsData.getResult().getData());
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
