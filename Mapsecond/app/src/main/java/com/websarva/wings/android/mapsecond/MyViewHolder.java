package com.websarva.wings.android.mapsecond;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;
    TextView favView;

    MyViewHolder(@NonNull View itemView) {
        super(itemView);
        titleView =  itemView.findViewById(R.id.comment);
        favView = itemView.findViewById(R.id.fav);

    }
}
