package com.fast.van.common;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class Validator {

    public static boolean textValidator(EditText editText) {
        return editText.getText().toString().isEmpty();
    }
    public static boolean passwordValidator(EditText editText) {
        if (editText.getText().toString().isEmpty())
            return true;

        if (editText.getText().toString().length()<6)
            return true;

        return false;

    }
    public static boolean nameValidator(EditText editText) {
        if (editText.getText().toString().isEmpty()||editText.getText().toString().length()<3)
            return true;
        return false;
    }

    public static boolean emailValidator(EditText editText) {

/*
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9\\-]+)*(\\.[A-Za-z]{2,})$";

//        if (editText.getText().toString().isEmpty())
//            return true;
        Pattern pattern;
        pattern = Pattern.compile(EMAIL_PATTERN);

        Matcher matcher = pattern.matcher(editText.getText().toString());

        return matcher.matches();
//        if (editText.getText().toString().isEmpty())
//            return true;
     //   return !TextUtils.isEmpty(editText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches();     //   return false;
    }

    public static boolean numberValidator(EditText editText) {

        if (editText.getText().toString().isEmpty())
            return true;
        //this is validation for SA
//        String pat="^(\\+?27|0)[6-9][1-7][0-9]{7}$";
//        Pattern pattern=Pattern.compile(pat);
//       return !pattern.matcher(editText.getText()).matches();
        if (editText.getText().toString().trim().replaceAll("[^0-9]", "").length() < 7)
            return true;

        return false;//editText.getText().toString().trim().replaceAll("[^0-9]", "").toString().charAt(0) == '0';
    }
    public static boolean varificationCodeValidator(EditText editText) {

        if (editText.getText().toString().isEmpty())
            return true;
        String pat="^[0-9]{6}$";
        Pattern pattern=Pattern.compile(pat);
       return !pattern.matcher(editText.getText()).matches();
//        if (editText.getText().toString().trim().replaceAll("[^0-9]", "").length() != 10)
//            return true;
//        if (editText.getText().toString().trim().replaceAll("[^0-9]", "").toString().charAt(0) == '0')
//            return true;
//        return false;
    }
    public static boolean vehicleNumValidator(EditText editText) {
        if(editText.getText().toString().isEmpty())
            return true;
  /*      if(editText.getText().toString().matches("([A-Z+]+[0-9+]+([A-Z+]?)+)$"))
            return false;*/

        return false;


    }

}
