package com.websarva.wings.android.mapsecond;

class RowData {

    /**
     * Created by naoi on 2017/04/25.
     */


    private String comment;
    private int fav;

    void setFav(int fav){
        this.fav = fav;
    }

    int getFav(){
        return fav;
    }

    String getTitle(){
        return comment;
    }

    void setTitle(String s){
        comment = s;
    }
}
