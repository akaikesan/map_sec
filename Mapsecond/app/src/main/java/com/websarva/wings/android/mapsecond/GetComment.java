package com.websarva.wings.android.mapsecond;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


class GetComment extends AsyncTask<Void, Void, String> {

    private float lat;
    private float lon;

    private gcCallBackTask callbacktask;





    GetComment(float lat,float lon) {
        this.lat = lat;
        this.lon = lon;
    }



    @Override
    protected String doInBackground(Void... params) {

        GetCommentInBackGround gc;

        double range = 234.0/(2*Math.PI*Math.cos(lat)*6371.0);

        try{

            Log.wtf("Getcomment","beforeGetcommentinbackground");

            gc = new GetCommentInBackGround(lat,lon,Math.abs(range));
            String result_comment = gc.postCom();

            if(result_comment == null){
                return null;
            }
            JSONObject json = new JSONObject(result_comment);
            JSONArray key = json.names ();
            for (int i = 0; i < key.length (); ++i) {
                String keys = key.getString (i);
                JSONObject value = json.getJSONObject(keys);


                //you put argument value.getString(username) in getImage on Production Environment.
            }

            return result_comment;


        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        callbacktask.gcCallBack(result);

    }

    void setOnCallBack(gcCallBackTask _cbj) {
        callbacktask = _cbj;
    }

    /**
     * コールバック用のstaticなclass
     */
    static class gcCallBackTask {
        void gcCallBack(String result) {
        }
    }


}