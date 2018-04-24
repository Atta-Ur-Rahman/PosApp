package com.example.attaurrahman.pos.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.genrilUtils.API;
import com.example.attaurrahman.pos.genrilUtils.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInFragment extends Fragment {
    View parentView;
    @BindView(R.id.et_login_email)
    EditText etLogInEmail;
    @BindView(R.id.et_login_password)
    EditText etLogInPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    String strEmail, strPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_log_in, container, false);

        ButterKnife.bind(this, parentView);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = etLogInEmail.getText().toString();
                strPassword = etLogInPassword.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    etLogInEmail.setError("Please enter valid email");

                } else if (strPassword.equals("") || strPassword.length() < 2) {
                    etLogInPassword.setError("Please enter password");
                } else {
                    API.LoginApiCall(getActivity(), strEmail, strPassword);
                }
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.connectFragment(getActivity(), new ForgetPasswordEmailFragment());
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.connectFragment(getActivity(), new SignUpFragment());
            }
        });


        return parentView;
    }



}
