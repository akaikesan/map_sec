package com.websarva.wings.android.mapsecond;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<RowData> list;

    private Context context = null;

    RecycleViewAdapter(List<RowData> list){
        this.list = list;
    }

    RecycleViewAdapter(List<RowData> list, Context context){

        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,viewGroup,false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titleView.setText(list.get(position).getTitle());
        holder.favView.setText(String.valueOf(list.get(position).getFav()));
        holder.userNameView.setText(list.get(position).getUsername());
        String url = "http://" + GlobalValue.getHost() + ":" + GlobalValue.getPort() + "/" + GlobalValue.getPath() + "/image/"+list.get(position).getUsername();
        if(context != null)  Glide.with(context).load(url).into(holder.imageView);
    }

    //Finally, what you want to display is displayed.

    @Override
    public int getItemCount() {
        return list.size();
    }
}
