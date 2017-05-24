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
 * Created by 若希 on 2017/5/22.
 */

public class SmileFragment extends Fragment {
    private String userName;

    public static SmileFragment newInstance(String  userName) {
        SmileFragment fragment = new SmileFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_smile,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b    = getArguments();
        userName = (String) b.get("userName");
        TabLayout tabLayout=(TabLayout)view.findViewById(R.id.table_layout_smile);
        ViewPager vp=(ViewPager)view.findViewById(R.id.vp_smile);
        //设置adapter
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(SmileTextFragment.newInstance(userName));
        fragmentList.add(SmileImageFragment.newInstance(userName));
        List<String> titles = new ArrayList<>();
        titles.add("笑话");
        titles.add("趣图");
        MainVPAdapter adapter=new MainVPAdapter(getFragmentManager(),fragmentList,titles);
        vp.setAdapter(adapter);
        //与view pager关联
        tabLayout.setupWithViewPager(vp);
    }
}
