package com.fast.van.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Amandeep Singh Bagli on 30/09/15.
 */
public class GoogleMapFragment  extends SupportMapFragment {
//Delta--InfoTyme, EchoGroup
    private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";

    public interface OnGoogleMapFragmentListener {
        void onMapReady(GoogleMap map);
    }

    public static GoogleMapFragment newInstance(OnGoogleMapFragmentListener callback) {
        GoogleMapFragment mapFragment=new GoogleMapFragment();
       mapFragment.mCallback=callback;
        return mapFragment;
    }

    public static GoogleMapFragment newInstance(GoogleMapOptions options) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(SUPPORT_MAP_BUNDLE_KEY, options);

        GoogleMapFragment fragment = new GoogleMapFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mCallback = (OnGoogleMapFragmentListener) getActivity();
//        } catch (ClassCastException e) {
//            throw new ClassCastException(getActivity().getClass().getName() + " must implement OnGoogleMapFragmentListener");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mCallback != null) {
            mCallback.onMapReady(getMap());
        }

        // Get the button view
        View locationButton = ((View) view.findViewById(1).getParent()).findViewById(2);

        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
         rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);


        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);



        rlp.setMargins(0, 0, 30, 30);



        return view;
    }



    private OnGoogleMapFragmentListener mCallback;
}