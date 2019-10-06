package com.websarva.wings.android.mapsecond;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Locale;

class GetImageInBackground {





    static Bitmap getImage(String username) {
        try {
            URL url = new URL(GlobalValue.getScheme() + "://" + GlobalValue.getHost() + ":" + GlobalValue.getPort() + "/" + GlobalValue.getPath() + "/image/" + username);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            Log.wtf("GetImageInBackGround","HttpConnection started");

            urlConnection.setConnectTimeout(100000);
            //レスポンスデータ読み取りタイムアウトを設定する。
            urlConnection.setReadTimeout(100000);
            //ヘッダーにUser-Agentを設定する。
            urlConnection.setRequestProperty("User-Agent", "Android");
            //ヘッダーにAccept-Languageを設定する。
            urlConnection.setRequestProperty("Accept-Language", Locale.getDefault().toString());

            urlConnection.setRequestMethod("GET");
            //HTTPのメソッドをPOSTに設定する。
            //you can get String Cookie from the method below.
            GetCookiesToSet cookie_getter =
                    new GetCookiesToSet(GlobalValue.getScheme(), GlobalValue.getHost(), GlobalValue.getPort(), GlobalValue.getPath());

            String cookie_in_request = cookie_getter.getStringCookie();

            urlConnection.setRequestProperty("Cookie", cookie_in_request);

            urlConnection.setDoInput(true);

            urlConnection.connect();

            int statusCode = urlConnection.getResponseCode();

            if(statusCode== HttpURLConnection.HTTP_OK){
                final InputStream in = urlConnection.getInputStream();
                System.out.println(urlConnection.getInputStream());

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;


                return BitmapFactory.decodeStream(in,null,bmOptions);

            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



}
