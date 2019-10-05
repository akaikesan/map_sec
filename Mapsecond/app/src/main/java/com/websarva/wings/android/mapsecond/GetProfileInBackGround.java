package com.websarva.wings.android.mapsecond;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Locale;

class GetProfileInBackGround {

    static String getGET(){
        try {
            URL url = new URL(GlobalValue.getScheme() + "://"+ GlobalValue.getHost()+":"+GlobalValue.getPort()+ "/"+GlobalValue.getPath()+"/profile/");



            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();




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
                    new GetCookiesToSet(GlobalValue.getScheme(),GlobalValue.getHost(),GlobalValue.getPort(),GlobalValue.getPath());

            String cookie_in_request = cookie_getter.getStringCookie();

            urlConnection.setRequestProperty("Cookie",cookie_in_request);

            urlConnection.connect();




            int statusCode = urlConnection.getResponseCode();

            if(statusCode==HttpURLConnection.HTTP_OK){
                Log.wtf("GetProfileInBackGround","profile response below");

                StringBuilder result = new StringBuilder();
                //responseの読み込み
                final InputStream in = urlConnection.getInputStream();
                String encoding = urlConnection.getContentEncoding();
                if(encoding == null) encoding = "utf-8";
                final InputStreamReader inReader = new InputStreamReader(in, encoding);

                final BufferedReader bufferedReader = new BufferedReader(inReader);


                String line;

                while((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                bufferedReader.close();
                inReader.close();
                in.close();


                return result.toString();


            }






        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }



}
