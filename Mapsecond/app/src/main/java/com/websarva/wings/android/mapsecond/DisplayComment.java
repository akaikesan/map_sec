package com.websarva.wings.android.mapsecond;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

class DisplayComment implements ClusterManager.OnClusterClickListener<Person>,ClusterManager.OnClusterItemClickListener<Person>{


    private ClusterManager<Person> mClusterManager;

    private double latitude, longitude;

    private GoogleMap mMap;

    private Context context;

    static Cluster<Person> mCluster;

    DisplayComment(double latitude, double longitude, GoogleMap mMap, Context context){
        this.latitude = latitude;
        this.longitude = longitude;
        this.mMap = mMap;
        this.context = context;
        mClusterManager = new ClusterManager<>(context,mMap);
    }



    void display(){

        mMap.getUiSettings().setAllGesturesEnabled(false);

        mMap.getUiSettings().setCompassEnabled(false);

        mMap.getUiSettings().setMapToolbarEnabled(false);

        GetComment gc = new GetComment((float) latitude,(float) longitude);

        mClusterManager.setOnClusterClickListener(DisplayComment.this);
        mClusterManager.setOnClusterItemClickListener(DisplayComment.this);
        mClusterManager.setRenderer(new PersonRenderer(context,mMap,mClusterManager));

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        gc.setOnCallBack(new GetComment.gcCallBackTask(){
            @Override
            void gcCallBack(String result) {

                System.out.println(result);
                try {
                    if(getContext() != null){


                        JSONObject json = new JSONObject(result);

                        if (json.isNull("comment0")) {
                            Log.d("pinnedmapsFragment","JSON has NOTHING");
                            return;
                        }


                        JSONArray key = json.names ();
                        for (int i = 0; i < key.length (); ++i) {
                            String keys = key.getString (i);
                            JSONObject value = json.getJSONObject(keys);
                            // do something with jsonObject here


                            mClusterManager.addItem(new Person(
                                    value.getDouble("latitude"),
                                    value.getDouble("longitude"),
                                    value.getString("content"),
                                    value.getString("username"),
                                    value.getInt("fav")));

                        }


                        mClusterManager.cluster();

                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });



        gc.execute();



    }

    private class PersonRenderer extends DefaultClusterRenderer<Person> {

        private final IconGenerator mIconGenerator = new IconGenerator(context);
        private final IconGenerator mClusterIconGenerator = new IconGenerator(context);

        PersonRenderer(Context context, GoogleMap map, ClusterManager<Person> clusterManager) {
            super(context, map, clusterManager);


        }

        @Override
        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {

            markerOptions
                    .icon(BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(person.getSnippet())))
                    .position(person.getPosition())
                    .anchor(mIconGenerator.getAnchorU(), mClusterIconGenerator.getAnchorV());
        }




    }

    @Override
    public boolean onClusterItemClick(Person item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Intent intent = new Intent(context,CommentFocusActivity.class);

        intent.putExtra("CONTENT", item.getComment());


        intent.putExtra("USERNAME",item.getUsername());

        intent.putExtra("FAV",item.getFav());



        Log.i("OnClusteritemClick","ClusterItem was clicked");

        context.startActivity(intent);
        return false;

    }


    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        Log.wtf("OnClusterClick","Cluster was clicked");

        Intent intent = new Intent(context, CommentListActivity.class);

        mCluster = cluster;

        context.startActivity(intent);

        return true;
    }


}
