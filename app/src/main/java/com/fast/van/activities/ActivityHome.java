package com.fast.van.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fast.van.R;
import com.fast.van.adapters.LeftNavMenuAdapter;
import com.fast.van.adapters.RightNavMenuAdapter;
import com.fast.van.callbacks.NavigationLeftDrawerCallbacks;
import com.fast.van.callbacks.NavigationRightDrawerCallbacks;
import com.fast.van.common.CustomClickListener;
import com.fast.van.common.SessionManager;
import com.fast.van.config.Constants;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.dialogs.ConfirmationDialog;
import com.fast.van.dialogs.ConfirmationDialogCodes;
import com.fast.van.dialogs.ConfirmationDialogEventsListener;
import com.fast.van.fragments.FragmentDelivery;
import com.fast.van.fragments.FragmentEstimate;
import com.fast.van.fragments.FragmentMyBooking;
import com.fast.van.fragments.FragmentNewRequest;
import com.fast.van.fragments.FragmentNoOrder;
import com.fast.van.fragments.FragmentOrder;
import com.fast.van.fragments.FragmentPayment;
import com.fast.van.fragments.FragmentSupport;
import com.fast.van.fragments.FragmentTodaysBookings;
import com.fast.van.fragments.GoogleMapFragment;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.menu.NavigationItem;
import com.fast.van.models.notifications.Datum;
import com.fast.van.models.notifications.Notification;
import com.fast.van.models.order.Order;
import com.fast.van.models.order.OrderDetails;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;
import com.fast.van.utils.file.AppFileUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Amandeep Singh Bagli on 28/09/15.
 */
public class ActivityHome extends LocationBaseActivity implements NavigationLeftDrawerCallbacks,
        NavigationRightDrawerCallbacks, GoogleMapFragment.OnGoogleMapFragmentListener, ConfirmationDialogEventsListener {

    LocationListener locationListener;
    private DrawerLayout mDrawerLayout;
    // Google Map
    private GoogleMap googleMap;
    private TextView title;
    private TextView nameTextView;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedindex = savedInstanceState.getInt("index", -1);
            getIntent().putExtras(savedInstanceState);

        }
        setContentView(R.layout.fastvan_activity_home);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        final ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
//        ab.setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        NavigationView navigationViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        nameTextView = (TextView) findViewById(R.id.txtUsername);
        mImageView = (ImageView) findViewById(R.id.imgAvatar);
        init();

        if (navigationView != null) {
            // setupDrawerContent(navigationView);
            Login user = CommonData.getSessionData(this);
            if (user != null && user.getData() != null) {
                nameTextView = (TextView) findViewById(R.id.txtUsername);
                nameTextView.setText(user.getData().getFullName());
                if (user.getData() != null)
                    Glide.with(this)
                            .load(user.getData().getDriverImageUrl())
                            .fitCenter()
                            .placeholder(R.drawable.icon_avatar_big)
                            .into(mImageView);
            }

            RecyclerView mDrawerList = (RecyclerView) findViewById(R.id.drawerList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mDrawerList.setLayoutManager(layoutManager);
            mDrawerList.setHasFixedSize(true);

//            final List<NavigationItem> navigationItems = getMenu();
//            NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(navigationItems);
//            adapter.setNavigationDrawerCallbacks(this);
            ArrayList<String> list = new ArrayList<String>();
            list.add("Home");
            list.add("My Bookings");
//            list.add("My Payments");
//            list.add("Fare Estimator");
            list.add("Support");
            list.add("Logout");

            LeftNavMenuAdapter adapter1 = new LeftNavMenuAdapter(this, list);

            mDrawerList.setAdapter(adapter1);

            mImageView.setOnClickListener(this);

//            homeScreen();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
//                message = extras.getString("message");
                String orderId = extras.getString("orderId");
                Log.e(TAG, extras.toString() + "OrderID: " + orderId);
                if (orderId != null && !orderId.isEmpty()) {


                    onNavigationLeftDrawerItemSelected(selectedindex != -1 ? selectedindex : 5);
                } else
                    onNavigationLeftDrawerItemSelected(selectedindex != -1 ? selectedindex : 0);
            } else
                onNavigationLeftDrawerItemSelected(selectedindex != -1 ? selectedindex : 0);
        }

    }

    @Override
    public void onLocationUpdate(Location location) {

        if (locationListener != null) {
            locationListener.onLocationChanged(location);
        }
    }

    @Override
    public void onGoogleConnectionFailed(ConnectionResult connectionResult) {

    }


    InputMethodManager imm;

    private void init() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        title = (TextView) findViewById(R.id.screenTitle);
        findViewById(R.id.backbutton).setOnClickListener(this);
        findViewById(R.id.textViewClearNotifications).setOnClickListener(this);
        //findViewById(R.id.notificationbutton).setOnClickListener(this);
        // findViewById(R.id.textviewNotificationCount).setOnClickListener(this);
        findViewById(R.id.llNotifications).setOnClickListener(this);
        LinearLayout ll = (LinearLayout) findViewById(R.id.llNotifications);
        CustomClickListener cc = new CustomClickListener(findViewById(R.id.textviewNotificationCount));
        ll.setOnTouchListener(cc);
        getNofifications(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onNavigationLeftDrawerItemSelected(int position) {
        if (position < 6)
            selectedindex = position;

        Fragment fragment = null;
        String tag = null;
        switch (position) {
            case 0:
                fragment = new FragmentTodaysBookings();
                //getTodaysMyOrder();
                title.setText("Home");
                tag = "noorder";
                break;
            case 1:
                title.setText("My Bookings");
                fragment = new FragmentMyBooking();
                tag = "mybookings";
                break;
//            case 2:
//                title.setText("My Payments");
//                fragment = new FragmentPayment();
//                tag = "payments";
//                break;

        /*    case 3:
                title.setText("Fare Estimator");
                fragment=new FragmentEstimate();
                tag="newRequest";
                break;*/
            case 2:
                title.setText("Support");
                fragment = new FragmentSupport();
                tag = "support";
                break;
            case 3:
                ConfirmationDialog.WithActivity(this).show("Are you sure, you want to log out?", "Yes", "No", ConfirmationDialogCodes.LogOut);
//                Intent intent=new Intent(activity,ActivityNewRequest.class);
//                startActivity(intent);

//                title.setText("Logout");
//                fragment=new FragmentNewRequest();
//                tag="support";
                break;
            case 4:
                fragment = new FragmentOrder();
                title.setText("Home");
                tag = "order";
                break;
            case 5:
                title.setText("Home");
                fragment = new FragmentNewRequest();
                fragment.setArguments(getIntent().getExtras());
                tag = "Home";
                break;
            default:
                Toast.makeText(activity, "under construction", Toast.LENGTH_LONG).show();
                break;


        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.container, fragment, tag);
            fragmentTransaction.commit();
        }
        if (mDrawerLayout != null)
            mDrawerLayout.closeDrawers();
    }

    @Override
    public void onClickView(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backbutton:
                try {
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                if (mDrawerLayout != null)
                    mDrawerLayout.openDrawer(GravityCompat.START);

                Login user = CommonData.getSessionData(this);

                if (user != null && user.getData() != null) {

                    nameTextView.setText(user.getData().getFullName());

                    Glide.with(this)
                            .load(user.getData().getDriverImageUrl())
                            .asBitmap()
                            .toBytes()
                            .centerCrop()
                            .into(new SimpleTarget<byte[]>(200, 200) {
                                @Override
                                public void onResourceReady(byte[] data, GlideAnimation anim) {
                                    // Post your bytes to a background thread and upload them here.
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    mImageView.setImageBitmap(bitmap);

                                }
                            });

                  /*  if (user.getData() != null)
                        Glide.with(this)
                                .load(user.getData().getDriverImageUrl())
                                .fitCenter()        .placeholder(R.drawable.icon_avatar_big).thumbnail(0.1f)
                                .into(mImageView) ;*/
                    //  .preload(mImageView.getMeasuredWidth(),mImageView.getMeasuredHeight())
                }

                break;
            case R.id.textViewClearNotifications:
                clearNotification();
                break;
            case R.id.notificationbutton:
            case R.id.textviewNotificationCount:
            case R.id.llNotifications:
                try {
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                //  mDrawerLayout.openDrawer(GravityCompat.END);
                getNofifications(false);
                break;
            case R.id.imgAvatar:
            /*    Intent intent = new Intent(activity, ActivityDriverEditProfile.class);
                startActivity(intent);
                Transformer.rightToLeft(activity);
                mDrawerLayout.closeDrawers();*/
                break;

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("Map", "main in main initialized");
    }

    @Override
    public void onNavigationRightDrawerItemSelected(int position, Datum data) {
        if (mDrawerLayout != null)
            mDrawerLayout.closeDrawers();
//        Toast.makeText(activity, "under construction", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(activity, ActivityNewRequest.class);
        intent.putExtra("message", "Notification new Booking");
        intent.putExtra("orderId", "" + data.getOrderId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void OnOkButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {
        switch (confirmationDialogCode) {
            case LogOut:
                logoutFromServer();
                break;
        }

    }

    @Override
    public void OnCancelButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {

    }


    private int selectedindex = -1;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", selectedindex);
        if (getIntent().getExtras() != null)
            outState.putAll(getIntent().getExtras());
        super.onSaveInstanceState(outState);

    }

    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }


    public void getTodaysMyOrder() {

        Login user = CommonData.getSessionData(activity);
        Log.e(TAG, "getOrder:LOADING...");
        if (user != null && !user.getAccessToken().isEmpty()) {
            Log.e(TAG, "getAccessToken:" + user.getAccessToken());
            LoadingBox.showLoadingDialog(activity, "Loading...");
            RestClient.getApiServiceForPojo().getTodayBooking(user.getAccessToken(), new Callback<Order>() {
                @Override
                public void success(Order order, Response response) {
                    Log.e(TAG, "getDriverInfo:NETWORK:" + order.toString());

//                    SessionManager.instance.addSpecificLastRequested(activity, Constants.DataRequest.ORDERS, Calendar.getInstance());

                    OrderDetails orderDetails = order.getData();
                    if (orderDetails != null)
                        initOrder(orderDetails);
                    LoadingBox.dismissLoadingDialog();
                }

                @Override
                public void failure(RetrofitError error) {
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);
                    initOrder(null);
                }
            });

        }
    }

    private void initOrder(OrderDetails orderDetails) {

        String tag = null;
        Fragment fragment = null;
        if (orderDetails != null) {
            tag = "order";
            fragment = FragmentOrder.newInstance(orderDetails);
        } else {
            tag = "noorder";
            fragment = new FragmentNoOrder();
        }


        try {
            title.setText("Home");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.container, fragment, tag);
            fragmentTransaction.commit();
        } catch (Exception e) {

        }

    }

    private void getNofifications(final boolean isBackground) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

        if (!isBackground)
            LoadingBox.showLoadingDialog(activity, "Loading...");
        String json = "";

        final Gson gson = new Gson();
        if (!SessionManager.instance.lastUpdatedExpired(activity, Constants.DataRequest.NOTIFICATIONS)) {
            Log.e(TAG, "data from file");
            json = AppFileUtils.readDataFile(Constants.DataRequest.NOTIFICATIONS + ".json", activity);
            Log.e("o", "data:" + json);
            Notification notifications = gson.fromJson(json, Notification.class);
            if (notifications != null)
                ((TextView) findViewById(R.id.textviewNotificationCount)).setText(notifications.getData().size() < 10 ? "" + notifications.getData().size() : "9+");
//            initlist(order);
            if (notifications != null && !isBackground) {

                LoadingBox.dismissLoadingDialog();
                initNotifcationMenu(notifications);
            }

        }

        if (BaseUtils.isNullOrEmpty(json)) {
            Login user = CommonData.getSessionData(activity);
            Log.e(TAG, "getOrder:LOADING...");
            if (user != null && !user.getAccessToken().isEmpty()) {
                Log.e(TAG, "getAccessToken:" + user.getAccessToken());
                //QW1hbk1vbiBPY3QgMTIgMjAxNSAwNjozNTo1MSBHTVQrMDAwMCAoVVRDKQ==
                RestClient.getApiServiceForPojo().getNotification(user.getAccessToken(), new Callback<Notification>() {
                    @Override
                    public void success(Notification notification, Response response) {
                        Log.e(TAG, "getDriverInfo:NETWORK:" + notification.toString());
                        AppFileUtils.saveDataFile(Constants.DataRequest.NOTIFICATIONS + ".json", gson.toJson(notification), activity);
                        SessionManager.instance.addSpecificLastRequested(activity, Constants.DataRequest.NOTIFICATIONS, Calendar.getInstance());
                        if (notification != null)
                            ((TextView) findViewById(R.id.textviewNotificationCount)).setText(notification.getData().size() < 10 ? "" + notification.getData().size() : "9+");

                        if (notification != null && !isBackground) {
                            LoadingBox.dismissLoadingDialog();
                            initNotifcationMenu(notification);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                      /*  ErrorCodes.checkCode(activity, error);
                        if (adapter != null && mValues != null) {
                            mValues.clear();
                            adapter.notifyDataSetChanged();
                        }

                        noRecord.setVisibility(View.VISIBLE);*/

                        ((TextView) findViewById(R.id.textviewNotificationCount)).setText("0");
                        if (!isBackground) {
                            LoadingBox.dismissLoadingDialog();
                            initNotifcationMenu(null);
                        }
                    }
                });

            }

        }

    }

    private void initNotifcationMenu(Notification notification) {

        NavigationView navigationViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        RecyclerView mDrawerList = (RecyclerView) findViewById(R.id.drawerListRight);
        // TextView noRecordFound=(TextView)findViewById(R.id.noRecord);
        if (navigationViewRight != null && notification != null && notification.getData().size() > 0) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);
            //noRecordFound.setVisibility(View.GONE);
            // mDrawerList.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mDrawerList.setLayoutManager(layoutManager);
            mDrawerList.setHasFixedSize(true);

          /*  ArrayList<String> list = new ArrayList<>();
            list.add("My Bookings");
            list.add("Refer & Earn");
            list.add("Promotion");
            list.add("Support");
            list.add("FAQ");
            list.add("About");
            list.add("Logout");*/

            RightNavMenuAdapter adapter1 = new RightNavMenuAdapter(this, notification.getData());

            mDrawerList.setAdapter(adapter1);

            ((TextView) findViewById(R.id.textviewNotificationCount)).setText(notification.getData().size() < 10 ? "" + notification.getData().size() : "9+");

            mDrawerLayout.openDrawer(GravityCompat.END);

        } else {
            CommonDialog.With(activity).show("There is no new notification");
            //   noRecordFound.setVisibility(View.VISIBLE);
            //   mDrawerList.setVisibility(View.GONE);
        }


    }

    private boolean onCreatedNotCalled;

    @Override
    public void onResume() {
        super.onResume();


    }

    private void clearNotification() {

        if (mDrawerLayout != null)
            mDrawerLayout.closeDrawers();
        LoadingBox.showLoadingDialog(activity, "Loading...");
        Login user = CommonData.getSessionData(activity);
        Log.e(TAG, "getOrder:LOADING...");
        if (user != null && !user.getAccessToken().isEmpty()) {
            Log.e(TAG, "getAccessToken:" + user.getAccessToken());
            //QW1hbk1vbiBPY3QgMTIgMjAxNSAwNjozNTo1MSBHTVQrMDAwMCAoVVRDKQ==
            RestClient.getApiServiceForPojo().clearNotification(user.getAccessToken(), new Callback<ServiceReply>() {
                @Override
                public void success(ServiceReply notification, Response response) {
                    Log.e(TAG, "clearNotification:NETWORK:" + notification.toString());
                    AppFileUtils.saveDataFile(Constants.DataRequest.NOTIFICATIONS + ".json", "", activity);
                    SessionManager.instance.addSpecificLastRequested(activity, Constants.DataRequest.NOTIFICATIONS, Calendar.getInstance());

                    LoadingBox.dismissLoadingDialog();

                    ((TextView) findViewById(R.id.textviewNotificationCount)).setText("0");
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
                }

                @Override
                public void failure(RetrofitError error) {


                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);
                    ((TextView) findViewById(R.id.textviewNotificationCount)).setText("0");
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

                }
            });
        }
    }

    private void logoutFromServer() {
        LoadingBox.showLoadingDialog(activity, "Loading...");
        Login user = CommonData.getSessionData(activity);
        Log.e(TAG, "logoutFromServer:LOADING...");
        if (user != null && !user.getAccessToken().isEmpty()) {
            Log.e(TAG, "getAccessToken:" + user.getAccessToken());
            //QW1hbk1vbiBPY3QgMTIgMjAxNSAwNjozNTo1MSBHTVQrMDAwMCAoVVRDKQ==
            RestClient.getApiServiceForPojo().driverLogout(user.getAccessToken(), new Callback<ServiceReply>() {
                @Override
                public void success(ServiceReply notification, Response response) {
                    Log.e(TAG, "logoutFromServer:NETWORK:" + notification.toString());

                    LoadingBox.dismissLoadingDialog();
                    Intent intent = new Intent(activity, ActivitySignUpSignIn.class);
                    startActivity(intent);

                    CommonData.logout(activity);

                    BaseUtils.removeNotification(activity);
                    AppFileUtils.saveDataFile(Constants.DataRequest.NOTIFICATIONS + ".json", "", activity);
                    AppFileUtils.saveDataFile(Constants.DataRequest.ORDERS + ".json", "", activity);

                    finish();


                }

                @Override
                public void failure(RetrofitError error) {


                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);


                }
            });
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    /*    if (onCreatedNotCalled && selectedindex == 0)
            onNavigationLeftDrawerItemSelected(0);
        onCreatedNotCalled = true;*/
    }
}
