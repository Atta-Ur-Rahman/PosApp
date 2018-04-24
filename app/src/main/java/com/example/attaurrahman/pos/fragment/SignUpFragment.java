package com.example.attaurrahman.pos.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.activity.MainActivity;
import com.example.attaurrahman.pos.genrilUtils.API;
import com.example.attaurrahman.pos.genrilUtils.Utilities;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class SignUpFragment extends Fragment implements View.OnClickListener {


    View parentView;
    String strEmail, strPhoneNumber, strPassword, strConfirmPassword, strPersonEmail, strGmailId, strProvider, strDeviceType, strDeviceToken;

    @BindView(R.id.et_sign_up_email)
    EditText etSignUpEmail;
    @BindView(R.id.et_sign_up_phone_num)
    EditText etSignUpPhoneNumber;
    @BindView(R.id.et_sign_up_password)
    EditText etSignUpPassword;
    @BindView(R.id.et_sign_up_confirm_password)
    EditText etSignUpConfirmPassword;
    @BindView(R.id.tv_sign_up_create)
    TextView tvSignUpCreate;
    @BindView(R.id.btn_sign_up_facebook)
    Button btnSignUpFacebook;
    @BindView(R.id.btn_sign_up_gmail)
    Button btnSignUpGmail;
    LoginButton loginButton;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        parentView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        loginButton = parentView.findViewById(R.id.login_button);


        ButterKnife.bind(this, parentView);
        tvSignUpCreate.setOnClickListener(this);
        btnSignUpFacebook.setOnClickListener(this);
        btnSignUpGmail.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUI(account);

        return parentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_up_create:
                inputSignUpData();

                break;
            case R.id.btn_sign_up_facebook:
                loginButton.performClick();
                ((MainActivity) getActivity()).facebook();
                Toast.makeText(getActivity(), "facebook", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_sign_up_gmail:
                signIn();
                break;
        }
    }

    private void inputSignUpData() {

        strEmail = etSignUpEmail.getText().toString();
        strPhoneNumber = etSignUpPhoneNumber.getText().toString();
        strPassword = etSignUpPassword.getText().toString();
        strConfirmPassword = etSignUpConfirmPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            etSignUpEmail.setError("Please enter valid email");
        } else if (strPhoneNumber.equals("") || strPhoneNumber.length() < 6) {
            etSignUpPhoneNumber.setError("Please enter phone number");
        } else if (!strPassword.equals(strConfirmPassword)) {
            etSignUpPassword.setError("Password does't matches");
        } else {
            API.SignUp(getActivity(), strEmail, strPassword, strPhoneNumber);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(getActivity(), "LogIn", Toast.LENGTH_SHORT).show();
//            String personId = account.getId();
//            strPersonEmail = account.getEmail();

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {

                strDeviceToken = Utilities.getSharedPreferences(getActivity()).getString("android_id", "");
                strDeviceType = "android";
                strProvider = "google";

                strPersonEmail = acct.getEmail();
                strGmailId = acct.getId();


                API.SocialLogin(getActivity(), strProvider, strGmailId, strDeviceType, strDeviceToken, strPersonEmail);


            }

            // Signed in successfully, show authenticated UI.


            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {

    }

}
