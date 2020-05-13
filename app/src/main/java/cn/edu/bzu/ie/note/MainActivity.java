package cn.edu.bzu.ie.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    //private NoteDataBase dbhelper;
    private FloatingActionButton float_btn;
    private Context context = this;
    private Toolbar myToolbar;//设置自定义的Toolbar，要先隐藏默认的.在style文件中
    //private Menu search;

    //一般三个同时出现
    private NoteAdapter adapter;
    private List<Note> list ;
    private ListView  listView;
    private static final String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = findViewById(R.id.myToolbar);
        float_btn = findViewById(R.id.float_btn);
        listView = findViewById(R.id.lv);
        //search = findViewById(R.id.search1);

        //设置适配器
        list = new ArrayList<>();
        adapter = new NoteAdapter(context,list);
        listView.setAdapter(adapter);
        refreshListView();//刷新列表并显示数据

        listView.setOnItemClickListener(this);//设置点击列表的监听器
        listView.setOnItemLongClickListener(this);//设置长按列表的监听器

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置转态栏字体为黑色
        //getWindow().getDecorView().setSystemUiVisibility(0);  //置转态栏字体为白色
        getWindow().setStatusBarColor(Color.WHITE);//设置转态栏背景为白色
        //getWindow().setStatusBarColor(getColor(R.color.accentColor));

        //用自定义的取代默认的，要先隐藏默认的.在style文件中
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow();
            }
        });

        //点击新建笔记，产生的事件
        float_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("mode",4);//用于标识新建的笔记
                startActivityForResult(intent,1); //用到Activity之间的数据回传
            }
        });
    }


    /*public static boolean setStatusBarTextColor(Window window, boolean lightStatusBar) {
        // 设置状态栏字体颜色 白色与深色
        View decor = window.getDecorView();
        int ui = decor.getSystemUiVisibility();
        ui |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (lightStatusBar) {
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        }
        decor.setSystemUiVisibility(ui);

    }*/
    //显示弹窗（合并版）
    public void showPopUpWindow(){
        View contentView = View.inflate(context,R.layout.setting_layout,null);//渲染弹窗视图
        View cover = View.inflate(context,R.layout.setting_cover,null);//渲染灰色半透明

        DisplayMetrics metrics = new DisplayMetrics();//获取屏幕大小
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final PopupWindow popupWindow = new PopupWindow(contentView,(int)(width*0.7),height,true);//设置弹窗
        final PopupWindow popupCover = new PopupWindow(cover,width,height,false);//设置灰色半透明铺满全屏

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));//设置白色背景，默认是透明的
        popupCover.showAtLocation(findViewById(R.id.main_layout),Gravity.LEFT,0,0);
        popupWindow.showAtLocation(findViewById(R.id.main_layout),Gravity.LEFT,0,0);//相对于main_layout的左侧显示

        cover.setOnTouchListener(new View.OnTouchListener() {//点击灰色半透明，弹窗关闭
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { //灰色半透明关闭
            @Override
            public void onDismiss() {
                popupCover.dismiss();
            }
        });


    }
    

    //接收从EditActivity传回来的数据（然后判断是新建的笔记，还是修改的笔记）
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        long note_id;
        int returnMote;//接收返回的mode。什么也不做：-1 新建笔记：0 修改笔记：1
        returnMote = data.getIntExtra("mode",-1);//必须指定默认值
        note_id = data.getLongExtra("id",0);//获取到的值会覆盖默认值

        if(returnMote == 1) {//更新笔记
            //获取更新的内容
            String content = data.getStringExtra("content");
            String time = data.getStringExtra("time");
            int tag = data.getIntExtra("tag",1);
            //更新
            Note note = new Note(content,time,tag);
            note.setId(note_id);//会根据这个ID，修改笔记
            CHAOZHUO chaozhuo = new CHAOZHUO(context);
            chaozhuo.open();
            chaozhuo.updataNote(note);
            chaozhuo.close();
        }else if(returnMote == 0) {//传回来的是新建笔记
            String content = data.getStringExtra("content");
            String time = data.getStringExtra("time");
            int tag = data.getIntExtra("tag",1);


            //对数据操作，把内容，时间都放在note中
            Note note = new Note(content, time, tag);
            CHAOZHUO cz = new CHAOZHUO(context);
            cz.open();
            cz.addNote(note); //把数据添加到数据库
            cz.close();
        }else if(returnMote == 2) {//从EditActivity点击了删除按钮
            Note note = new Note();
            note.setId(note_id);
            CHAOZHUO cz = new CHAOZHUO(MainActivity.this);
            cz.open();
            cz.removeNote(note);
            cz.close();
        }
        refreshListView();  //接收到数据就刷新
        super.onActivityResult(requestCode, resultCode, data);
}

    //刷新ListView
    public void refreshListView(){
        CHAOZHUO cz = new CHAOZHUO(context);//开头定义了Context context = this;
        cz.open();//先打开数据库
        list.clear();
        list.addAll(cz.getAllNotes());
        cz.close();
        adapter.notifyDataSetChanged();
        Log.i("tags","qaa成功");
    }

    //点击item，进入查看或者修改笔记（要在前面设置：listView.setOonItemClick(this);）
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv :
                Note note = (Note) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("id", note.getId());
                intent.putExtra("content", note.getContent());
                intent.putExtra("time", note.getTime());
                intent.putExtra("tag", note.getTag());
                //用于标识更新的笔记
                intent.putExtra("mode",3);//相当于一个标识。便于在EditActivity中接收数据
                startActivityForResult(intent, 3);
                break;
        }
    }

    //长按删除笔记（要在前面设置：listView.setOnItemLongClickListener(this);）
    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定删除此条笔记？");
        //获得item的ID
        //final Note note = (Note) parent.getItemAtPosition(position);
        final Note note = list.get(position);//上下两条等价

        //设置确认键
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CHAOZHUO cz = new CHAOZHUO(context);
                cz.open();
                cz.removeNote(note);
                cz.close();
                refreshListView();//刷新
                dialog.dismiss();
                Log.d("test","删除成功 ");
            }
        });
        //设置取消键
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //创建对话框并显示
        builder.create().show();
        return true;
    }

    //按两次退出应用 (虚拟机长按也会退出，手机不会)
    private long exitTime = 0;//必须要声明全局变量，否则不起作用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK &&  event.getAction() == KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis() - exitTime>2000){
                Toast.makeText(context,"在按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else {
               super.onBackPressed();//不用finnish()和system.exit(0),退出动画不流畅
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //方法二：（虚拟机长按退出键，没反应。手机正常）
 /*   private long exitTime = 0;//必须要声明全局变量，否则不起作用
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - exitTime >2000){
            Toast.makeText(context,"在按一次退出",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else {
            super.onBackPressed();
        }
    }*/

    //显示右上角的图标,和搜索功能
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("test","執行了");
        getMenuInflater().inflate(R.menu.main_menu,menu);//设置(显示)自定义的菜单

        //因为搜索框是在标题栏，搜索的内容也在。所以要写在onCreateOptionsMenu中
        MenuItem msearch = menu.findItem(R.id.search1);
        SearchView searchView = (SearchView) msearch.getActionView();//创建搜索界面
        //searchView.setQueryHint("搜索");//搜索的提示
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;//此方法用于有个搜索按钮（即输入完内容，要点击搜索）
            }

            @Override//内容改变时搜索
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    //右上角图标的监听器，点击右上角删除全部图标
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.all_delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("确定清空所有笔记吗？");

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteDataBase helper = new NoteDataBase(MainActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.delete("notes",null,null);
                        //更新sqlite_sequence表的序号为0，当表的名字为notes时
                        //不清空，会接着以前的序号
                        db.execSQL("update sqlite_sequence set seq=0 where name='notes'");
                        refreshListView();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//关闭对话框
                    }
                });
                builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
