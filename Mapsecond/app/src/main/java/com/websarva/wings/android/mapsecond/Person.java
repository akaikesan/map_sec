package com.websarva.wings.android.mapsecond;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Person implements ClusterItem {

    private double lat,lon;

    private String snip;

    private String username;

    Person(double lat,double lon,String snip, String username){
        this.lat = lat;
        this.lon = lon;
        this.snip = snip;
        this.username = username;
    }

    String getUsername(){
        return username;
    }

    String getComment(){
        return snip;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat,lon);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        if(snip.length() > 10){
            return snip.substring(0,7) + "...";
        }else{
            return snip;
        }
    }
}
