package com.websarva.wings.android.mapsecond;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Locale;

class SendCommentBackGround {

    private String sendData;


    SendCommentBackGround(String comment, double latitude,double longitude) {

        this.sendData = String.format("{ \"content\":\"%s\",\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\"}", comment);

    }





    boolean postPOST() throws JSONException {

        JSONObject responseJsonObject = new JSONObject(sendData);

        String jsonText = responseJsonObject.toString();


        try {
            URL url = new URL("http://localhost:8000/accounts/comment/");



            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");



            urlConnection.setConnectTimeout(100000);
            //レスポンスデータ読み取りタイムアウトを設定する。
            urlConnection.setReadTimeout(100000);
            //ヘッダーにUser-Agentを設定する。
            urlConnection.setRequestProperty("User-Agent", "Android");
            //ヘッダーにAccept-Languageを設定する。
            urlConnection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
            //ヘッダーにContent-Typeを設定する
            urlConnection.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
            //HTTPのメソッドをPOSTに設定する。
            urlConnection.setRequestMethod("POST");
            //you can get String Cookie from the method below.
            GetCookiesToSet cookie_getter = new GetCookiesToSet("http","localhost",8000,"accounts/");

            String cookie_in_request = cookie_getter.getStringCookie();

            urlConnection.setRequestProperty("Cookie",cookie_in_request);
            //リクエストのボディ送信を許可する
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            urlConnection.connect();



            OutputStream outputStream = urlConnection.getOutputStream();

            PrintStream ps = new PrintStream(outputStream);

            ps.print(jsonText);





            ps.close();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {


                return true;

            }
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return false;
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }
}
