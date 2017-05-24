package com.example.myfirstapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 若希 on 2017/4/27.
 */

public class MainVPAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    List<String> titles;
    public MainVPAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
        super(fm);
        this.fragmentList=fragmentList;
      this. titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
