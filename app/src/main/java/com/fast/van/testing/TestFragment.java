package com.fast.van.testing;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.fast.van.R;

public final class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    int anIntexColor=Color.YELLOW;

    public static TestFragment newInstance(String content,int index) {
        TestFragment fragment = new TestFragment(getColors(index));

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append(content).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();

        return fragment;
    }
    public TestFragment(int anIntex){
        this.anIntexColor=anIntex;

    }
    private String mContent = "???";
static int  getColors(int i){
    switch (i){
        case 0:
            return Color.GRAY;
        case 1:
            return Color.RED;
        case 2:
            return Color.BLACK;
        case 3:
            return Color.BLUE;
        case 4:
            return Color.YELLOW;
    }
  return Color.RED;
}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View layout=inflater.inflate(R.layout.delivery_location_fragment_row,null);
//        TextView text = new TextView(getActivity());
//        text.setGravity(Gravity.CENTER);
//        text.setText(mContent);
//        text.setTextSize(20 * getResources().getDisplayMetrics().density);
//        text.setPadding(20, 20, 20, 20);
//        text.setBackgroundColor(Color.WHITE);
//        LinearLayout layout = new LinearLayout(getActivity());
//        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        layout.setGravity(Gravity.CENTER);
//        layout.addView(text);
//        layout.setBackgroundColor(anIntexColor);

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}
