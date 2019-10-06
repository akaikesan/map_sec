package com.websarva.wings.android.mapsecond;

import android.graphics.Bitmap;

class RowData {

    private String comment;
    private int fav;
    private String username;
    private Bitmap image;

    //been set in createDataset of CommentListActivity and gotten in RecycleViewAdapter.

    void setUsername(String username){
        this.username = username;
    }

    String getUsername(){
        return username;
    }


    void setFav(int fav){
        this.fav = fav;
    }

    int getFav(){
        return fav;
    }



    void setTitle(String s){
        comment = s;
    }

    String getTitle(){
        return comment;
    }

    void setIcon(Bitmap image){
        this.image = image;
    }

    Bitmap getImage(){
        return image;
    }
}
