package cn.edu.bzu.ie.note;

import androidx.core.app.ActivityCompat;

public class Note {
    private long id;
    private String content;
    private String time;
    private int tag;

    //有了构造方法，则系统不会在创建默认的方法。只能自己在创建一个
    public Note(){ }

    /*public Note(String string, String cursorString, int anInt){
    }*/
    //构造方法
    public Note(String content,String time,int tag){
        //因为初始化函数和类中的变量名相同，所有要用this
        this.content = content;
        this.time = time;
        this.tag = tag;
    }


    //如果外部引用id content等时，只能用getId,getContent方法
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}



