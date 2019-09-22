package com.websarva.wings.android.mapsecond;

class GlobalValue {
    private static final String SCHEME = "http";
    private static final String HOST = "localhost";
    private static final int PORT = 8000;
    private static final String PATH = "accounts";

    static String getScheme(){
        return SCHEME;
    }

    static String getHost(){
        return HOST;
    }

    static int getPort(){
        return PORT;
    }

    static String getPath(){
        return PATH;
    }

}
