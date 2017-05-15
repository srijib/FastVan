package com.fast.van.activities;

import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;

import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fast.van.R;
import com.fast.van.adapters.PlaceAutocompleteAdapter;
import com.fast.van.utils.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//import com.example.android.common.logger.Log;

/**
 * Created by Amandeep Singh Bagli on 09/10/15.
 */
public class ActivitySearchLocation extends LocationBaseActivity{

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;
    private ImageView crossButton;
    private ImageView searchIconLeft;
    private ImageView searchIconRight;




    static final LatLng TutorialsPoint = new LatLng(21 , 57);

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private GoogleMap googleMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        setContentView(R.layout.fastvan_activity_location_finder);



        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);


        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(TutorialsPoint).title("TutorialsPoint"));
            CameraUpdate update= CameraUpdateFactory.newLatLng(TutorialsPoint);
            googleMap.moveCamera(update);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);


            googleMap.addMarker(new MarkerOptions().
                    position(place.getLatLng()).title(place.getName().toString()));
            CameraUpdate update= CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 8f);
            googleMap.animateCamera(update);

            // Format details of the place for display and show it in a TextView.
            Log.e("", formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()).toString());

            // Display the third party attributions if set.
           // final CharSequence thirdPartyAttribution = places.getAttributions();
           /* if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }*/

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };

    private  Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }


    @Override
    public void onLocationUpdate(Location location) {

    }

    @Override
    public void onGoogleConnectionFailed(ConnectionResult connectionResult) {

    }


    private void moveView( final View from,final View to)
    {
        RelativeLayout root = (RelativeLayout) findViewById( R.id.searchlayout );
      //  ImageView img = (ImageView) findViewById( R.id.searchIconLeft );
    //    DisplayMetrics dm = new DisplayMetrics();
       // this.getWindowManager().getDefaultDisplay().getMetrics(dm);
      //  int statusBarOffset =0;// dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        from.getLocationOnScreen( originalPos );

      //  int xDest = dm.widthPixels/2;

        RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) from.getLayoutParams();
        int xDest = to.getLeft();


        int yDest =to.getBottom()+(to.getMeasuredHeight()/2);


        android.util.Log.e("position",  " xD:" + xDest +  " yD:" + yDest);
        TranslateAnimation anim = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
        anim.setDuration(1000);
        anim.setFillAfter(true);
        from.startAnimation(anim);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                from.setVisibility(View.GONE);
                to.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
   private void init(){

       crossButton=(ImageView)findViewById(R.id.crossIcon);
       searchIconLeft=(ImageView)findViewById(R.id.searchIconLeft);
       searchIconRight=(ImageView)findViewById(R.id.searchIconRight);
       setCrossClickListener();
       setTextChangeListeners();
       setFocusChangeListeners();
    }
    public void setCrossClickListener() {
        crossButton
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mAutocompleteView.setText("");
                    }
                });

         

    }

    public void setFocusChangeListeners() {
        mAutocompleteView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean focus) {
                if (focus && !mAutocompleteView.getText().toString().isEmpty()) {
                    crossButton.setVisibility(View.VISIBLE);
                    searchIconLeft.setVisibility(View.VISIBLE);
                    searchIconRight.setVisibility(View.GONE);
//                    moveView(searchIconRight,searchIconLeft);
                } else {
                    crossButton.setVisibility(View.GONE);
                    searchIconLeft.setVisibility(View.GONE);
                    searchIconRight.setVisibility(View.VISIBLE);
//                    moveView(searchIconLeft,searchIconRight);
                }
            }
        });



    }


    public void setTextChangeListeners() {
        mAutocompleteView.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

                if (s.toString().length() > 0) {

                    crossButton.setVisibility(View.VISIBLE);
                    searchIconLeft.setVisibility(View.VISIBLE);
                    searchIconRight.setVisibility(View.GONE);
                   // moveView(searchIconRight, searchIconLeft);
                } else {

                    crossButton.setVisibility(View.GONE);
                    searchIconLeft.setVisibility(View.GONE);
                    searchIconRight.setVisibility(View.VISIBLE);
//                    moveView(searchIconLeft, searchIconRight);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (s.toString().length() > 0) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });



    }

    @Override
    public void onClickView(View v) {
        int id=v.getId();
//        switch (id){
//        }
    }
}
