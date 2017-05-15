package com.fast.van.common;

/**
 * This class is use for all the basic validation which we use in project
 *
 * @author Eshant Mittal
 */

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

import com.fast.van.utils.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationEditText {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    // This TextWatcher sub-class formats entered numbers as 1 (123) 456-7890
    private boolean mFormatting; // this is a flag which prevents the
    // stack(onTextChanged)
    private boolean clearFlag;
    private int mLastStartLocation;
    private String mLastBeforeText;
    private Pattern pattern;
    private Matcher matcher;
    private InputFilter[] filterNameValidation = new InputFilter[]{new InputFilter() {
        public CharSequence filter(CharSequence src, int start, int end,
                                   Spanned dst, int dstart, int dend) {

            if (src.equals("")) { // for backspace
                return src;
            }
//       if (src.toString().matches("[a-zA-Z]+")) {
       if (src.toString().matches("\\p{L}+")) {

         //   if (dst.length()==0&&src.toString().matches("[A-Z]")) {


                return src;
            }


//            try {
//                String nsrc= null;
//                nsrc = src.toString().substring(1, src.length());
//                if (nsrc.matches("[a-z]+")) {
//                    return src;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


//            if (dstart!=0&&src.toString().matches("[a-z]+")) {
//                return src;
//            }
            /*if (src.toString().matches("[A-Z]{1}[a-z]+")) {
                return src;
            }*/

          /*  try {
              //  if(src.toString().substring(0, 1).matches("[A-Z]"))
             return    src.toString().substring(0, src.length()-1);
            } catch (Exception e) {
                e.printStackTrace();
            }*/


            return "";
        }
    }};
    private InputFilter[] filterPhoneValidation = new InputFilter[]{new InputFilter() {
        public CharSequence filter(CharSequence src, int start, int end,
                                   Spanned dst, int dstart, int dend) {

            if (src.equals("")) { // for backspace
                return src;
            }
         // if (src.toString().matches("[a-zA-Z]+")) {


            if (dst.length()==10) {


                return "";
            }
            if (src.toString().matches("[0-9]")) {


                return src;
            }


            return "";
        }
    }};

    /**
     * This is a method for various validation like Password, FirstName,LastName,FullName,PhoneNumber
     * <p/>
     * In this method we have to pass two
     *
     * @param textType:  Password for space validation means no space will be entered in password field.
     *                   FirstName for space validation and no any special character will be entered.
     *                   LastName for space validation and no any special character will be entered.
     *                   FullName no any special character will be entered
     *                   PhoneNumber entered numbers as 1 (123) 456-7890 or (999) 999-9999
     * @param editTexts: pass the edittext name on which the validation has to be check.
     *                   <p/>
     *                   You can also pass more than 1 edittext in one call.
     */

    public void setValidationFilter(TextType textType, EditText... editTexts) {
        switch (textType) {
            case Password:
            case VehicleNumber:
                editTextNoSpace(editTexts);
                break;
            case FirstName:
            case LastName:
                editTextNoSpace(editTexts);
               editTextNameFilter(editTexts);
                break;
            case FullName:
                editTextNameFilter(editTexts);
                break;
            case PhoneNumber:

                editTextNoSpace(editTexts);
                editTextPhoneFilter(editTexts);

              /*  for (final EditText editText : editTexts) {
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);
                    usPhoneNumberFormatter(editText);

                }*/
                break;
            default:
                break;
        }
    }

    /**
     * This method is use to Email validation
     *
     * @param email
     * @return
     */


    public Boolean IsEmailValid(String email) {

        pattern = Pattern.compile(EMAIL_PATTERN);

        matcher = pattern.matcher(email);

        return matcher.matches();

    }

    /**
     * @param editTexts
     */
    public void editTextNoSpace(EditText... editTexts) {

        for (final EditText editText : editTexts)

            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    // TODO Auto-generated method stub

                    String result = s.toString().trim();

                    if (!s.toString().equals(result)) {

                        editText.setText(result);

                        editText.setSelection(result.length());

                    }
                }

            });
    }

    /**
     * @param editTextName
     */
    public void editTextNameFilter(EditText... editTextName) {
        for (final EditText editTexts : editTextName) {
            editTexts.setFilters(filterNameValidation);
           // editTexts.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        }
    }
    public void editTextPhoneFilter(EditText... editTextName) {
        for (final EditText editTexts : editTextName) {
            editTexts.setFilters(filterPhoneValidation);

        }
    }

    /**
     * @param mWeakEditText
     */
    public void usPhoneNumberFormatter(final EditText mWeakEditText) {
        mWeakEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (after == 0 && s.toString().equals("1 ")) {
                    clearFlag = true;
                }
                mLastStartLocation = start;
                mLastBeforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // if (s.toString().length() == 1) {
                if (s.toString().equals("1")) {
                    mWeakEditText
                            .setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                                    16)});
                } else {
                    mWeakEditText
                            .setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                                    14)});
                }

                //   }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Make sure to ignore calls to afterTextChanged caused by the
                // work
                // done below
                if (!mFormatting) {
                    mFormatting = true;
                    int curPos = mLastStartLocation;
                    String beforeValue = mLastBeforeText;
                    String currentValue = s.toString();
                    String formattedValue = formatUsNumber(s);
                    if (currentValue.length() > beforeValue.length()) {
                        int setCusorPos = formattedValue.length()
                                - (beforeValue.length() - curPos);
                        mWeakEditText.setSelection(setCusorPos < 0 ? 0
                                : setCusorPos);
                    } else {
                        int setCusorPos = formattedValue.length()
                                - (currentValue.length() - curPos);
                        if (setCusorPos > 0
                                && !Character.isDigit(formattedValue
                                .charAt(setCusorPos - 1))) {
                            setCusorPos--;
                        }
                        mWeakEditText.setSelection(setCusorPos < 0 ? 0
                                : setCusorPos);
                    }
                    mFormatting = false;
                }
            }
        });
    }

    private String formatUsNumber(Editable text) {
        StringBuilder formattedString = new StringBuilder();
        // Remove everything except digits
        int p = 0;
        while (p < text.length()) {
            char ch = text.charAt(p);
            if (!Character.isDigit(ch)) {
                text.delete(p, p + 1);
            } else {
                p++;
            }
        }
        // Now only digits are remaining
        String allDigitString = text.toString();

        int totalDigitCount = allDigitString.length();

        if (totalDigitCount == 0
                || (totalDigitCount > 10 && !allDigitString.startsWith("1"))
                || totalDigitCount > 11) {
            // May be the total length of input length is greater than the
            // expected value so we'll remove all formatting
            text.clear();
            text.append(allDigitString);
            return allDigitString;
        }
        int alreadyPlacedDigitCount = 0;
        // Only '1' is remaining and user pressed backspace and so we clear
        // the edit text.
        if (allDigitString.equals("1") && clearFlag) {
            text.clear();
            clearFlag = false;
            return "";
        }
        if (allDigitString.startsWith("1")) {
            formattedString.append("1 ");
            alreadyPlacedDigitCount++;
        }
        // The first 3 numbers beyond '1' must be enclosed in brackets "()"
        if (totalDigitCount - alreadyPlacedDigitCount > 3) {
            formattedString.append("("
                    + allDigitString.substring(alreadyPlacedDigitCount,
                    alreadyPlacedDigitCount + 3) + ") ");
            alreadyPlacedDigitCount += 3;
        }
        // There must be a '-' inserted after the next 3 numbers
        if (totalDigitCount - alreadyPlacedDigitCount > 3) {
            formattedString
                    .append(allDigitString.substring(alreadyPlacedDigitCount,
                            alreadyPlacedDigitCount + 3) + "-");
            alreadyPlacedDigitCount += 3;
        }
        // All the required formatting is done so we'll just copy the
        // remaining digits.
        if (totalDigitCount > alreadyPlacedDigitCount) {
            formattedString.append(allDigitString
                    .substring(alreadyPlacedDigitCount));
        }

        text.clear();
        text.append(formattedString.toString());
        return formattedString.toString();
    }

    private String formatIndianNumber(Editable text) {
        StringBuilder formattedString = new StringBuilder();
        // Remove everything except digits
        int p = 0;
        while (p < text.length()) {
            char ch = text.charAt(p);
            if (!Character.isDigit(ch)) {
                text.delete(p, p + 1);
            } else {
                p++;
            }
        }
        // Now only digits are remaining
        String allDigitString = text.toString();

        int totalDigitCount = allDigitString.length();

        if (totalDigitCount == 0
                || totalDigitCount > 10 ) {
            // May be the total length of input length is greater than the
            // expected value so we'll remove all formatting
            text.clear();
            text.append(allDigitString);
            return allDigitString;
        }
        int alreadyPlacedDigitCount = 0;
        // Only '1' is remaining and user pressed backspace and so we clear
        // the edit text.
//        if (allDigitString.equals("1") && clearFlag) {
//            text.clear();
//            clearFlag = false;
//            return "";
//        }
//        if (allDigitString.startsWith("1")) {
//            formattedString.append("1 ");
//            alreadyPlacedDigitCount++;
//        }
        // The first 3 numbers beyond '1' must be enclosed in brackets "()"
        if (totalDigitCount - alreadyPlacedDigitCount > 3) {
            formattedString.append("("
                    + allDigitString.substring(alreadyPlacedDigitCount,
                    alreadyPlacedDigitCount + 3) + ") ");
            alreadyPlacedDigitCount += 3;
        }
        // There must be a '-' inserted after the next 3 numbers
        if (totalDigitCount - alreadyPlacedDigitCount > 3) {
            formattedString
                    .append(allDigitString.substring(alreadyPlacedDigitCount,
                            alreadyPlacedDigitCount + 3) + "-");
            alreadyPlacedDigitCount += 3;
        }
        // All the required formatting is done so we'll just copy the
        // remaining digits.
        if (totalDigitCount > alreadyPlacedDigitCount) {
            formattedString.append(allDigitString
                    .substring(alreadyPlacedDigitCount));
        }

        text.clear();
        text.append(formattedString.toString());
        return formattedString.toString();
    }

    public enum TextType {
        Password, FirstName, LastName, FullName, PhoneNumber,VehicleNumber
    }

}
