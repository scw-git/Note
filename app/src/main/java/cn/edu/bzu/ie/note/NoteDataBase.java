package cn.edu.bzu.ie.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteDataBase extends SQLiteOpenHelper {

    public static final String NOTES = "notes";
    public static final String ID = "_id";//命名规则，有下划线的是主键
    public static final String CONTENT = "content";
    public static final String TIME = "time";
    public static final String TAG = "tag";
    public static final String USER = " user";
    public static final String PASSWORD = "passWord";
    public static final String USERTABLE = "userTable";
    private Context context;

    //创建数据库note_database
    public NoteDataBase(@Nullable Context context) {

        super(context, "NoteDataBase", null, 1);
    }

    /*public NoteDataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        //表的创建：方法一
        //AUTOINCREMENT:自增一
        //DEGAULT:默认为1
       /* db.execSQL("CREATE TABLE NoteData(_id INTEGER PRIMARY KEY AUTOINCREMENT,conten TEXT NOT NULL," +
                "time TEXT NOT NULL,tag INTEGER DEFAULT 1)");*/
        //方法二
            db.execSQL( "create table " + NOTES + "("
                + ID + " integer primary key autoincrement,"
                    + USER + " string ,"
                + CONTENT + " text not null ,"
                + TIME + " text not null,"
                + TAG + " ingeter default 1)");

            db.execSQL("create table " + USERTABLE + "("
                    + USER  + " string primary key ,"
                    + PASSWORD + " string not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
