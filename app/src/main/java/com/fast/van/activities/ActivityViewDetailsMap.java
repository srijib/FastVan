package com.fast.van.activities;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.fragments.GoogleMapFragment;
import com.fast.van.models.order.DropLocationDetail;
import com.fast.van.models.order.PickupLocation;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Amandeep Singh Bagli on 24/11/15.
 */
public class ActivityViewDetailsMap extends LocationBaseActivity {
    public static final String TITLEKEY="screenname";
    public static final String SCREENDATA="screendata";
    public static final String ISPICKUP="ispickuplocation";

private PickupLocation pickupLocation;
private DropLocationDetail dropLocationDetail;
    private String title;
    private GoogleMap googleMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.fastvan_activity_viewdetails_maps);
        findViewById(R.id.buttonOK).setOnClickListener(this);
        findViewById(R.id.backbutton).setOnClickListener(this);
    }
private Marker mylocation;
    @Override
    public void onLocationUpdate(Location location) {
        if (googleMap != null) {
            if (mylocation == null) {
                LatLng latLng=    new LatLng(location.getLatitude(), location.getLongitude());
                mylocation = googleMap.addMarker(new MarkerOptions().
                        position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));

               // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13f));
            } else
                mylocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));

        }
    }

    @Override
    public void onGoogleConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            if (googleMap == null) {
                googleMap = ((GoogleMapFragment) getSupportFragmentManager().
                        findFragmentById(R.id.map)).getMap();



            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            googleMap.setMyLocationEnabled(true);
//            Marker TP = googleMap.addMarker(new MarkerOptions().
//                    position(TutorialsPoint).title("TutorialsPoint"));
//            CameraUpdate update = CameraUpdateFactory.newLatLng(TutorialsPoint);
            //  googleMap.moveCamera(update);
        } catch (Exception e) {
         Log.e(TAG, "googleMap:Exception:", e);
        }



   Bundle extra=getIntent().getExtras();
        TextView textView=(TextView)findViewById(R.id.screenTitle);
        title="Location";
        textView.setText(title);
        if(extra!=null){
            title=extra.getString(TITLEKEY,title);
            textView.setText(title);

            if(extra.getBoolean(ISPICKUP)){
                pickupLocation= BaseUtils.getOBJFromJSON(extra.getString(SCREENDATA), PickupLocation.class);
            }else{
                dropLocationDetail= BaseUtils.getOBJFromJSON(extra.getString(SCREENDATA), DropLocationDetail.class);
            }


        }
        init();

    }
    private void init(){
TextView titleAddress=(TextView)findViewById(R.id.addressTitle);
        titleAddress.setText(title+":");
TextView addressTextView=(TextView)findViewById(R.id.address);





        String addressString=null;
        if (pickupLocation != null) {

            addressString=pickupLocation.getFullAddress();

            if(pickupLocation.getDetails()!=null&&!pickupLocation.getDetails().isEmpty()){
                addressString=addressString+" ("+pickupLocation.getDetails()+").";
            }




            targetLocation(pickupLocation.getLatitude(), pickupLocation.getLongitude());
        }
        if (dropLocationDetail != null) {

            addressString=dropLocationDetail.getFullAddress();

            if(dropLocationDetail.getDetails()!=null&&!dropLocationDetail.getDetails().isEmpty()){
                addressString=addressString+" ("+dropLocationDetail.getDetails()+").";
            }

            targetLocation(dropLocationDetail.getLatitude(), dropLocationDetail.getLongitude());
        }
        addressTextView.setText(addressString);

    }

    private void targetLocation(double latitude,double longitude){


        if(googleMap==null)
            return;
        LatLng latLng=new LatLng(latitude,longitude);
        googleMap.addMarker(new MarkerOptions().
                position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_pin)));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 13f);
        googleMap.animateCamera(update);
    }

    @Override
    public void onClickView(View view) {
switch (view.getId()){
    case R.id.buttonOK:
    case R.id.backbutton:
        onBackPressed();
        break;

}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Transformer.leftToRight(this);
    }
}
