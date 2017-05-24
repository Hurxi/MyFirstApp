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
import com.example.myfirstapp.adapter.SmileImageAdapter;
import com.example.myfirstapp.adapter.SmileTextRVAdapter;
import com.example.myfirstapp.bean.SmileImageResponse;
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
 * Created by 若希 on 2017/5/23.
 */

public class SmileImageFragment extends Fragment {
    String userName;
    List<SmileImageResponse.ResultBean.DataBean>  smileImageList;
    SmileImageAdapter adapter;
    int page=1;
    public static SmileImageFragment newInstance(String  userName) {
        SmileImageFragment fragment = new SmileImageFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_smile_image,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b    = getArguments();
        userName = (String) b.get("userName");
        smileImageList=new ArrayList<>();
        final SpringView springView=(SpringView)view.findViewById(R.id.spring_smile_image);
        springView.setHeader(new DefaultHeader(getContext()));//设置头布局
        springView.setFooter(new DefaultFooter(getContext()));//设置尾布局
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                smileImageList.clear();
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
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.rv_smile_image);
//        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        StaggeredGridLayoutManager layoutManager=new
                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new SmileImageAdapter(getContext(),smileImageList,userName);
        recyclerView.setAdapter(adapter);
        getData();
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
//                        Log.d("s=",s);
                        Gson gson=new Gson();
                        SmileImageResponse smileImageResponse=gson.fromJson(s,SmileImageResponse.class);
                        smileImageList.addAll(smileImageResponse.getResult().getData());
                        adapter.initTextArray();
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
