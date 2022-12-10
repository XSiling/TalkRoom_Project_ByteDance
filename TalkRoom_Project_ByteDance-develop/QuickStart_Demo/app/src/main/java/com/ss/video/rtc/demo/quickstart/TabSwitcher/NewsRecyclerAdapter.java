package com.ss.video.rtc.demo.quickstart.TabSwitcher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.rtc.demo.quickstart.R;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<NewsItem> items;
    private View inflater;
    private NewsFragment parentFragment;
    private OnItemClickListener listener;


    public void setonClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public NewsRecyclerAdapter(Context context, List<NewsItem> items, NewsFragment parent) {
        this.context = context;
        this.items = items;
        this.parentFragment = parent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context).inflate(R.layout.new_item, parent, false);
        return new MyViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewsItem item = items.get(position);
        holder.name.setText(item.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentFragment.getActivity(), WebPage.class);
                intent.putExtra("url", item.url);
                parentFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

}
