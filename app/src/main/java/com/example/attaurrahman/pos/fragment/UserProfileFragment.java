package com.example.attaurrahman.pos.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attaurrahman.pos.Configuration.Config;
import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.genrilUtils.PosHelper;
import com.example.attaurrahman.pos.genrilUtils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileFragment extends Fragment {
    View parentView;
    @BindView(R.id.tv_profile_first_name)
    TextView tvProfileFirstName;
    @BindView(R.id.tv_profile_last_name)
    TextView tvProfileLastName;
    @BindView(R.id.tv_profile_email)
    TextView tvProfileEmail;
    @BindView(R.id.tv_profile_mobile_num)
    TextView tvProfileMobileNumber;
    @BindView(R.id.btn_edit_profile)
    Button btnEditProfile;



    String strFirstName, strLastName, strEmail, strMobileNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_user_profile, container, false);


        ButterKnife.bind(this, parentView);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.connectFragment(getActivity(),new EditInfoFragment());
            }
        });

        apicall();

        return parentView;
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "user_profile"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(response).getJSONObject("user_data");
                    tvProfileFirstName.setText((jsonObject.getString("first_name")));
                    tvProfileLastName.setText(jsonObject.getString("last_name"));
                    tvProfileEmail.setText(jsonObject.getString("email"));
                    tvProfileMobileNumber.setText(jsonObject.getString("mobile_no"));

                    String strFirstName = jsonObject.getString("first_name");
                    Utilities.putValueInEditor(getActivity()).putString("first_name",strFirstName).commit();
                    Utilities.putValueInEditor(getActivity()).putString("last_name",jsonObject.getString("last_name")).commit();
                    Utilities.putValueInEditor(getActivity()).putString("email",jsonObject.getString("email")).commit();
                    Utilities.putValueInEditor(getActivity()).putString("mobile_no",jsonObject.getString("mobile_no")).commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("zma pos profile response", response);
                if (response.contains("true")) {

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Log.d("zmaEmail",s+strPassword);
                String strToken = Utilities.getSharedPreferences(getActivity()).getString("token", "");
                String strUserId = Utilities.getSharedPreferences(getActivity()).getString("user_id", "");
                params.put("api_token", strToken);
                params.put("user_id", strUserId);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

}
