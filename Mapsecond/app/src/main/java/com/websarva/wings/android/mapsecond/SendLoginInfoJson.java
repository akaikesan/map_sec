package com.websarva.wings.android.mapsecond;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

class SendLoginInfoJson {


    private String sendData;

    SendLoginInfoJson(String email, String password){
        this.sendData = String.format("{ \"email\":\"%s\",\"password\":\"%s\"}", email ,password);

    }

    Boolean postPOST() throws Exception {

        JSONObject responseJsonObject = new JSONObject(sendData);

        String jsonText = responseJsonObject.toString();
        HttpUrl httpurl = new HttpUrl.Builder().scheme(GlobalValue.getScheme()).host(GlobalValue.getHost()).port(GlobalValue.getPort()).addPathSegment(GlobalValue.getPath()).build();

        try {
            //URL url = new URL("https://mapweb.herokuapp.com/accounts/");
            URL url = new URL(GlobalValue.getScheme() + "://"+ GlobalValue.getHost()+":"+GlobalValue.getPort()+ "/"+GlobalValue.getPath()+"/");


            //URI uri = new URI("/");



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



                final String COOKIES_HEADER = "Set-Cookie";

                Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                List<String> cookiesHeader =  headerFields.get(COOKIES_HEADER);


                WebviewCookieHandler ch = new WebviewCookieHandler();

                //Log.wtf("wtf","cookiesHeader");
                //System.out.println(cookiesHeader);
                //Log.wtf("wtf","HeaderFields");
                //System.out.println(headerFields);




                if (cookiesHeader != null) {

                    String[] cookie_list = cookiesHeader.get(0).split(";");

                    List<Cookie> cookies_al = new ArrayList<>(cookie_list.length);

                    //Log.wtf("wtf","List element");
                    //System.out.println(Arrays.toString(cookie_list));

                    for(String header:cookie_list){


                        Cookie c = Cookie.parse(httpurl,header);
                        if(c != null){
                            cookies_al.add(c);


                        }

                    }



                    //Log.wtf("wtf","cookie_array_list");

                    //System.out.println(cookies_al);

                    ch.saveFromResponse(httpurl,cookies_al);




                }


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
        return null;

    }




}
