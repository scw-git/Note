package cn.edu.bzu.ie.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private Toolbar myToolbar;//设置自定义的Toolbar，要先隐藏默认的.在style文件中
    private EditText et;
    private String old_content = "";
    private String old_time = "";
    private int old_tag = 1;
    private long id = 0;
    private int openMode = 0;
    private int tag = 1;
    public  Intent intent = new Intent();
    private boolean TagChange = false;
    private static final String TAG = "EditActivity";//在oncreate外输入logt+回车

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        et = findViewById(R.id.et);

        //设置左上角的图标及事件
        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);//用自定义的取代默认的，要先隐藏默认的.在style文件中
        myToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);//设置一个向左的小箭头
        getSupportActionBar().setTitle("新建");//设置标题
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JudgeUpOrNew ();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //设置转态栏颜色，和字体的颜色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置转态栏字体为黑色
        //getWindow().getDecorView().setSystemUiVisibility(0);  //置转态栏字体为白色
        getWindow().setStatusBarColor(Color.WHITE);//设置转态栏背景为白色
        //getWindow().setStatusBarColor(getColor(R.color.accentColor));

        //接受点击item传回来的数据（对数据的处理在系统键盘事件中）
        Intent intent = getIntent();//获取getIntent对象，用来接受数据
        openMode = intent.getIntExtra("mode",0);//获取传入的值
        Log.d("test","点击了+号，openMode是" + openMode);//3是修改，4是新建,
        if(openMode == 3){//获取传回来的内容,好像数字类型的都有默认值
            getSupportActionBar().setTitle("编辑");//设置标题
            id = intent.getLongExtra("id",0);
            old_content = intent.getStringExtra("content");
            old_time = intent.getStringExtra("time");
            old_tag = intent.getIntExtra("tag",1);
            et.setText(old_content);//把获取的内容显示
            et.setSelection(old_content.length());//把光标放在文章后面
            Log.d("test","点击了item，openMode是"+ openMode+ "获取了传回来的数据");

        }
    }

    //显示右上角的图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edti_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //上角的监听器
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("确定删除此笔记？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(openMode==4){//从新建笔记进来的
                            intent.putExtra("mode",-1);
                            setResult(1,intent);
                        }else if(openMode==3){//从item进来的
                           intent.putExtra("id",id);
                           intent.putExtra("mode",2);
                           setResult(RESULT_OK,intent);
                        }
                        finish();//点击确认，关闭页面
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //设置系统键盘事件
    public boolean onKeyDown(int keycode, KeyEvent event){
        //按返回键时，判断是来自新建的，还是修改的
        if (keycode == KeyEvent.KEYCODE_HOME){
            return true;//如果直接按home键，则直接退出
        }
        else if(keycode == KeyEvent.KEYCODE_BACK){
            JudgeUpOrNew (); //判断点击是来自新建还是item
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keycode,event);
    }

    //判断点击是来自新建还是item
    public void JudgeUpOrNew (){

        Log.d("test","在JudgeUpOrNew中 openMode 是" + openMode);
        if(openMode==4){//点击了+号，即新建按钮
            if(et.getText().toString().length()==0){
                intent.putExtra("mode",-1);//什么都不做,因为没有数据putExtra
            }else {
                intent.putExtra("content", et.getText().toString());
                intent.putExtra("time", timeStr());
                intent.putExtra("tag",tag);
                intent.putExtra("mode",0);//新建笔记
                Log.d("test","执行了新建笔记，并把数据返回了" );

            }
        }else if(openMode==3){//从ListView点击了item
            if(et.getText().toString().equals(old_content) && !TagChange){//内容没有修改,且标签也没有呢修改
                intent.putExtra("mode",-1);//什么也不执行
            }else {
                intent.putExtra("content", et.getText().toString());
                intent.putExtra("time", timeStr());
                intent.putExtra("id",id);//这个id一定是你点击的那个id
                intent.putExtra("tag",tag);
                intent.putExtra("mode",1);//修改笔记
                Log.d("test","执行了修改笔记，并把数据返回了");

            }
        }

    }

    //返回时间，和定义显示格式
    public String timeStr(){
        //获取时间对象
        Date date = new Date();
        //定义时间格式,月份必须用MM，分钟必须用mm，大写H表示24小时格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //返回按要求定义的时间格式
        return simpleDateFormat.format(date);
    }

}
