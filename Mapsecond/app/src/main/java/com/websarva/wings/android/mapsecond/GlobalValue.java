package com.websarva.wings.android.mapsecond;

public class GlobalValue {
    private static final String SCHEME = "http";
    private static final String HOST = "localhost";
    private static final int PORT = 8000;
    private static final String PATH = "accounts";
    private static final String CRYPT = "abcdefg098765432";

    public static String getScheme(){
        return SCHEME;
    }

    public static String getHost(){
        return HOST;
    }

    public static int getPort(){
        return PORT;
    }

    public static String getPath(){
        return PATH;
    }

    public static String getCryptKey(){
        return CRYPT;
    }

}
