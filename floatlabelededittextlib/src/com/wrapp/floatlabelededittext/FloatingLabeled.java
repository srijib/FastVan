package com.wrapp.floatlabelededittext;

/**
 * Created by Clicklabs131 on 17/09/15.
 */

import android.annotation.TargetApi;
        import android.content.Context;
        import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
        import android.graphics.drawable.Drawable;
        import android.os.Build;
        import android.text.Editable;
        import android.text.TextUtils;
        import android.text.TextWatcher;
        import android.util.AttributeSet;
        import android.util.TypedValue;
        import android.view.Gravity;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.TextView;

        import com.nineoldandroids.animation.Animator;
        import com.nineoldandroids.animation.AnimatorListenerAdapter;
        import com.nineoldandroids.animation.AnimatorSet;
        import com.nineoldandroids.animation.ObjectAnimator;
        import com.nineoldandroids.view.animation.AnimatorProxy;


public class FloatingLabeled extends FrameLayout {

    private static final int DEFAULT_PADDING_LEFT = 2;

    private TextView mHintTextView;
    private EditText mEditText;

    private Context mContext;


    /**
     * @param context
     */
    public FloatingLabeled(Context context) {
        super(context);
        mContext = context;
    }


    /**
     * @param context
     * @param attrs
     */
    public FloatingLabeled(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setAttributes(attrs);

    }


    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FloatingLabeled(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setAttributes(attrs);
    }


    /**
     * @param attrs
     */
    private void setAttributes(AttributeSet attrs) {
        mHintTextView = new TextView(mContext);

        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.FloatLabeledEditText);

        final int padding = a.getDimensionPixelSize(R.styleable.FloatLabeledEditText_fletPadding, 0);
        final int defaultPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PADDING_LEFT, getResources().getDisplayMetrics());
        final int paddingLeft = a.getDimensionPixelSize(R.styleable.FloatLabeledEditText_fletPaddingLeft, defaultPadding);
        final int paddingTop = a.getDimensionPixelSize(R.styleable.FloatLabeledEditText_fletPaddingTop, 0);
        final int paddingRight = a.getDimensionPixelSize(R.styleable.FloatLabeledEditText_fletPaddingRight, 0);
        final int paddingBottom = a.getDimensionPixelSize(R.styleable.FloatLabeledEditText_fletPaddingBottom, 0);
        Drawable background = a.getDrawable(R.styleable.FloatLabeledEditText_fletBackground);

        if (padding != 0) {
            mHintTextView.setPadding(padding, padding, padding, padding);
        } else {
            mHintTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }

        if (background != null) {
            setHintBackground(background);
        }
        mHintTextView.setTextColor(Color.GRAY);
        mHintTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        //    Typeface type = Typeface.createFromAsset(getResources().getAssets(), "AvenirNext-Regular.ttf");
        try {
            mHintTextView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "AvenirNext-Regular.ttf"));
        } catch (Exception e) {
e.printStackTrace();
        }
        //  mHintTextView.setTextAppearance(mContext, a.getResourceId(R.styleable.FloatLabeledEditText_fletTextAppearance, android.R.style.TextAppearance_Small));

        //Start hidden
        mHintTextView.setVisibility(INVISIBLE);
        AnimatorProxy.wrap(mHintTextView).setAlpha(0);

        addView(mHintTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        a.recycle();
    }

    @SuppressWarnings("deprecation")
    private void setHintBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mHintTextView.setBackground(background);
        } else {
            mHintTextView.setBackgroundDrawable(background);

        }
    }

    /**
     * @param child
     * @param index
     * @param params
     */
    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            if (mEditText != null) {
                throw new IllegalArgumentException("Can only have one Edittext subview");
            }
            final LayoutParams lp = new LayoutParams(params);
            lp.gravity = Gravity.BOTTOM;
            lp.topMargin = (int) (mHintTextView.getTextSize() + mHintTextView.getPaddingBottom() + mHintTextView.getPaddingTop());
            params = lp;
            setEditText((EditText) child);
        }
        super.addView(child, index, params);
    }


    /**
     * @param gotFocus
     */
    private void onFocusChanged(boolean gotFocus) {
        if (gotFocus && mHintTextView.getVisibility() == VISIBLE) {
            ObjectAnimator.ofFloat(mHintTextView, "alpha", 0.83f, 1f).start();
        } else if (mHintTextView.getVisibility() == VISIBLE) {
            AnimatorProxy.wrap(mHintTextView).setAlpha(1f);  //Need this for compat reasons
            ObjectAnimator.ofFloat(mHintTextView, "alpha", 1f, 0.83f).start();
            // mHintTextView.setTextColor(getResources().getColor(R.color.light_grey));
        }
    }


    /**
     * @param show
     */
    private void setShowHint(final boolean show) {

        AnimatorSet animation = null;
        if ((mHintTextView.getVisibility() == VISIBLE) && !show) {
            animation = new AnimatorSet();
            ObjectAnimator move = ObjectAnimator.ofFloat(mHintTextView, "translationY", 0, mHintTextView.getHeight() / 8);
            ObjectAnimator fade = ObjectAnimator.ofFloat(mHintTextView, "alpha", 1, 0);
            animation.playTogether(move, fade);
        } else if ((mHintTextView.getVisibility() != VISIBLE) && show) {
            animation = new AnimatorSet();
            ObjectAnimator move = ObjectAnimator.ofFloat(mHintTextView, "translationY", mHintTextView.getHeight() / 8, 0);
            ObjectAnimator fade;
            if (mEditText.isFocused()) {
                fade = ObjectAnimator.ofFloat(mHintTextView, "alpha", 0, 1);
            } else {
                fade = ObjectAnimator.ofFloat(mHintTextView, "alpha", 0, 0.83f);

            }
            animation.playTogether(move, fade);
        }

        if (animation != null) {
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mHintTextView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mHintTextView.setVisibility(show ? VISIBLE : INVISIBLE);
                    AnimatorProxy.wrap(mHintTextView).setAlpha(show ? 1 : 0);
                }
            });
            animation.start();
        }
    }

    /**
     * @return
     */
    public EditText getEditText() {
        return mEditText;
    }


    /**
     * @param editText
     */
    private void setEditText(EditText editText) {
        mEditText = editText;

        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                setShowHint(!TextUtils.isEmpty(s));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean gotFocus) {
                onFocusChanged(gotFocus);
            }
        });

        mHintTextView.setText(mEditText.getHint());

        if (!TextUtils.isEmpty(mEditText.getText())) {
            mHintTextView.setVisibility(VISIBLE);
        }
    }

    /**
     * @return hint
     */
    public CharSequence getHint() {
        return mHintTextView.getHint();
    }


    /**
     * @param hint
     */
    public void setHint(String hint) {
        mEditText.setHint(hint);
        mHintTextView.setText(hint);
    }

}