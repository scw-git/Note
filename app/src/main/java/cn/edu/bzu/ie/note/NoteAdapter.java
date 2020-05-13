package cn.edu.bzu.ie.note;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

public class NoteAdapter extends BaseAdapter {

    private  Context context;
    private List<Note> list;//List集合的变量
    private List<Note> copy_list;//用来备份的
    private Myfilter myfilter;

    //因为ListView里面是有很多个item子项需要被适配的，
    // 而且我上面已经定义好了一个实体类NOte用于封装每一个item子项内部需要适配的内容的一个集合，
    // 换句话说，每一个实体，就是一个完整的item里面所要适配的所有内容，
    // 那么我们又已知，一个ListView不一定只会有一个item，
    // 那么显然，我们需要在NOteAdapter里面定义一个泛型为Note的一个List，
    // 用于表示所有的item子项。
   public  NoteAdapter(Context context, List<Note> list){
        this.context = context;
        this.list = list;
        copy_list = list;
   }

    @Override
    //返回列表长度
    public int getCount() {
        return list.size();
    }

    @Override
    //输入一个位置，返回这个位置的对象
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    //输入位置，返回这个位置的ID
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //优化
        //LayoutInflater layoutInflater = LayoutInflater.from(context);
        //convertView = layoutInflater.inflater(R.layout.note_layout);
        ViewHolder viewHolder ;
        if(convertView==null){//没有可复用的，则创建一个
            convertView = View.inflate(context,R.layout.note_layout,null);//note_layout为ListView的子项

            viewHolder = new ViewHolder();

            viewHolder.tv_content = convertView.findViewById(R.id.tv_content);//获取控件的id
            viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_content.setText(list.get(position).getContent());//给控件赋值
        viewHolder.tv_time.setText(list.get(position).getTime());
        return convertView;
    }
    private class ViewHolder{
       TextView tv_content;
       TextView tv_time;
    }

    //实例化Myfilter对象myfilter
    public Filter getFilter(){
       if(myfilter==null){
           myfilter = new Myfilter();
       }
       return myfilter;
    }

    //自定义一个过滤器
    class Myfilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Note> query_lists;
            //对于字符串处理Android为我们提供了一个简单实用的TextUtils类，
            if(TextUtils.isEmpty(constraint)){//过滤的关键字为空时，列出所有内容
                query_lists = copy_list;//copy_list是上面定义用来备份数据的
            }else {
                query_lists = new ArrayList<>();
                for(Note note : copy_list){//把list中的每个元素赋值给Note类型note
                    if(note.getContent().contains(constraint)){//如果有笔记包含搜索的内容，则添加
                        query_lists.add(note);
                    }
                }
            }
            results.values = query_lists;//将得到的集合保存到FilterResults的values中
            results.count = query_lists.size();//将得到的集合大小保存到count中
            return results;
        }
        //告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (List<Note>) results.values;
            if(results.count>0){
                notifyDataSetChanged();//通知数据发生了改变
            }else{
                notifyDataSetInvalidated();//失败
            }
        }
    }
}
