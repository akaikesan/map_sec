package com.websarva.wings.android.mapsecond;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class GetCookiesToSet {

    private String cookie_in_request = "";


    public GetCookiesToSet(String scheme, String host, int port, String path){

        HttpUrl httpurl = new HttpUrl.Builder().scheme(scheme).host(host).port(port).addPathSegment(path).build();

        List<Cookie> cookies = new WebviewCookieHandler().loadForRequest(httpurl);

        boolean x=true;


        for (Cookie cookie : cookies) {



            String[] tmp = cookie.toString().split(";");


            if(x){
                cookie_in_request += tmp[0];

                x=false;

            }
            else{

                cookie_in_request += ";" + tmp[0];

            }


        }

    }

    public String getStringCookie(){
        return cookie_in_request;
    }
}
