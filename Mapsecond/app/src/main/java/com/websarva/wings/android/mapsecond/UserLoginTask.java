package com.websarva.wings.android.mapsecond;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;

    Activity context;





    UserLoginTask(String email, String password,Activity context) {
        mEmail = email;
        mPassword = password;
        this.context=context;
    }

    private boolean sj;


    @Override
    protected Boolean doInBackground(Void... params) {











        try{
            Log.wtf("omg", "hahahahah2");

            sj = new SendJson(mEmail, mPassword).postPOST();



        }catch(Exception e){
            e.printStackTrace();
        }


//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {



        Log.wtf("omg", "hahahahah");


        if(sj){

            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);

        }




    }


}
