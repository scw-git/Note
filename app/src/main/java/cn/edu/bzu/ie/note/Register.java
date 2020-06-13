package cn.edu.bzu.ie.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    private EditText userNumber;
    private EditText passWord;
    private NoteDataBase dbHelper = new NoteDataBase(this);
    private EditText againPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button button = findViewById(R.id.register);
        userNumber = findViewById(R.id.user);
        passWord = findViewById(R.id.passWord);
        againPassWord = findViewById(R.id.againpassWord);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置转态栏字体为黑色
        //getWindow().getDecorView().setSystemUiVisibility(0);  //置转态栏字体为白色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);//设置转态栏背景为白色
        }
        //getWindow().setStatusBarColor(getColor(R.color.accentColor));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userRegister = userNumber.getText().toString();//获取用户名
                String pw = passWord.getText().toString();//获取密码
                String apw = againPassWord.getText().toString();//获取确认密码
                if(userRegister.isEmpty()){
                    Toast.makeText(Register.this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
                }else if(pw.isEmpty()){
                    Toast.makeText(Register.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                }else if (apw.isEmpty()){
                    Toast.makeText(Register.this,"请输入确认密码！",Toast.LENGTH_SHORT).show();
                }else {
                    if(checkUser(userRegister)){
                        Toast.makeText(Register.this,"用户名已被注册，请更换！",Toast.LENGTH_SHORT).show();
                    }else if(!(pw.equals(apw))){
                        Toast.makeText(Register.this,"两次输入的密码不相同！",Toast.LENGTH_SHORT).show();
                    } else if (registerUser(userRegister,pw)){
                        Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this,Login.class);
                        intent.putExtra("u",userRegister);//用于回显用户名的
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    //向数据库插入数据
    public boolean registerUser(String userName,String passWord){
        SQLiteDatabase db = dbHelper.getWritableDatabase();//创建数据库和表
        ContentValues values = new ContentValues();
        values.put(NoteDataBase.USER,userName);
        values.put(NoteDataBase.PASSWORD,passWord);
        db.insert(NoteDataBase.USERTABLE,null,values);
        Log.d("test","用户名："+ userName + "密码："+ passWord );
        db.close();
        return true;
    }

    //检查用户名是否存在，返回Boolean型
    public boolean checkUser(String userName){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from UserTable where user =?",new String[]{userName});
        if(cursor.getCount()>0){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
}
