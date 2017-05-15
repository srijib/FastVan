package com.fast.van.fonts;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2015, Posiba. All rights reserved.
 *
 * @author Amandeep Singh
 * @since 22/9/15
 */
public class Typekit {
    private static Typekit ourInstance = new Typekit();
    private Map<FontStyle, Typeface> mFonts = new HashMap<>();

    private Typekit() {
    }

    public static Typekit getInstance() {
        return ourInstance;
    }

    public static Typeface createFromAsset(Context context, String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }

    public Typeface get(FontStyle fontStyle) {
        return mFonts.get(fontStyle);
    }

    public Typekit addBlack(Typeface typeface) {
        mFonts.put(FontStyle.Black, typeface);
        return this;
    }
    public Typekit addBoldItalic(Typeface typeface) {
        mFonts.put(FontStyle.BoldItalic, typeface);
        return this;
    }

    public Typekit addRegular(Typeface typeface) {
        mFonts.put(FontStyle.Regular, typeface);
        return this;
    }

    public Typekit addBold(Typeface typeface) {
        mFonts.put(FontStyle.Bold, typeface);
        return this;
    }

    public Typekit addExtraLight(Typeface typeface) {
        mFonts.put(FontStyle.ExtraLight, typeface);
        return this;
    }
    public Typekit addExtraLightItalic(Typeface typeface) {
        mFonts.put(FontStyle.ExtraLightItalic, typeface);
        return this;
    }
    public Typekit addItalic(Typeface typeface) {
        mFonts.put(FontStyle.Italic, typeface);
        return this;
    }
    public Typekit addLight(Typeface typeface) {
        mFonts.put(FontStyle.Light, typeface);
        return this;
    }
    public Typekit addLightItalic(Typeface typeface) {
        mFonts.put(FontStyle.LightItalic, typeface);
        return this;
    }
    public Typekit addSemiBold(Typeface typeface) {
        mFonts.put(FontStyle.SemiBold, typeface);
        return this;
    }
    public Typekit addSemiBoldItalic(Typeface typeface) {
        mFonts.put(FontStyle.SemiBoldItalic, typeface);
        return this;
    }





}
