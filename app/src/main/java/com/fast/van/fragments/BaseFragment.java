package com.fast.van.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.fast.van.callbacks.FastVanClickListener;
import com.fast.van.utils.Log;


/**
 * Created by Amandeep Singh Bagli on 30/09/15.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener,FastVanClickListener{

    protected Activity activity;
    protected String TAG=getClass().getCanonicalName();
    private Handler handler;
    private Runnable runnable;
    private boolean canClick;
    protected InputMethodManager imm;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    activity=getActivity();
        canClick=true;
        handler=new Handler();
        runnable =new Runnable() {
            @Override
            public void run() {
                canClick=true;
            }
        };
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        if(canClick){
            canClick=false;
            onClickView(v);
            handler.postDelayed(runnable,1500);

        }else{
            Log.e(TAG, "Click Not allowed at the moment");
        }
    }
}
