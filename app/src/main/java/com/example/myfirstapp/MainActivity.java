package com.example.myfirstapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.adapter.MainVPAdapter;
import com.example.myfirstapp.bean.CollectData;
import com.example.myfirstapp.bean.TabBean;
import com.example.myfirstapp.fragment.HistoryFragment;
import com.example.myfirstapp.fragment.NewsFragment;
import com.example.myfirstapp.fragment.SmileFragment;
import com.example.myfirstapp.fragment.TianqiFragment;
import com.example.myfirstapp.utils.MyDBHelper;
import com.example.myfirstapp.widget.BottomLayout;
import com.example.myfirstapp.widget.UnScrollViewPager;
import com.githang.statusbar.StatusBarCompat;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {
    UnScrollViewPager viewPager;
    MainVPAdapter mainVPAdapter;
    List<Fragment> fragmentList=new ArrayList<>();
    TextView tvName;
    Button btnChange,btnFinish;
    Toolbar toolBar;
    private DrawerLayout mDrawerLayout;
    MyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar=(Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        NavigationView navView=(NavigationView)findViewById(R.id.nav_view);
        tvName=(TextView)navView.getHeaderView(0).findViewById(R.id.user_name);
        btnChange=(Button) navView.findViewById(R.id.btn_change_nav);
        btnFinish=(Button) navView.findViewById(R.id.btn_finish_nav);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        tvName.setText(getIntent().getStringExtra("userName"));

        dbHelper = new MyDBHelper(this,"MyUser.db",null,4);
        navView.setCheckedItem(R.id.nav_collect);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_collect:
                        Intent intent=new Intent(MainActivity.this,CollectActivity.class);
                        intent.putExtra("userName",getIntent().getStringExtra("userName"));
                        startActivity(intent);
                        break;
                    case R.id.nav_scan:
                        Intent intent1=new Intent(MainActivity.this, CaptureActivity.class);
                        startActivityForResult(intent1,1);
                        break;
                    case R.id.nav_shake:
                        Intent intent2=new Intent(MainActivity.this, YaoActivity.class);
                        intent2.putExtra("userName",getIntent().getStringExtra("userName"));
                        startActivity(intent2);
                        break;


                }

                return true;
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "btnchange", Toast.LENGTH_SHORT).show();
                Intent intentChange=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intentChange);
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.finishAll();
            }
        });
        getSupportActionBar().setTitle("新闻");
        fragmentList.add(NewsFragment.newInstance(getIntent().getStringExtra("userName")));
        fragmentList.add(new TianqiFragment());
        fragmentList.add(HistoryFragment.newInstance(getIntent().getStringExtra("userName")));
        fragmentList.add(SmileFragment.newInstance(getIntent().getStringExtra("userName")));
        List<String> titles = new ArrayList<>();
        titles.add("1");
        titles.add("2");
        titles.add("3");
        titles.add("4");
        mainVPAdapter=new MainVPAdapter(getSupportFragmentManager(),fragmentList,titles);
        viewPager=(UnScrollViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(mainVPAdapter);
        viewPager.setOffscreenPageLimit(4);


        BottomLayout bottomLayout= (BottomLayout) findViewById(R.id.bottom_layout);
        final List<TabBean> tabs=new ArrayList<>();
        tabs.add(new TabBean("新闻",R.mipmap.bottom_news,R.mipmap.bottom_news_1,1));
        tabs.add(new TabBean("天气",R.mipmap.bottom_travel,R.mipmap.bottom_travel_1,1));
        tabs.add(new TabBean("历史",R.mipmap.bottom_ticket,R.mipmap.bottom_ticket_1,1));
        tabs.add(new TabBean("笑话",R.mipmap.bottom_joke,R.mipmap.bottom_joke_1,1));
        //先设置点击监听 再初始化
        bottomLayout.setBottom(this,tabs,viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        getSupportActionBar().setTitle("新闻");
//                        StatusBarCompat.setStatusBarColor(MainActivity.this,R.color.colorPrimary,false);

                        toolBar.setVisibility(View.VISIBLE);

//                        setTitle("综合");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("天气");

//                        StatusBarCompat.setStatusBarColor(MainActivity.this,R.drawable.shape1,false);
                        toolBar.setVisibility(View.GONE);
//                        setTitle("动弹");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("历史");
//                        StatusBarCompat.setStatusBarColor(MainActivity.this,R.color.colorPrimary,false);
                        toolBar.setVisibility(View.VISIBLE);
//                        setTitle("发现");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("笑话");
//                        StatusBarCompat.setStatusBarColor(MainActivity.this,R.color.colorPrimary,false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (data!=null){
                    Bundle bundle=data.getExtras();
                    if (bundle==null){
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE)==CodeUtils.RESULT_SUCCESS){
                        //成功
                        String resule=bundle.getString(CodeUtils.RESULT_STRING);
                        Toast.makeText(MainActivity.this, "解析结果"+resule, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //失败
                        Toast.makeText(MainActivity.this, "解析失败", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }
}
