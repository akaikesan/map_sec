package com.websarva.wings.android.mapsecond;

import android.os.AsyncTask;



public class SendComment extends AsyncTask<Void, Void, Boolean> {
    private boolean tc;

    private double latitude, longitude;

    private String comment;

    SendComment(String c,double lat,double lon){

        comment = c;

        latitude = lat;

        longitude = lon;

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            tc = new SendCommentBackGround(comment,latitude,longitude).postPOST();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }



    @Override
    protected void onPostExecute(final Boolean success) {

    }

}