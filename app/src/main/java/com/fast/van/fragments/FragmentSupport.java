package com.fast.van.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fast.van.R;
import com.fast.van.models.login.Login;
import com.fast.van.testing.TestActivity;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 05/10/15.
 */
public class FragmentSupport extends BaseFragment {
    private String phoneNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fastvan_fragement_support, container, false);
        view.findViewById(R.id.llCallUs).setOnClickListener(this);
        view.findViewById(R.id.llSmsUs).setOnClickListener(this);
        view.findViewById(R.id.llMailUs).setOnClickListener(this);
        view.findViewById(R.id.ibCallUs).setOnClickListener(this);
        view.findViewById(R.id.ibSmsUs).setOnClickListener(this);
        view.findViewById(R.id.ibMailUs).setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Login user = CommonData.getSessionData(getActivity());
        if(user!=null)
            phoneNumber=user.getAdminDataPhone();
    }

    @Override
    public void onClickView(View v) {


        int id = v.getId();
//        Intent intent;
        switch (id) {
            case R.id.llCallUs:
            case R.id.ibCallUs:
                if (phoneNumber != null)
                    BaseUtils.makeCall(phoneNumber, activity);
                //    BaseUtils.makeCall("0114350637", activity);

                break;
            case R.id.llSmsUs:
            case R.id.ibSmsUs:

                break;
            case R.id.llMailUs:
            case R.id.ibMailUs:

                onShareClick();
                break;
        }
//        Toast.makeText(activity, "Support Under Construction", Toast.LENGTH_LONG).show();

   /*     int id=v.getId();
        Intent intent;
        switch (id){
            case R.id.llCallUs:
            case R.id.ibCallUs:
                  intent=  new Intent(activity, TestActivity.class);
                intent.putExtra("caller","CALLUS");
                startActivity(intent);
                break;
            case R.id.llSmsUs:
            case R.id.ibSmsUs:
                  intent=  new Intent(activity, TestActivity.class);
                intent.putExtra("caller","SMSUS");
                startActivity(intent);
                break;
            case R.id.llMailUs:
            case R.id.ibMailUs:
                intent=new   Intent(activity, TestActivity.class);
                intent.putExtra("caller","MAILUS");
                startActivity(intent);
                break;
        }*/
    }


    private void onShareClick() {
        Resources resources = getResources();

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FastVan Support");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@fastvan.com"});
        emailIntent.setType("message/rfc822");

        PackageManager pm = activity.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");

        Intent openInChooser = Intent.createChooser(emailIntent, "fastvan support");

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
//            if(packageName.contains("android.email")) {
//                emailIntent.setPackage(packageName);
//            }
//            else
            if (packageName.contains("mms") || packageName.contains("android.email")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@fastvan.com"});
//                intent.setType("text/plain");
                intent.setType("message/rfc822");


                if (packageName.contains("android.email")) {
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "fastvan support");
                }

                if (packageName.contains("email")) {
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                }
                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }
}
