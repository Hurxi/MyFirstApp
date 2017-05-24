package com.example.myfirstapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfirstapp.R;
import com.example.myfirstapp.adapter.NewsRVAdapter;
import com.example.myfirstapp.adapter.SmileTextRVAdapter;
import com.example.myfirstapp.bean.SmileTextResponse;
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
 * Created by 若希 on 2017/5/22.
 */

public class SmileTextFragment extends Fragment {
    String userName;
    private List<SmileTextResponse.ResultBean.DataBean> smileTextList;
    SmileTextRVAdapter adapter;
    int page=1;

    public static SmileTextFragment newInstance(String  userName) {
        SmileTextFragment fragment = new SmileTextFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_smile_text,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b    = getArguments();
        userName = (String) b.get("userName");
        smileTextList=new ArrayList<>();
        final SpringView springView=(SpringView)view.findViewById(R.id.spring_smile_text);
        springView.setHeader(new DefaultHeader(getContext()));//设置头布局
        springView.setFooter(new DefaultFooter(getContext()));//设置尾布局
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                smileTextList.clear();
                page=1;


                getData();
            }

            @Override
            public void onLoadmore() {
                //上拉加载更多
                page=2;
                getData();
            }
        });
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.rv_smile_text);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),1);
//        StaggeredGridLayoutManager layoutManager=new
//                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new SmileTextRVAdapter(getContext(),smileTextList,userName);
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        OkGo.get(JuheApi.SMILE_TEXT)
                .tag(this)
                .params("page",page)
                .params("pagesize",20)
                .params("key", KeyValue.KEY_XIAOHUA)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d("s=",s);
                        Gson gson=new Gson();
                        SmileTextResponse smileTextResponse=
                                gson.fromJson(s,SmileTextResponse.class);
                        smileTextList.addAll(smileTextResponse.getResult().getData()) ;
                        adapter.initTextArray();
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
