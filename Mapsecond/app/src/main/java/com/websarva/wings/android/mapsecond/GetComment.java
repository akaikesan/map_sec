package com.websarva.wings.android.mapsecond;


import android.os.AsyncTask;


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

            gc = new GetCommentInBackGround(lat,lon,Math.abs(range));
            return gc.postCom();

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