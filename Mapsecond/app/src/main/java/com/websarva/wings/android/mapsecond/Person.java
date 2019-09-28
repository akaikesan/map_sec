package com.websarva.wings.android.mapsecond;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Person implements ClusterItem {

    private double lat,lon;

    private String snip;

    Person(double lat,double lon,String snip){
        this.lat = lat;
        this.lon = lon;
        this.snip = snip;

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
        return snip;
    }
}
