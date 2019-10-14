package com.websarva.wings.android.mapsecond;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Locale;

class SendBitmapInBackGround {

    private String str_image;
    SendBitmapInBackGround(Bitmap image){
        str_image = encodeTobase64(image);
    }


    Boolean send_bm(){


        try {




            String jsonStr = String.format("{ \"content\":\"%s\"}", str_image);

            URL url = new URL("http://localhost:8000/accounts/image/");

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

            ps.print(jsonStr);


            ps.close();

            int statusCode = urlConnection.getResponseCode();

            return statusCode == HttpURLConnection.HTTP_OK;

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }


    private static String encodeTobase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b,Base64.DEFAULT);
    }

}
