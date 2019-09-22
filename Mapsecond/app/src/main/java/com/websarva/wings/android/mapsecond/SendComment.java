package com.websarva.wings.android.mapsecond;

import android.os.AsyncTask;



public class SendComment extends AsyncTask<Void, Void, Boolean> {

    private double latitude, longitude;

    private String comment;

    private sendCommentCallBack sendcommentcallback;

    SendComment(String c,double lat,double lon){

        comment = c;

        latitude = lat;

        longitude = lon;

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            return new SendCommentBackGround(comment, latitude, longitude).postPOST();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    protected void onPostExecute(final Boolean result) {
        super.onPostExecute(result);
        sendcommentcallback.CallBack(result);
    }

    void setSendCommentCallBack(sendCommentCallBack scb){
        sendcommentcallback = scb;
    }

    public static class sendCommentCallBack{
        public void CallBack(Boolean result){

        }
    }

}