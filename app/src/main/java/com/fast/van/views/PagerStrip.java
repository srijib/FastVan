package com.fast.van.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class PagerStrip extends View {


    Paint grey = new Paint();
    Paint white = new Paint();
    float radius = 5;
    int currPosition = 0;
    float margin = 12;
    int size = 5;
    ViewPager pager;


    /**
     * @param context
     * @param attrs
     */
    public PagerStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        currPosition = 0;
        grey.setColor(Color.DKGRAY);
        grey.setAntiAlias(true);
        white.setColor(Color.WHITE);
        white.setAntiAlias(true);

        final float density = getResources().getDisplayMetrics().density;
        radius = (radius / 2) * density;
        margin = (margin / 2) * density;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        try {
            for (int i = 0; i < size; i++) {
                if (i == currPosition) {
//					canvas.drawCircle((i + 1) * circleArea - circleArea / 2,
//							circleY, radius, white);
//
                    canvas.drawCircle((margin * (i + 1) + (radius * 2) * i),
                            radius, radius, white);
                } else {
                    canvas.drawCircle((margin * (i + 1)) + (radius * 2) * i,
                            radius, radius, grey);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * @param curValue
     */
    public void SetCurrentValue(int curValue) {
        currPosition = curValue;
        invalidate();
    }


    /**
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
        setParams();
    }


    public void setParams() {
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) this.getLayoutParams();
        rl.height = (int) (radius * 2);
        rl.width = (int) (margin * (size) + (radius * 2) * size);
        this.setLayoutParams(rl);
    }


    /**
     * @param pager
     */
    public void setPager(ViewPager pager) {
        this.pager = pager;

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {

                SetCurrentValue(position % size);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}