package com.example.myfirstapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfirstapp.R;
import com.example.myfirstapp.adapter.MainVPAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 若希 on 2017/5/17.
 */

public class NewsFragment extends Fragment {
    private String userName;

    public static NewsFragment newInstance(String  userName) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_news,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b    = getArguments();
        userName = (String) b.get("userName");
        TabLayout tabLayout=(TabLayout)view.findViewById(R.id.table_layout_news);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        ViewPager vp=(ViewPager)view.findViewById(R.id.vp_news);
        //设置adapter
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(HostNewsFragment.newInstance(userName,"top"));
        fragmentList.add(HostNewsFragment.newInstance(userName,"shehui"));
        fragmentList.add(HostNewsFragment.newInstance(userName,"guonei"));
        fragmentList.add(HostNewsFragment.newInstance(userName,"guoji"));
        fragmentList.add(HostNewsFragment.newInstance(userName,"yule"));
        fragmentList.add(HostNewsFragment.newInstance(userName,"tiyu"));
        fragmentList.add(HostNewsFragment.newInstance(userName,"junshi"));
        fragmentList.add(HostNewsFragment.newInstance(userName,"keji"));
        fragmentList.add(HostNewsFragment.newInstance(userName,"caijing"));
        fragmentList.add(HostNewsFragment.newInstance(userName,"shishang"));

        List<String> titles = new ArrayList<>();
        titles.add("头条");
        titles.add("社会");
        titles.add("国内");
        titles.add("国际");
        titles.add("娱乐");
        titles.add("体育");
        titles.add("军事");
        titles.add("科技");
        titles.add("财经");
        titles.add("时尚");
        MainVPAdapter adapter=new MainVPAdapter(getFragmentManager(),fragmentList,titles);
        vp.setAdapter(adapter);
        //与view pager关联
        tabLayout.setupWithViewPager(vp);

    }
}
