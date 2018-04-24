package com.example.attaurrahman.pos.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.genrilUtils.API;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordEmailFragment extends Fragment {

    View parentView;
    @BindView(R.id.et_forget_email)
    EditText etForgetEmail;
    @BindView(R.id.btn_submit_forget_email)
    Button btnSubmitForgetEmail;
    String strEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_forget_password_email, container, false);

        ButterKnife.bind(this, parentView);

        btnSubmitForgetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = etForgetEmail.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    etForgetEmail.setError("Please enter valid email");
                } else {
                    API.ForgetPasswordEmailVerify(getActivity(),strEmail );
                }
            }
        });


        return parentView;
    }


}
