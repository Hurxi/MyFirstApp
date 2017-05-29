package com.example.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class CityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Toolbar toolbar=(Toolbar)findViewById(R.id.tool_bar_city);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("城市管理");
        ImageButton ibSelectCity=(ImageButton)findViewById(R.id.ib_select_city);
        ibSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CityActivity.this,SelectCityActivity.class);
                startActivity(intent);
            }
        });
    }
}
