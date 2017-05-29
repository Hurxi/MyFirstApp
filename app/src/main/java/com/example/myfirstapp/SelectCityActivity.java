package com.example.myfirstapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.myfirstapp.bean.ProvinceBean;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity {
    final List<ProvinceBean> options1Items=new ArrayList<ProvinceBean>();
    final List<List<String>> options2Items=new ArrayList<List<String>>();
    final List<List<List<ProvinceBean>>> options3Items=new ArrayList<List<List<ProvinceBean>>>();
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        ll=(LinearLayout)findViewById(R.id.ll);
        initOptionData();
        OptionsPickerView pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
//                List<String> options1Items

                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2);
                Toast.makeText(SelectCityActivity.this, ""+tx, Toast.LENGTH_SHORT).show();
            }
        })
                .setDecorView(ll)
                .setTitleText("城市选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setSelectOptions(0, 1)//默认选中项
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("省", "市", "区")
                .setCancelColor(Color.BLACK)
                .setSubmitColor(Color.BLACK)
                .build();
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
    }
    private void initOptionData() {



        /**

         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，

         * PickerView会通过getPickerViewText方法获取字符串显示出来。

         */



        //选项1

        options1Items.add(new ProvinceBean(0, "广东", "描述部分", "其他数据"));

        options1Items.add(new ProvinceBean(1, "湖南", "描述部分", "其他数据"));

        options1Items.add(new ProvinceBean(2, "广西", "描述部分", "其他数据"));
        options1Items.add(new ProvinceBean(2, "江苏", "描述部分", "其他数据"));
        options1Items.add(new ProvinceBean(2, "四川", "描述部分", "其他数据"));



        //选项2

        ArrayList<String> options2Items_01 = new ArrayList<>();

        options2Items_01.add("广州");

        options2Items_01.add("佛山");

        options2Items_01.add("东莞");

        options2Items_01.add("珠海");

        ArrayList<String> options2Items_02 = new ArrayList<>();

        options2Items_02.add("长沙");

        options2Items_02.add("岳阳");

        options2Items_02.add("株洲");

        options2Items_02.add("衡阳");

        ArrayList<String> options2Items_03 = new ArrayList<>();

        options2Items_03.add("桂林");

        options2Items_03.add("玉林");
        ArrayList<String> options2Items_04 = new ArrayList<>();

        options2Items_04.add("南京");

        options2Items_04.add("淮安");
        options2Items_04.add("扬州");
        options2Items_04.add("苏州");
        options2Items_04.add("泰州");
        options2Items_04.add("徐州");
        options2Items_04.add("常州");
        options2Items_04.add("无锡");
        options2Items_04.add("南通");
        options2Items_04.add("盐城");
        options2Items_04.add("宿迁");
        options2Items_04.add("连云港");
        ArrayList<String> options2Items_05 = new ArrayList<>();

        options2Items_05.add("成都");
        options2Items_05.add("自贡");
        options2Items_05.add("攀枝花");
        options2Items_05.add("泸州");
        options2Items_05.add("德阳");
        options2Items_05.add("绵阳");
        options2Items_05.add("广元");
        options2Items_05.add("遂宁");
        options2Items_05.add("内江");
        options2Items_05.add("乐山");
        options2Items_05.add("巴中");
        options2Items_05.add("宜宾");
        options2Items_05.add("广安");
        options2Items_05.add("达州");
        options2Items_05.add("眉山");
        options2Items_05.add("雅安");
        options2Items_05.add("资阳");
        options2Items_05.add("阿坝");
        options2Items_05.add("甘孜");
        options2Items_05.add("凉山");
        options2Items_05.add("南充");
        options2Items.add(options2Items_01);

        options2Items.add(options2Items_02);

        options2Items.add(options2Items_03);
        options2Items.add(options2Items_04);
        options2Items.add(options2Items_05);



        /*--------数据源添加完毕---------*/

    }


}
