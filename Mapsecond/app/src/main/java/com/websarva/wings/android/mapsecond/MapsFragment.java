package com.websarva.wings.android.mapsecond;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;

    private Marker mMarker = null;

    private boolean is_end;

    ClusterManager<Person> mClusterManager;


    double latitude = 0, longitude = 0;

    boolean GpsStatus;



    LocationManager locationManager;
    private String TAG = "Verbose";

    public void GPSStatus() {
        if(getContext() != null){
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, null);


        view.findViewById(R.id.btToComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), CommentActivity.class);

                i.putExtra("LAT",(float)latitude);

                i.putExtra("LON",(float)longitude);

                startActivity(i);
            }
        });

        return view;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

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

        Context contextOfFragment = getContext();

        mMap = googleMap;
        if(contextOfFragment != null){
            mClusterManager = new ClusterManager<>(contextOfFragment,mMap);
        }

        mMap.getUiSettings().setAllGesturesEnabled(false);

        mMap.getUiSettings().setCompassEnabled(false);

        mMap.getUiSettings().setMapToolbarEnabled(false);


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = false;
            if (contextOfFragment != null) {
                success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(contextOfFragment
                                , R.raw.style_json));
            }
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        Log.wtf(TAG, "onMapReady: is started");

        checkPermission_or_request();


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


            if(getActivity() != null){

                locationManager =
                        (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                updates(locationManager);
            }





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

                updates(locationManager);
            }


        }
    }

    public void updates(LocationManager locationManager) {
        Activity activityOfFragment = getActivity();
        Context contextOfFragment = getContext();

        //ここの==が！＝になっていたため、うまくいかなかった。現在位置情報を取得に成功！　私は賢くなっている！！！！！！！

        assert contextOfFragment != null;
        assert activityOfFragment != null;
        if (ActivityCompat.checkSelfPermission(contextOfFragment,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activityOfFragment, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    300, 40, this);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    300, 40, this);
            Log.wtf(TAG, "onRequestPermissionsResult:request your location!!");


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



            latitude = location.getLatitude();

            longitude = location.getLongitude();

            LatLng sydney = new LatLng(latitude, longitude);

            if (mMarker != null) {
                mMarker.remove();

                Log.v("MapsF.onLocationchanged", "pin removed");
            } else {
                Log.v("MapsF.onLocationchanged", "pin is null");
            }

            DisplayComment dc = new DisplayComment(latitude,longitude,mMap,getContext(),mClusterManager);
            dc.display();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(sydney)      // Sets the center of the map to Mountain View
                    .zoom(16)                   // Sets the zoom
                    .bearing(0)               // Sets the orientation of the camera to east
                    .tilt(70)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            Log.v(TAG, "pin reloaded");



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

        Log.v("onDestroy","MapsFragment is finished");

    }





}
