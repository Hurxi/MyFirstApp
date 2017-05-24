package com.example.myfirstapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirstapp.utils.MyDBHelper;

public class RegeistActivity extends BaseActivity {
    EditText etUserName,etUserPwd,etUserPws2;
    Button btnRegeist;
    MyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regeist);
        Toolbar toolbar=(Toolbar)findViewById(R.id.tool_bar_regeist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("注册");
        etUserName=(EditText)findViewById(R.id.et_regeist_name);
        etUserPwd=(EditText)findViewById(R.id.et_regeist_pwd);
        etUserPws2=(EditText)findViewById(R.id.et_regeist_pwd2);
        btnRegeist=(Button)findViewById(R.id.btn_regeist);
        dbHelper=new MyDBHelper(this,"MyUser.db",null,4);
        btnRegeist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册用户 去数据库的用户表中 插入一条数据
                String userName=etUserName.getText().toString();
                String userPwd=etUserPwd.getText().toString();
                String userPwd2=etUserPws2.getText().toString();
                if(userName.isEmpty()||userPwd.isEmpty()||userPwd2.isEmpty()){
                    Toast.makeText(RegeistActivity.this,"请输入账号信息",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(!userPwd.equals(userPwd2)){
                        Toast.makeText(RegeistActivity.this,"两次输入密码不一致，请重新输入",
                                Toast.LENGTH_SHORT).show();
                        etUserPwd.setText("");
                        etUserPws2.setText("");
                    }
                    else{
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        //数据库 有则拿到 无则创建
                        ContentValues values=new ContentValues();
                        values.put("userName",userName);
                        values.put("userPwd",userPwd);

                        long id=db.insert("UserInfo",null,values);
                        if(id!=-1) {
                            Toast.makeText(RegeistActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(RegeistActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }
}
