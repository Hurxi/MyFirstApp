package com.example.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView imageView=(ImageView)findViewById(R.id.iv_image);
        Picasso.with(this).load(getIntent().getStringExtra("url"))
                .placeholder(R.color.colorPrimary)
                .into(imageView);
    }
}
