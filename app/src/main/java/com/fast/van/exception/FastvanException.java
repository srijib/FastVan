package com.fast.van.exception;

import android.content.ActivityNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Amandeep Singh Bagli on 13/12/15.
 */
public class FastvanException extends Exception{
    public FastvanException(String msg) {
        super(msg);
    }

    public FastvanException(ActivityNotFoundException e) {
        super(e);
    }

    public FastvanException(FileNotFoundException e) {
        super(e);
    }

    public FastvanException(IOException e) {
        super(e);
    }
    public FastvanException(IllegalArgumentException e) {
        super(e);
    }

}
