package com.websarva.wings.android.mapsecond;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;
    TextView favView;
    TextView userNameView;
    CircleImageView imageView;

    MyViewHolder(@NonNull View itemView) {
        super(itemView);


        titleView =  itemView.findViewById(R.id.comment);

        favView = itemView.findViewById(R.id.fav);
        favView.setTypeface(Typeface.MONOSPACE);

        userNameView = itemView.findViewById(R.id.username);
        userNameView.setTypeface(Typeface.DEFAULT_BOLD);

        imageView = itemView.findViewById(R.id.iconImage);

    }
}
