package com.fast.van.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.common.ValidationEditText;
import com.fast.van.common.Validator;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.dialogs.ConfirmationDialogCodes;
import com.fast.van.dialogs.ConfirmationDialogEventsListener;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.ServiceReply;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 17/09/15.
 */
public class ActivityForgetPassword extends BaseActivity implements ConfirmationDialogEventsListener {
    private EditText phonenumber;
    private Button  phoneCross;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.fastvan_activity_forget_password);


        init();


    }
    private void init(){
        findViewById(R.id.backbutton).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);
        ValidationEditText validationEditText=new ValidationEditText();

        TextView title=(TextView)findViewById(R.id.screenTitle);
        title.setText("Forgot Password");
        phonenumber = (EditText) findViewById(R.id.mobilenumber);
        validationEditText.setValidationFilter(ValidationEditText.TextType.PhoneNumber,phonenumber);
        phoneCross = (Button) findViewById(R.id.phoneCross);
        setCrossClickListener();
        setTextChangeListeners();
        setFocusChangeListeners();
        hideKeyboard();

    }

    @Override
    public void onClickView(View v) {
        int id=v.getId();

        switch (id){
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.submit:
                resetforgetPassword();
                break;
        }
    }
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setCrossClickListener() {
        phoneCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber.setText("");
            }
        });



    }

    public void setFocusChangeListeners() {
        phonenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean focus) {
                if (focus && !phonenumber.getText().toString().isEmpty()) {
                    phoneCross.setVisibility(View.VISIBLE);
                    phonenumber.setError(null);
                } else {
                    phoneCross.setVisibility(View.GONE);
                }
            }
        });



    }


    public void setTextChangeListeners() {



        phonenumber.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

                if (s.toString().length() > 0) {

                    phoneCross.setVisibility(View.VISIBLE);
                } else {

                    phoneCross.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (s.toString().length() > 0) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }

    private boolean validateform(){
        boolean validated=true;
        if(Validator.numberValidator(phonenumber)){
            validated=false;
            phoneCross.setVisibility(View.GONE);
            phonenumber.setError(getString(R.string.phonerequired));

        }

        return  validated;
    }
    private void resetforgetPassword(){

        if(validateform()) {
            final ConfirmationDialogEventsListener confirmationDialogEventsListener=this;
            LoadingBox.showLoadingDialog(activity, "Resetting...");

            RestClient.getApiServiceForPojo().driverForgotPassword(phonenumber.getText().toString(),  new Callback<ServiceReply>() {
                @Override
                public void success(ServiceReply s, Response response) {
                    Log.d("Success", "RestPassword:" + s.toString());
                    //CommonDialog.With(activity).show(s.getMessage());

                    LoadingBox.dismissLoadingDialog();

                    CommonDialog.with(activity, confirmationDialogEventsListener).show(s.getMessage(), ConfirmationDialogCodes.forgotPassword);
                }

                @Override
                public void failure(RetrofitError error) {
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);


                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Transformer.leftToRight(activity);
    }

    @Override
    public void OnOkButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {
        switch (confirmationDialogCode){
            case forgotPassword:
               onBackPressed();
                break;
        }
    }

    @Override
    public void OnCancelButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {

    }
}
