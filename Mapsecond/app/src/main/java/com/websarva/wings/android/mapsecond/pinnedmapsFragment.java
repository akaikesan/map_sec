
package com.websarva.wings.android.mapsecond;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.LOCATION_SERVICE;

public class pinnedmapsFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private boolean is_end;

    private GoogleMap mMap;

    private Marker mMarker = null;


    double latitude = 0, longitude = 0;

    boolean GpsStatus;

    int pinnumber = 0;

    LocationManager locationManager;
    private String TAG = "Verbose";

    public void GPSStatus() {
        if(getContext() != null){

            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        }
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        is_end = false;

        super.onCreate(savedInstanceState);

        Context contextOfFragment = getContext();


        GPSStatus();

        if (!GpsStatus) {
            Toast toast = Toast.makeText(contextOfFragment,
                    "turn your location access on  to use our service ", Toast.LENGTH_SHORT);
            toast.show();

            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        //This class automatically initializes the maps system and the view.


    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pinnedmaps, null);


        //ここからコメントアクティビティへの遷移の流れ
        view.findViewById(R.id.nextpinbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context contextOfFragment = getContext();

                if(contextOfFragment !=null) {
                    SharedPreferences data = contextOfFragment.getSharedPreferences("DataSave", Context.MODE_PRIVATE);

                    int maxofthis = data.getInt("MAXOFTHIS", 0);

                    if (pinnumber < maxofthis) {//つまり、pinnumber == maxofthis == 0のときはpinnumber は０のままmaxofthisが１になるとpinnumber = 1になる。
                        pinnumber++;
                    } else if (pinnumber != 0 && pinnumber == maxofthis) {
                        pinnumber = 1;
                    }


                    if (pinnumber != 0) {
                        Log.wtf(TAG, "this PinNumber:" + pinnumber);
                    } else {
                        Log.wtf(TAG, "you haven't registered yet");
                    }

                    latitude = data.getFloat("LAT" + pinnumber, (float) latitude);
                    longitude = data.getFloat("LONG" + pinnumber, (float) longitude);

                    LatLng sydney = new LatLng(latitude, longitude);

                    if (mMarker != null) {
                        mMarker.remove();

                        Log.wtf(TAG, "pin removed");
                    }

                    IconGenerator iconFactory = new IconGenerator(getContext());

                    mMap.getUiSettings().setAllGesturesEnabled(false);

                    mMap.getUiSettings().setCompassEnabled(false);

                    mMap.getUiSettings().setMapToolbarEnabled(false);


                    MarkerOptions markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("PIN " + pinnumber)))
                            .position(sydney)
                            .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
                    mMarker = mMap.addMarker(markerOptions);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(sydney)      // Sets the center of the map to Mountain View
                            .zoom(16)                   // Sets the zoom
                            .bearing(0)                // Sets the orientation of the camera to east
                            .tilt(70)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    Log.wtf(TAG, "pin reloaded");


                    GetComment gc = new GetComment((float) latitude,(float) longitude);

                    gc.setOngcCallBack(new GetComment.gcCallBackTask(){
                        @Override
                        void gcCallBack(String result) {
                            try {

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
                                    IconGenerator iconFactory = new IconGenerator(getContext());
                                    String pintext;
                                    if(value.getString("content").length() > 10){
                                        pintext = value.getString("content").substring(0,10);
                                    }else{
                                        pintext = value.getString("content");
                                    }

                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(pintext)))
                                            .position(new LatLng(value.getDouble("latitude"),value.getDouble("longitude")))
                                            .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
                                    mMarker = mMap.addMarker(markerOptions);


                                }


                                mMap.getUiSettings().setScrollGesturesEnabled(true);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    });

                    gc.execute();


                }
            }
        });


        return view;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment;
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.pinned_map);

        if (mapFragment != null) {

            mapFragment.getMapAsync(this);

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        final Context contextOfFragment = getContext();

        mMap = googleMap;


        mMap.getUiSettings().setAllGesturesEnabled(false);

        mMap.getUiSettings().setCompassEnabled(false);

        mMap.getUiSettings().setMapToolbarEnabled(false);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

            assert contextOfFragment != null;
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(contextOfFragment
                            , R.raw.style_json));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        Log.wtf(TAG, "onMapReady: is started");

        checkPermission_or_request();


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng longpushLocation) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(contextOfFragment);

                // ダイアログの設定
                //アイコン設定
                alertDialog.setTitle("confirmation");      //タイトル設定
                alertDialog.setMessage("pin");  //内容(メッセージ)設定


                // OK(肯定的な)ボタンの設定
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences data = contextOfFragment.getSharedPreferences("DataSave", Context.MODE_PRIVATE);
                        int maxofthis = data.getInt("MAXOFTHIS",0);

                        // OKボタン押下時の処理



                        Log.wtf(TAG,"max number:" + maxofthis);

                        SharedPreferences.Editor editor = data.edit();

                        int new_pinnum;

                        if(maxofthis < 5){
                            editor.putInt("MAXOFTHIS",maxofthis+1);
                        }
                        maxofthis++;
                        //この時点で、 1 <= maxofthis <= 5　
                        //ここからmaxofthisはピンの個数。
                        if(maxofthis <= 5) {
                            editor.putFloat("LAT" + maxofthis, (float) longpushLocation.latitude);
                            editor.putFloat("LONG" + maxofthis, (float) longpushLocation.longitude);
                            new_pinnum = maxofthis;
                        }else if(maxofthis == 6){//maxofthisが6以上のとき
                            editor.putFloat("LAT" + pinnumber, (float) longpushLocation.latitude);
                            editor.putFloat("LONG" + pinnumber, (float) longpushLocation.longitude);

                            new_pinnum = pinnumber;
                        }
                        else{
                            new_pinnum=0;
                        }


                        editor.apply();


                        Toast.makeText(contextOfFragment,"saved new PIN" + new_pinnum,Toast.LENGTH_SHORT).show();




                        Log.wtf("AlertDialog", "Positive which :" + which);
                    }
                });

                // NG(否定的な)ボタンの設定
                alertDialog.setNegativeButton("NG", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // NGボタン押下時の処理
                        Log.wtf("AlertDialog", "Negative which :" + which);
                    }
                });

                // ダイアログの作成と描画
//        alertDialog.create();
                alertDialog.show();
            }
        });





    }

    public void checkPermission_or_request() {

        Context contextOfFragment = getContext();

        assert contextOfFragment != null;
        if (ContextCompat.checkSelfPermission(contextOfFragment,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && getActivity() != null) {
            Log.wtf(TAG, "checkPermission_or_request: you allowed permission ");

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    1000);
        } else {

            Log.wtf(TAG, "checkPermission_or_request: already permission is allowed");

            if(getActivity()!=null){
                locationManager =
                        (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            }

            updates(locationManager);

        }


    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.wtf("debug", "checkSelfPermission true");
                Toast toast = Toast.makeText(getContext(),
                        "位置情報解禁！", Toast.LENGTH_SHORT);
                toast.show();


            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(getActivity(),
                        "これ以上なにもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
            if(getActivity() != null){
                locationManager =
                        (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            }


            updates(locationManager);

        }
    }

    public void updates(LocationManager locationManager) {

        if(getActivity()!= null){

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {


                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        300, 40, this);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        300, 40, this);
                Log.wtf(TAG, "onRequestPermissionsResult:request your location!!");


            }
        }

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        if(!is_end) {

            Context contextOfFragment = getContext();
            if(contextOfFragment != null){


                SharedPreferences data = contextOfFragment.getSharedPreferences("DataSave", Context.MODE_PRIVATE);


                latitude = data.getFloat("LAT1", 400);
                longitude = data.getFloat("LONG1", 400);


                Log.wtf(TAG, "onLocationChanged: ");

                LatLng sydney;

                String pintxt;

                if (latitude == 400 || longitude == 400) {

                    sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    pintxt = "Long press to PIN";
                } else {
                    sydney = new LatLng(latitude, longitude);
                    pintxt = "PIN 1";
                    pinnumber = 1;
                }

                if (mMarker != null) {
                    mMarker.remove();

                    Log.wtf(TAG, "pin removed");
                }

                Activity activity = getActivity();

                IconGenerator iconFactory = new IconGenerator(activity);


                MarkerOptions markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(pintxt)))
                        .position(sydney)
                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
                mMarker = mMap.addMarker(markerOptions);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(sydney)      // Sets the center of the map to Mountain View
                        .zoom(16)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(70)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Log.wtf(TAG, "pin reloaded");


                mMap.getUiSettings().setScrollGesturesEnabled(true);


            }


        }

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onDestroy(){
        super.onDestroy();

        is_end = true;



        Log.wtf("onDestroy in pinned map","pinnedMapsFragment is finished");

    }




}
