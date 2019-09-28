package com.websarva.wings.android.mapsecond;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<RowData> list;

    RecycleViewAdapter(List<RowData> list){
        this.list = list;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
