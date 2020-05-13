package cn.edu.bzu.ie.note;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

public class override_listview extends ListView {
    public override_listview(Context context) {
        super(context);
    }

    public override_listview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public override_listview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //
            case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                //返回记录数据行数
                int itemnum = pointToPosition(x, y);

                if (itemnum == AdapterView.INVALID_POSITION)
                    break;
                else{
                    if(itemnum==0){
                        if(itemnum==(getAdapter().getCount()-1)){
                            setSelector(R.drawable.listview_corner); //仅仅一行记录的样式
                        }else{
                            setSelector(R.drawable.listview_corner); //多行且第一行的样式
                        }
                    }else if(itemnum==(getAdapter().getCount()-1))  //最后一行的样式
                        setSelector(R.drawable.listview_corner);
                    else{
                        setSelector(R.drawable.listview_corner);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
}

