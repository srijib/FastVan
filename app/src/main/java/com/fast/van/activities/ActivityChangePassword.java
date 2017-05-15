package com.fast.van.activities;

import android.content.Context;
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
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 07/10/15.
 */
public class ActivityChangePassword extends BaseActivity {
    private Button passwordCross, oldPasswordCross,confirmPasswordCross;
    private EditText oldPassword, password,conformPassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fastvan_activity_change_password);
        initScreen();
    }

    private void initScreen(){
        TextView title=(TextView)findViewById(R.id.screenTitle);
        title.setText("Change Password");
        findViewById(R.id.backbutton).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);
        ValidationEditText validationEditText=new ValidationEditText();
        oldPassword = (EditText) findViewById(R.id.oldpassword);
        password = (EditText) findViewById(R.id.password);
        conformPassword = (EditText) findViewById(R.id.confirmpassword);
        validationEditText.setValidationFilter(ValidationEditText.TextType.Password,oldPassword);
        validationEditText.setValidationFilter(ValidationEditText.TextType.Password,password);
        validationEditText.setValidationFilter(ValidationEditText.TextType.Password,conformPassword);

        passwordCross = (Button) findViewById(R.id.passwordCross);


        oldPasswordCross = (Button) findViewById(R.id.oldpasswordCross);
        confirmPasswordCross = (Button) findViewById(R.id.confirmPasswordCross);
        setCrossClickListener();
        setTextChangeListeners();
        setFocusChangeListeners();
        hideKeyboard();

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
        oldPasswordCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword.setText("");
            }
        });

        passwordCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });
        confirmPasswordCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conformPassword.setText("");
            }
        });

    }

    public void setFocusChangeListeners() {
        oldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean focus) {
                if (focus && !oldPassword.getText().toString().isEmpty()) {
                    oldPasswordCross.setVisibility(View.VISIBLE);
                    oldPassword.setError(null);
                } else {
                    oldPasswordCross.setVisibility(View.GONE);
                }
            }
        });


        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean focus) {
                if (focus && !password.getText().toString().isEmpty()) {
                    passwordCross.setVisibility(View.VISIBLE);
                    password.setError(null);
                } else {
                    passwordCross.setVisibility(View.GONE);
                }
            }
        });
        conformPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean focus) {
                if (focus && !conformPassword.getText().toString().isEmpty()) {
                    confirmPasswordCross.setVisibility(View.VISIBLE);
                    conformPassword.setError(null);
                } else {
                    confirmPasswordCross.setVisibility(View.GONE);
                }
            }
        });
    }


    public void setTextChangeListeners() {
        password.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

                if (s.toString().length() > 0) {

                    passwordCross.setVisibility(View.VISIBLE);
                } else {

                    passwordCross.setVisibility(View.GONE);
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

                password.setError(null);
            }
        });
        conformPassword.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

                if (s.toString().length() > 0) {

                    confirmPasswordCross.setVisibility(View.VISIBLE);
                } else {

                    confirmPasswordCross.setVisibility(View.GONE);
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

                conformPassword.setError(null);
            }
        });

        oldPassword.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

                if (s.toString().length() > 0) {

                    oldPasswordCross.setVisibility(View.VISIBLE);
                } else {

                    oldPasswordCross.setVisibility(View.GONE);
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
                oldPassword.setError(null);

            }
        });
    }

    private boolean validateform(){
        boolean validated=true;
        if(Validator.passwordValidator(oldPassword)){
            if(validated)
                oldPassword.requestFocus();
            validated=false;
            oldPasswordCross.setVisibility(View.GONE);

            if(oldPassword.getText().toString().isEmpty())
                oldPassword.setError(getString(R.string.oldpasswordrequired));
            else
                oldPassword.setError(getString(R.string.passwordlenghtrequired));

        }
        if(Validator.passwordValidator(password)){
            if(validated)
                password.requestFocus();
            validated=false;
            passwordCross.setVisibility(View.GONE);
            if(password.getText().toString().isEmpty())
            password.setError(getString(R.string.passwordrequired));
            else
            password.setError(getString(R.string.passwordlenghtrequired));

        }
        if(Validator.passwordValidator(conformPassword)){
            if(validated)
                conformPassword.requestFocus();
            validated=false;
            confirmPasswordCross.setVisibility(View.GONE);

            if(conformPassword.getText().toString().isEmpty())
                conformPassword.setError(getString(R.string.confirmpasswordrequired));
            else
                conformPassword.setError(getString(R.string.passwordlenghtrequired));

        }

        if (!conformPassword.getText().toString().contentEquals(password.getText().toString())) {
            if(validated)
                conformPassword.requestFocus();
            validated=false;
            confirmPasswordCross.setVisibility(View.GONE);
            conformPassword.setError(getString(R.string.confirmpasswordnotmatchedrequired));
        }
        return  validated;
    }

    @Override
    public void onClickView(View v) {
        int id=v.getId();
        switch (id){
            case R.id.submit:
                changePassword();
                break;
            case R.id.backbutton:
                onBackPressed();
                break;


        }
    }

    private void changePassword(){
        if(validateform()){


            Login user = CommonData.getSessionData(this);

if(user!=null) {
    LoadingBox.showLoadingDialog(activity, "Changing...");
    RestClient.getApiServiceForPojo().driverResetPassword(user.getAccessToken(), oldPassword.getText().toString(), password.getText().toString(), new Callback<ServiceReply>() {
        @Override
        public void success(ServiceReply s, Response response) {
            Log.d("Success", "changePassword:" + s.toString());
            oldPassword.setText("");
            password.setText("");
            conformPassword.setText("");

            LoadingBox.dismissLoadingDialog();

            CommonDialog.With(activity).show(s.getMessage());
        }

        @Override
        public void failure(RetrofitError error) {
            LoadingBox.dismissLoadingDialog();
            ErrorCodes.checkCode(activity, error);


        }
    });
}

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Transformer.leftToRight(activity);
    }
}
