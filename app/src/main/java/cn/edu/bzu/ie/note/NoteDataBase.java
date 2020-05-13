package cn.edu.bzu.ie.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteDataBase extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "notes";
    public static final String ID = "_id";//命名规则，有下划线的是主键
    public static final String CONTENT = "content";
    public static final String TIME = "time";
    public static final String TAG = "tag";

    //创建数据库note_database
    public NoteDataBase(@Nullable Context context) {

        super(context, "NoteDataBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //表的创建：方法一
        //AUTOINCREMENT:自增一
        //DEGAULT:默认为1
       /* db.execSQL("CREATE TABLE NoteData(_id INTEGER PRIMARY KEY AUTOINCREMENT,conten TEXT NOT NULL," +
                "time TEXT NOT NULL,tag INTEGER DEFAULT 1)");*/
        //方法二
            db.execSQL( "create table " + TABLE_NAME + "("
                + ID + " integer primary key autoincrement,"
                + CONTENT + " text not null ,"
                + TIME + " text not null,"
                + TAG + " ingeter default 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
