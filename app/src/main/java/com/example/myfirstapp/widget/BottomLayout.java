package com.example.myfirstapp.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myfirstapp.bean.TabBean;

import java.util.List;

/**
 * Created by 若希 on 2017/5/17.
 */

public class BottomLayout extends LinearLayout {
    List<TabBean> tabList;
    Context context;
    ViewPager viewPager;
    int midIndex;
    public BottomLayout(Context context) {
        super(context);
    }

    public BottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setBottom(Context context, List<TabBean> tabList, ViewPager viewPager){
        this.tabList=tabList;
        this.context=context;
        this.viewPager=viewPager;
        init();

    }

    private void init() {
        //初始化 bottomLayout底部导航
        int mIndex=-1;
        for (int i = 0; i < tabList.size(); i++) {
            final TabBean tab=tabList.get(i);

            if (tab.getType()==1){
                mIndex+=1;
            }
            else {
                midIndex=i;
            }
            tab.setIndex(mIndex);
            final BottomTab bottomTab=new BottomTab(context,tab);
            LayoutParams layoutParams=new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight=1;
            layoutParams.gravity= Gravity.CENTER;
            addView(bottomTab,layoutParams);
            if (tab.getType()==1){
                bottomTab.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(tab.getIndex(),false);
                    }
                });
            }
            else {
                bottomTab.setOnClickListener(mOnClickListener);

            }
            if (i==0){
                bottomTab.setSelected(true);//初始选中第0项
            }


        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //先全部设为 未选中 即false
                //再将选中的页设为true
                for (int i = 0; i < getChildCount(); i++) {//子布局数量
                    getChildAt(i).setSelected(false);
                }
                    getChildAt(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    OnClickListener mOnClickListener;
    public void setMidClickListener(OnClickListener onClickListener){
        mOnClickListener=onClickListener;

    }
}
