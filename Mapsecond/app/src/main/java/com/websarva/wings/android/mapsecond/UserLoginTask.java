package com.websarva.wings.android.mapsecond;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

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

            SharedPreferences data = context.getSharedPreferences("DataSave", Context.MODE_PRIVATE);


            try {
                SharedPreferences.Editor editor = data.edit();
                editor.putString("EM", mEmail);

                SecretKeySpec keySpec = new SecretKeySpec("abcdefg098765432".getBytes(), "AES"); // キーファイル生成
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                byte[] encrypted = cipher.doFinal(mPassword.getBytes()); // byte配列を暗号化
                String up = Base64.encodeToString(encrypted, Base64.DEFAULT); // Stringにエンコード

                // 入力されたログインIDとログインパスワード
                editor.putString("PW", up);
                editor.putBoolean("Is_Logined_device",true);

                // 保存
                editor.apply();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);

        }




    }


}
