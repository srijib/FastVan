package com.fast.van.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fast.van.R;

/**
 * Created by Amandeep Singh Bagli on 24/12/15.
 */
public class ActivityOrderStateNotification extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_custom_msg);
        TextView textView= (TextView) findViewById(R.id.textMessage);
        String msg="Order status has been changed.";
        if(getIntent().hasExtra("message")){
            msg=getIntent().getStringExtra("message");
        }
        textView.setText(msg);
        findViewById(R.id.btnOk).setOnClickListener(this);
    }

    @Override
    public void onClickView(View view) {
finish();
    }
}
