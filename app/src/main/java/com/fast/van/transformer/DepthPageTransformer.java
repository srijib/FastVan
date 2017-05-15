package com.fast.van.transformer;

import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.utils.Log;

/**
 * Created by Amandeep Singh Bagli on 29/09/15.
 */
public class DepthPageTransformer  implements ViewPager.PageTransformer {
    int  margin=-1;
    @Override
    public void transformPage(View page, float position) {
        // margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10 * 3, page.getContext().getResources().getDisplayMetrics());


        if(margin==-1){
            margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10 * 3, page.getContext().getResources().getDisplayMetrics());
            Log.e("DepthPageTransformer", "margin:" + margin);
        }
        //int pageWidth = page.getWidth();
       //PageTransformer:-0.9285714 pre
       // PageTransformer:0.071428575 cur
       //PageTransformer:1.0714285 nxt
        // View backgroundView = page.findViewById(R.id.root);

       // page.setTranslationX((float) (pageWidth / 1.2 * position));

        float offset=Float.parseFloat(String.format("%.2f", position));
        Log.e("DepthPageTransformer", "PageTransformer:" + offset);
        View view=page.findViewById(R.id.parentLayoutD);
        //float scale = 1 - (position * 0.2f);
        //if(scale>1)
       // int padding=view.getPaddingTop();
       // if(padding!=0)
      //view.setPadding(0,50*(int)position/100,0,50*(int)position/100);
//        TextView textView1= (TextView) page.findViewById(R.id.textParcel);
//        TextView textView2= (TextView) page.findViewById(R.id.textRight);
//        textView1.setText(""+offset);
//        textView2.setText(""+offset);


         if(offset>0){
            offset=offset*100;
            if(offset>100)
                offset=100;

            view.setPadding(0,margin*(int)(offset)/100,0,margin*(int)(offset)/100);
        }else {
            offset=offset*-1;
            offset=offset*100;
            if(offset>100)
                offset=100;

            view.setPadding(0,margin*(int)(offset)/100,0,margin*(int)(offset)/100);
        }




//        else
//            view.setPadding(0,50*(int)scale/100,0,50*(int)scale/100);


//        View header = page.findViewById(R.id.header);
//
//        header.setTranslationX((float) (pageWidth / 1.2 * position));
//
//        View textView = page.findViewById(R.id.textView);

//        textView.setTranslationX((float) (pageWidth / 1.8 * position));

        //   Log.v("pageWidth = ", pageWidth+"");
        //   Log.v("position = ",position+"");

    }


}