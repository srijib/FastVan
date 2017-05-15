package com.fast.van.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.fast.van.R;


/**
 * Copyright (c) 2015, Posiba. All rights reserved.
 *
 * @author Hien Ngo
 * @since 5/8/15
 */
public class TypekitFactory {
    public TypekitFactory() {
    }

    public View onViewCreated(View view, String name, View parent, Context context, AttributeSet attrs) {
        if (view == null) {
            return null;
        }

        if (view instanceof TextView) {
            TextView textView = (TextView) view;

            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Typekit);
            int fontValue = (array != null) ? array.getInt(R.styleable.Typekit_font, -1) : -1;
            if (fontValue != -1) {
                switch (fontValue) {
                    case 1:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.Black));
                        break;
                    case 2:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.Bold));
                        break;
                    case 3:

                        textView.setTypeface(Typekit.getInstance().get(FontStyle.BoldItalic));
                        break;
                    case 4:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.ExtraLight));
                        break;
                    case 5:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.ExtraLightItalic));
                        break;
                    case 6:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.Italic));
                        break;
                    case 7:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.Light));
                        break;
                    case 8:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.LightItalic));
                        break;
                    case 9:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.Regular));
                        break;
                    case 10:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.SemiBold));
                        break;
                    case 11:
                        textView.setTypeface(Typekit.getInstance().get(FontStyle.SemiBoldItalic));
                        break;

                }
            } else {

                textView.setTypeface(Typekit.getInstance().get(FontStyle.Regular));


            }


        }

        return view;
    }

    public View onViewCreated(View view, Context context, AttributeSet attrs) {
        //TODO: implement
        return view;
    }
}
