package cn.edu.bzu.ie.note;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    NoteDataBase dbHelper = new NoteDataBase(this);

    private EditText userName;
    private EditText passWord;
    private Context context = this;
    private String pw;//获取输入框的密码
    public static String user;//定义一个静态变量，CRUD中会用到，//获取输入框的用户名


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userName = findViewById(R.id.Et_account);
        passWord = findViewById(R.id.Et_password);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置转态栏字体为黑色
        //getWindow().getDecorView().setSystemUiVisibility(0);  //置转态栏字体为白色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);//设置转态栏背景为白色
        }
        //getWindow().setStatusBarColor(getColor(R.color.accentColor));

        //点击注册进入注册页面
        TextView register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivityForResult(intent,10);
                finish();
            }
        });

        //点击登录按钮
        Button btn_login = findViewById(R.id.login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = userName.getText().toString();//获取输入框的用户名
                pw = passWord.getText().toString();//获取输入框的密码

                if(user.isEmpty()){ //判断是否为空
                    Toast.makeText(context,"请输入账号",Toast.LENGTH_SHORT).show();
                }
                else if(pw.isEmpty()){
                    Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
                }else {
                    if(login(user, pw)){ //返回true时，说明用户名和密码在数据库中存在
                        Toast.makeText(Login.this,"登录成功！",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(Login.this,"密码或用户名错误！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //注册完后会返回登录界面，并执行onPostResume函数
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intent = getIntent();
        String values = intent.getStringExtra("u");
        userName.setText(values);
}

    //验证登录
    public boolean login(String userName,String passWord){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from UserTable where user=? and passWord=?";
        Cursor cursor = db.rawQuery(sql,new String[]{userName,passWord});
        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        }else {
            cursor.close();
            db.close();
            return false;
        }
    }
}
