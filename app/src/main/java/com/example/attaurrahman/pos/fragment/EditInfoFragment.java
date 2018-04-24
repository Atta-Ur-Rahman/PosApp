package com.example.attaurrahman.pos.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.genrilUtils.API;
import com.example.attaurrahman.pos.genrilUtils.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditInfoFragment extends Fragment {

    View parentView;
//    @BindView(R.id.et_first_name)
//    EditText etFirstName;
//   /* @BindView(R.id.et_last_name)
//    EditText etLastName;
//    @BindView(R.id.iv_cross_first_name)
//    ImageView ivCrossFirstName;
//    @BindView(R.id.iv_cross_last_name)
//    ImageView ivCrossLastName;*/
    @BindView(R.id.et_edit_profile_first_name)
    EditText etEditFirstName;
    @BindView(R.id.et_edit_profile_last_name)
    EditText etEditlastName;
    @BindView(R.id.et_edit_profile_email)
    EditText etEditEmail;
    @BindView(R.id.et_edit_profile_mobile_num)
    EditText etEditMobileNum;
    @BindView(R.id.btn_edit_profile_save)
    Button btnEditProfileSave;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_edit_info, container, false);

        ButterKnife.bind(this, parentView);

       /* etFirstName.addTextChangedListener(genraltextWatcher);
        etLastName.addTextChangedListener(genraltextWatcher);
        etFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirstName.setText("");
            }
        });
        etLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLastName.setText("");
            }
        });
*/

        etEditFirstName.setText(Utilities.getSharedPreferences(getActivity()).getString("first_name", ""));
        etEditlastName.setText(Utilities.getSharedPreferences(getActivity()).getString("last_name", ""));
        etEditEmail.setText(Utilities.getSharedPreferences(getActivity()).getString("email", ""));
        etEditMobileNum.setText(Utilities.getSharedPreferences(getActivity()).getString("mobile_no", ""));


        btnEditProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "save", Toast.LENGTH_SHORT).show();

                String strFirstName = etEditFirstName.getText().toString();
                String strLastName = etEditlastName.getText().toString();
                String strEmail = etEditEmail.getText().toString();
                String strMobileNum = etEditMobileNum.getText().toString();

                if (strFirstName.length()==0) {
                    etEditFirstName.setError("Please enter first name");
                } else if (strLastName.length() == 0) {
                    etEditlastName.setError("Please enter last name");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches() && strEmail.length() ==0) {
                    etEditEmail.setError("Please enter email");
                } else if (strMobileNum.length() == 0) {
                    etEditMobileNum.setError("Please enter mobile number");
                } else {
                    API.EditProfileAPI(getActivity(), strFirstName, strLastName, strEmail, strMobileNum);
                }

            }
        });


        return parentView;
    }


  /*  private TextWatcher genraltextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etFirstName.getText().toString().length() > 0) {
                ivCrossFirstName.setVisibility(View.VISIBLE);
            } else {
                ivCrossFirstName.setVisibility(View.GONE);
            }
            if (etLastName.getText().toString().length() > 0) {
                ivCrossLastName.setVisibility(View.VISIBLE);
            } else {
                ivCrossLastName.setVisibility(View.GONE);
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };*/

}
