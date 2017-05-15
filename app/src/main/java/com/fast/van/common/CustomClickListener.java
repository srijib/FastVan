package com.fast.van.common;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.ref.WeakReference;

/**
 * Created by Amandeep Singh Bagli on 06/10/15.
 */
public class CustomClickListener implements View.OnTouchListener {

    long time;
    WeakReference<ViewGroup> parent;

    public CustomClickListener(View view) {
        ViewGroup parent =(ViewGroup) view.getParent();
        this.parent = new WeakReference<ViewGroup>(parent);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                parent.get().setPressed(true);
                for (int i = 0; i < parent.get().getChildCount(); i++) {
                    if (parent.get().getChildAt(i) instanceof Button) {
                        parent.get().getChildAt(i).setPressed(true);
                    }
                }
                time = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_UP:
                parent.get().setPressed(false);
                for (int i = 0; i < parent.get().getChildCount(); i++) {
                    if (parent.get().getChildAt(i) instanceof Button) {
                        parent.get().getChildAt(i).setPressed(false);
                    }
                }
                if (System.currentTimeMillis() - time < 500) {
                    view.performClick();
                    return true;
                }
                break;
        }
        return true;
    }
}