package com.example.attaurrahman.pos.genrilUtils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attaurrahman.pos.Configuration.Config;
import com.example.attaurrahman.pos.fragment.CompleteJobsFragment;
import com.example.attaurrahman.pos.fragment.ForgetPasswordCodeVerificationFragment;
import com.example.attaurrahman.pos.fragment.PosJobsFragment;
import com.example.attaurrahman.pos.fragment.UserProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.attaurrahman.pos.Configuration.Config.BASE_URL;

/**
 * Created by AttaUrRahman on 3/20/2018.
 */

public class API {


    public static void SignUp(final Context context, final String strEmail, final String strPassword, final String strPhoneNumber) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + "sign_up",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response).getJSONObject("user_data");
                            Utilities.putValueInEditor(context).putString("token", jsonObject.getString("api_token")).commit();
                            Utilities.putValueInEditor(context).putString("user_id", jsonObject.getString("id")).commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (response.contains("User Registered Successfully")) {
                            Toast.makeText(context, "Succesfull", Toast.LENGTH_SHORT).show();
                            Log.d("zama response", response.toString());
                            // Utilities.connectFragmentForMyComplaint(context, new Complaint_Fragment());
                            Utilities.putValueInEditor(context).putBoolean("isLogin", true).commit();
                        } else if (response.contains("This Eamil is Already Registered Please Try With Another one")) {
                            Toast.makeText(context, "Already Registerd", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();

                        NetworkResponse networkResponse = error.networkResponse;

                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout";

                            } else if (error.getClass().equals(NoConnectionError.class)) {

                                errorMessage = "Failed to connect server";
                            }
                        }
                        Log.i("Error", errorMessage);
                        error.printStackTrace();
                    }
                }
        ) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("email", strEmail);
                params.put("password", strPassword);
                params.put("mobile_no", strPhoneNumber);


                return checkParams(params);
            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private static Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }

        }
        return map;

    }


    public static void LoginApiCall(final Context context, final String strLogInEmail, final String strLogInPassword) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response).getJSONObject("user_data");
                    Utilities.putValueInEditor(context).putString("token", jsonObject.getString("api_token")).commit();
                    Utilities.putValueInEditor(context).putString("user_id", jsonObject.getString("id")).commit();
                    Utilities.connectFragmentWithOutBackStack(context, new CompleteJobsFragment());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Log.d("zmaEmail",s+strPassword);
                params.put("email", strLogInEmail);
                params.put("password", strLogInPassword);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }


    public static void ForgetPasswordEmailVerify(final Context context, final String strVerifyEmail) {


        final StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL + "forgotpassword", new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("Verification code is sent to you via email.")) {
                            Toast.makeText(context, "Succesfull", Toast.LENGTH_SHORT).show();
                            Utilities.connectFragmentWithOutBackStack(context, new ForgetPasswordCodeVerificationFragment());

                        } else {
                            Toast.makeText(context, "Wrong Email", Toast.LENGTH_SHORT).show();

                        }
                        Log.d("zama respose chnage ", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

                        // btnForgetPasswordSubmit.setEnabled(true);
                        Log.d("zama error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("email", strVerifyEmail);


                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

    }

    public static void ForgetPasswordVerifyCode(final Context context, final String strVerifyCode) {


        final StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL + "forgot_code_verify", new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("true")) {

                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "wrong number", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("code", strVerifyCode);


                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);


    }


    public static void SocialLogin(final Context context, final String strPorvider, final String strProviderID, final String strDeviceType, final String strDeviceToken, final String strEmail) {


        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL + "sociallogin", new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("zama respose", response);

                        JSONObject jsonObject = null;
                        try {

                            jsonObject = new JSONObject(response).getJSONObject("user");
                            Utilities.putValueInEditor(context).putString("token", jsonObject.getString("api_token")).commit();
                            Utilities.putValueInEditor(context).putString("user_id", jsonObject.getString("id")).commit();
                            Utilities.connectFragmentWithOutBackStack(context, new PosJobsFragment());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (response.contains("User Registered Successfully!")) {

                            Toast.makeText(context, "Succesfull", Toast.LENGTH_SHORT).show();
                            Log.d("zama respose", response);
                        } else if (response.contains("User already Registered")) {
                            Toast.makeText(context, "User already Registered", Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                        Log.d("zma socil log in error", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("provider", strPorvider);
                params.put("provider_id", strProviderID);
                params.put("device_type", strDeviceType);
                params.put("device_token", strDeviceToken);
                params.put("email", strEmail);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);


    }

    public static void PosJobs(final Context context, final String strUrl, final String strArray, final List<PosHelper> list, final PosJobsAdapter posJobsAdapter) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + strUrl
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                Log.d("zma pos job response", response);
                if (response.contains("true")) {
                    try {

                        jsonObject = new JSONObject(response);
                        JSONArray jsonArr = jsonObject.getJSONArray(strArray);
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject temp = jsonArr.getJSONObject(i);

                            PosHelper posHelper = new PosHelper();
                            posHelper.setStrJobId(temp.getString("job_id"));
                            posHelper.setStrJobTitle(temp.getString("job_title"));
                            posHelper.setStrDescription(temp.getString("description"));
                            posHelper.setStrLatitude(temp.getString("latitude"));
                            posHelper.setStrLongitude(temp.getString("longitude"));
                            posHelper.setStrTime(temp.getString("time"));
                            posHelper.setStrPosImage(temp.getString("image"));
                            list.add(posHelper);


                        }
                        posJobsAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                String strToken = Utilities.getSharedPreferences(context).getString("token", "");
                params.put("api_token", strToken);


                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    public static void PosCompleteJobs(final Context context, String strUrl, final List<PosHelper> list, final PosJobsAdapter posJobsAdapter) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + strUrl
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("zma response ", response);
                if (response.contains("true")) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArr = jsonObject.getJSONArray("user_jobs");
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject temp = jsonArr.getJSONObject(i);
                            PosHelper posHelper = new PosHelper();

                            String firstName = temp.getString("first_name");
                            String lastName = temp.getString("last_name");
                            String email = temp.getString("email");
                            String usersId = temp.getString("users_id");


                            posHelper.setStrJobTitle(temp.getString("job_title"));
                            posHelper.setStrDescription(temp.getString("description"));
                            posHelper.setStrLatitude(temp.getString("latitude"));
                            posHelper.setStrLongitude(temp.getString("longitude"));
                            posHelper.setStrPosImage(temp.getString("image"));
                            list.add(posHelper);


                        }
                        posJobsAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                String strToken = Utilities.getSharedPreferences(context).getString("token", "");
                String strUserId = Utilities.getSharedPreferences(context).getString("user_id", "");
                params.put("api_token", strToken);
                params.put("users_id", strUserId);

                Log.d("zma userid ao api token", strToken + "&&" + strUserId);


                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }

    public static void UserJobsAccepted(final Context context) {

        Toast.makeText(context, "UserJobs", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "jobs_accepted"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    Toast.makeText(context, "Job Accepted", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                String strToken = Utilities.getSharedPreferences(context).getString("token", "");
                String strJobId = Utilities.getSharedPreferences(context).getString("job_id", "");
                String strUserId = Utilities.getSharedPreferences(context).getString("user_id", "");
                params.put("api_token", strToken);
                params.put("job_id", strJobId);
                params.put("users_id", strUserId);

                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }


    public static void EditProfileAPI(final Context context, final String strFirstName, final String strLastName, final String strEmail, final String strMobileNum) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"edit_profile"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("true")) {
                    Toast.makeText(context, "Edit Profile Succesful", Toast.LENGTH_SHORT).show();
                    Utilities.connectFragmentWithOutBackStack(context,new UserProfileFragment());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String strToken = Utilities.getSharedPreferences(context).getString("token", "");
                String strUserId = Utilities.getSharedPreferences(context).getString("user_id", "");
                params.put("user_id", strUserId);
                params.put("first_name", strFirstName);
                params.put("last_name", strLastName);
                params.put("email", strEmail);
                params.put("mobile_no", strMobileNum);
                params.put("api_token", strToken);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

}
