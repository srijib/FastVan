package com.fast.van.utils.map;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout {

    private OnTouchListener onTouchListener;

    public TouchableWrapper(Context context) {
        super(context);
    }

    public void setTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			onTouchListener.onTouch();
//			break;
//		case MotionEvent.ACTION_UP:
//			onTouchListener.onRelease();
//			break;
//		}

        return super.dispatchTouchEvent(event);
    }

    public interface OnTouchListener {
        void onTouch();

        void onRelease();
    }
}
