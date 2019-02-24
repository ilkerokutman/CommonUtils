package com.performans.commonutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyModel> itemList;

    public MyAdapter(Context mContext, List<MyModel> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder holder,
                                 final int position) {
        final MyModel item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDesc());
        if (item.hasAction()) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //handle click
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc;
        public RelativeLayout layout;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.item_title);
            desc = view.findViewById(R.id.item_desc);
            layout = view.findViewById(R.id.item_layout);
        }
    }


}
