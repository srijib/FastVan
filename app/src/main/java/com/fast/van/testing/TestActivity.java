package com.fast.van.testing;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fast.van.R;
import com.fast.van.activities.BaseActivity;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class TestActivity extends BaseActivity{



    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 18;
    TextView signup, login;
    Button backBtn;
    EditText email, password;
    LinearLayout googlePlus, facebook;



    Button forgotPassword, passwordCross, emailCross;


    /*
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String callfrom="nowhere";
        Bundle extra=getIntent().getExtras();

        if(extra!=null){
            callfrom=extra.getString("caller","somewhere");
        }

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        googlePlus = (LinearLayout) findViewById(R.id.googlePlus);
        facebook = (LinearLayout) findViewById(R.id.facebook);

        signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"Under Construction",Toast.LENGTH_LONG).show();
            }
        });

        login = (TextView) findViewById(R.id.login);
        login.setText(callfrom);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"Under Construction login",Toast.LENGTH_LONG).show();
            }
        });

        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        forgotPassword = (Button) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"Under Construction forgotPassword",Toast.LENGTH_LONG).show();
            }
        });

        forgotPassword.setText(Html.fromHtml("?"));


        passwordCross = (Button) findViewById(R.id.passwordCross);


        emailCross = (Button) findViewById(R.id.emailCross);


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
        emailCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
            }
        });

        passwordCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });

    }

    public void setFocusChangeListeners() {
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean focus) {
                if (focus && !email.getText().toString().isEmpty()) {
                    emailCross.setVisibility(View.VISIBLE);
                } else {
                    emailCross.setVisibility(View.GONE);
                }
            }
        });


        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean focus) {
                if (focus && !password.getText().toString().isEmpty()) {
                    passwordCross.setVisibility(View.VISIBLE);
                } else {
                    passwordCross.setVisibility(View.GONE);
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
                    forgotPassword.setVisibility(View.GONE);
                    passwordCross.setVisibility(View.VISIBLE);
                } else {
                    forgotPassword.setVisibility(View.VISIBLE);
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


            }
        });


        email.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

                if (s.toString().length() > 0) {

                    emailCross.setVisibility(View.VISIBLE);
                } else {

                    emailCross.setVisibility(View.GONE);
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

    @Override
    public void onClickView(View v) {

    }
}
