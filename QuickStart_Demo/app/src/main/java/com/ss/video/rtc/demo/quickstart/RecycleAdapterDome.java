package com.ss.video.rtc.demo.quickstart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ss.rtc.demo.quickstart.R;

import java.util.List;

/*
① 创建一个继承RecyclerView.Adapter<VH>的Adapter类
② 创建一个继承RecyclerView.ViewHolder的静态内部类
③ 在Adapter中实现3个方法：
   onCreateViewHolder()
   onBindViewHolder()
   getItemCount()
*/
public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder>{
    public Object setOnItemClick;
    private Context context;
    private List<String> list;
    private View inflater;
    private OnItemClick ItemClick;
    public static int mposition;
    //构造方法，传入数据
    public RecycleAdapterDome(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }

    public void addItem(int position, String element){
        if (position >= 0) {
            //给adapter里面的数据集合添加元素
            list.add(position, element);
            //添加插入后的动画效果
            notifyItemInserted(position);
            //插入后,更新后面item的position
            notifyItemRangeChanged(position, list.size() - position, "addItem");
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.recycle_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    public  void setOnItemClick(OnItemClick Item){
        this.ItemClick=Item;

    }
    public  interface OnItemClick
    {
        void ItemClick(int position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //将数据和控件绑定
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ItemClick != null) {
                    mposition=position;
                    ItemClick.ItemClick(position);
                }
            }
        });
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }


    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.msg);

        }
    }
}


