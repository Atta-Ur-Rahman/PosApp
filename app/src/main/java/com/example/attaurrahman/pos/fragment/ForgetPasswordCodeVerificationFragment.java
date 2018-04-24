package com.example.attaurrahman.pos.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.genrilUtils.API;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordCodeVerificationFragment extends Fragment {

    View parentView;
    String strVerifyCode;
    @BindView(R.id.et_code_num1)EditText etNum1;
    @BindView(R.id.et_code_num2)EditText etNum2;
    @BindView(R.id.et_code_num3)EditText etNum3;
    @BindView(R.id.et_code_num4)EditText etNum4;
    @BindView(R.id.et_code_num5)EditText etNum5;
    @BindView(R.id.et_code_num6)EditText etNum6;
    @BindView(R.id.btn_forget_password_verify_code)Button btnForgetPasswordVerifyCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_forget_password_code_verification, container, false);
        ButterKnife.bind(this,parentView);
       CodeVerifyInputData();

       btnForgetPasswordVerifyCode.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String num1 = etNum1.getText().toString();
               String num2 = etNum2.getText().toString();
               String num3 = etNum3.getText().toString();
               String num4 = etNum4.getText().toString();
               String num5 = etNum5.getText().toString();
               String num6 = etNum6.getText().toString();

               strVerifyCode = num1 + num2 + num3 + num4 + num5 + num6;

               if (strVerifyCode.length()==6){

                   API.ForgetPasswordVerifyCode(getActivity(),strVerifyCode);

               }else {
                   Toast.makeText(getActivity(), "Please enter verify code", Toast.LENGTH_SHORT).show();
               }
           }
       });

        return parentView;
    }

    private void CodeVerifyInputData() {

        etNum1.addTextChangedListener(genraltextWatcher);
        etNum2.addTextChangedListener(genraltextWatcher);
        etNum3.addTextChangedListener(genraltextWatcher);
        etNum4.addTextChangedListener(genraltextWatcher);
        etNum5.addTextChangedListener(genraltextWatcher);
        etNum6.addTextChangedListener(genraltextWatcher);

    }

    private TextWatcher genraltextWatcher = new TextWatcher() {
        private View view;
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (etNum1.length()==1) {

                etNum2.requestFocus();

            }if (etNum2.length()==1){

                etNum3.requestFocus();

            }if (etNum3.length()==1) {

                etNum4.requestFocus();

            }if (etNum4.length()==1){

                etNum5.requestFocus();

            }if (etNum5.length()==1){

                etNum6.requestFocus();

            }if (etNum6.length()==1){


            }

        }

    };



}
