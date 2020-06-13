package cn.edu.bzu.ie.note;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CRUD  {
    //SQLiteOpenHelper helper;
    //继承的数据库类，创建了一个对象。NoteDataBase继承了SQLiteOpenHelper
    NoteDataBase helper;
    SQLiteDatabase db;

 /*   //1代表ID，2代表CONTENT
    private static final String[] column = {
            NoteDataBase.ID,
            NoteDataBase.CONTENT,
            NoteDataBase.TIME,
            NoteDataBase.TAG,}*/;

    //继承的数据库类，创建了一个对象。

    public CRUD(){}
    public CRUD(Context context){
        helper = new NoteDataBase(context);
    }

    //打开数据库
    //单独的数据库对象helper是不能操作数据库的,要与可以操作数据库的SQLiteDatabase 对象db相关联
    public void open(){
        db = helper.getWritableDatabase();
    }
    //关闭数据库
    public void close(){
        db.close();
    }

    //添加
    //类名 + 方法名这种方法可以返回多个不同类型的值
    public  Note addNote(Note note){//既添加了数据有返回了id值
        ContentValues values = new ContentValues();
        //key值表示插入的列名，values表示内容
        //把要插入的内容暂时放到values中
        //Note note = null;
        values.put(NoteDataBase.CONTENT,note.getContent());
        values.put(NoteDataBase.TIME,note.getTime());
        values.put(NoteDataBase.TAG,note.getTag());
        values.put(NoteDataBase.USER,note.getUser());
        //db已经与数据库相关联了，可以操作数据了
        long insertId = db.insert(NoteDataBase.NOTES,null,values);
        note.setId(insertId);
        return note;
    }
    //查询单个
  /*  public Note getNote(long id){
        //数据表
        //列名称数组
        //查找条件
        //查找条件的值
        //将id转换为字符串
        //.....
       Cursor cursor = db.query(NoteDataBase.NOTES,column,NoteDataBase.ID +"=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null) cursor.moveToFirst();//把游标放到第一的位置
        Note note = new Note(cursor.getString(1),cursor.getString(2),cursor.getInt(3));
        return note;
    }*/

    //查询所有
    //返回一个 Note对象的List集合
    //返回结果有不同类型时，用泛型
    //List系统定义了泛型，可以返回多个不同类型的值。也可以用类名+方法名，return +类名对象。

    /*List指的是集合.<>是泛型,里面指定了这个来集合中存放的是什么数据.
    比如有一个学生类Student,Student里面包含了学生的一些信息源.这样每一个Student对象百就代表了一个学生.
    此时List<Student>就代表这个集合中存放了很多个度学生对象,这个集合就像一个班级一样.*/
    public List<Note> getAllNotes(){
        //Cursor是每行的集合,rawQuery相当于用SQL语法查询
        Cursor cursor = db.rawQuery("select * from notes where user =?",new String[]{Login.user});

        //ArrayList提供动态添加和减少元素，实现了接口List
        List<Note> notes = new ArrayList<>();
        if(cursor.getCount()>0){//或者用cursor.moveToFirst
            while (cursor.moveToNext()){//执行一次就获得一行
                Note note = new Note();//有两个构造函数，调用的是无参数的
                //用note获得ID，content等内容，然后把它们加到Note 创建的对象里
                //列的类型：cursor.getLong()
                //列名：cursor.getColumnIndex(NoteDataBase.ID)
               // note.setUser(cursor.getString(cursor.getColumnIndex(NoteDataBase.USER)));
                note.setId(cursor.getLong(cursor.getColumnIndex(NoteDataBase.ID)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NoteDataBase.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(NoteDataBase.TIME)));
                note.setTag(cursor.getInt(cursor.getColumnIndex(NoteDataBase.TAG)));
                //ArrayList有add方法
                notes.add(note);
            }
            cursor.close();
        }
        return notes;
    }

    //更新
    public int updataNote(Note note){
        ContentValues values = new ContentValues();
        values.put(NoteDataBase.CONTENT,note.getContent());
        values.put(NoteDataBase.TIME,note.getTime());
        values.put(NoteDataBase.TAG,note.getTag());
        return db.update(NoteDataBase.NOTES,values,NoteDataBase.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    //通过ID删除
    public void removeNote(Note note){
        db.delete(NoteDataBase.NOTES,NoteDataBase.ID + "=" + note.getId(),null);
    }
}
