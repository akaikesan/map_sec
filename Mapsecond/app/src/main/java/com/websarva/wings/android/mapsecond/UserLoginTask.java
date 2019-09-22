package com.websarva.wings.android.mapsecond;


import android.os.AsyncTask;
import android.util.Log;

class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;


    private CallBackTask callbacktask;





    UserLoginTask(String email, String password) {
        mEmail = email;
        mPassword = password;
    }



    @Override
    protected Boolean doInBackground(Void... params) {

        SendJson sj;


        try{
            Log.wtf("omg", "hahahahah2");

            sj = new SendJson(mEmail, mPassword);

            return sj.postPOST();


        }catch(Exception e){
            e.printStackTrace();
        }


        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        callbacktask.CallBack(result);


    }

    void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }


    /**
     * コールバック用のstaticなclass
     */
    static class CallBackTask {
        void CallBack(Boolean result) {
        }
    }


}
