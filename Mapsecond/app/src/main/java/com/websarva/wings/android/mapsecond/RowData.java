package com.websarva.wings.android.mapsecond;

class RowData {

    private String comment;
    private int fav;
    private String username;

    //been set in createDataset of CommentListActivity and gotten in RecycleViewAdapter.

    void setUsername(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }


    void setFav(int fav) {
        this.fav = fav;
    }

    int getFav() {
        return fav;
    }


    void setTitle(String s) {
        comment = s;
    }

    String getTitle() {
        return comment;
    }

}
