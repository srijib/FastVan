package com.fast.van.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.van.R;
import com.fast.van.activities.ActivityHome;
import com.fast.van.utils.Log;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Amandeep Singh Bagli on 23/10/15.
 */
public class FragmentNoOrder extends BaseFragment  implements GoogleMapFragment.OnGoogleMapFragmentListener,LocationListener {

    private static View view;
    // Google Map
    private GoogleMap googleMap;
    private Marker myLocationMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fastvan_fragment_no_order, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        try {
            ActivityHome activityHome= (ActivityHome) getActivity();
            activityHome.setLocationListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, GoogleMapFragment.newInstance(this), "googlemap");
        fragmentTransaction.commit();

    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap=map;
        if(googleMap!=null){
            googleMap.setMyLocationEnabled(true);
            Log.d("googlemap", "onMapReady initialized :)");

/*
            LatLng MELBOURNE = new LatLng(-37.813, 144.962);
            Marker melbourne = googleMap.addMarker(new MarkerOptions()
                    .position(MELBOURNE)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_van_icon)));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(MELBOURNE, 10);
          //  map.animateCamera(cameraUpdate);
            map.moveCamera(cameraUpdate);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);*/


        }else{
            Log.d("googlemap","onMapReady error :(");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(googleMap!=null){
            Log.d("googlemap", "onMapReady initialized :)");
          //  googleMap.clear();

            LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
            if(myLocationMarker==null)
            myLocationMarker = googleMap.addMarker(new MarkerOptions()
                    .position(mylocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_van_icon)));
            else
                myLocationMarker.setPosition(mylocation);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mylocation, 13f);
            //  map.animateCamera(cameraUpdate);
            googleMap.moveCamera(cameraUpdate);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);


        }else{
            Log.d("googlemap","onMapReady error :(");
        }
    }

    @Override
    public void onClickView(View v) {

    }
}
