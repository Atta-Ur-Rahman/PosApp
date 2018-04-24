package com.example.attaurrahman.pos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.fragment.CompleteJobsFragment;
import com.example.attaurrahman.pos.fragment.FlowExpreeTerminalsFragment;
import com.example.attaurrahman.pos.fragment.LogInFragment;
import com.example.attaurrahman.pos.fragment.MyAccountFragment;
import com.example.attaurrahman.pos.fragment.PosJobsFragment;
import com.example.attaurrahman.pos.fragment.UserProfileFragment;
import com.example.attaurrahman.pos.genrilUtils.API;
import com.example.attaurrahman.pos.genrilUtils.Utilities;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CallbackManager callbackManager;
    String strProvider, strFacebookId, strDeviceType, strDeviceId,strEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


       Utilities.connectFragmentWithOutBackStack(this,new LogInFragment());



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mission) {
            Utilities.connectFragmentWithOutBackStack(this,new PosJobsFragment());
        } else if (id == R.id.nav_complete) {
            Utilities.connectFragmentWithOutBackStack(this,new CompleteJobsFragment());

        } else if (id == R.id.nav_my_account) {
            Utilities.connectFragmentWithOutBackStack(this,new MyAccountFragment());

        } else if (id == R.id.nav_edit_info) {
            Utilities.connectFragmentWithOutBackStack(this,new UserProfileFragment());

        } else if (id == R.id.nav_flow_express_terminal) {
            Utilities.connectFragmentWithOutBackStack(this,new  FlowExpreeTerminalsFragment());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void facebook() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                displayuserInfo(object);


                            }
                        });

                        Toast.makeText(MainActivity.this, "login", Toast.LENGTH_SHORT).show();

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "name,first_name, last_name,email,gender"); // Parámetros que pedimos a facebook
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayuserInfo(JSONObject object) {

        try {


            strFacebookId = object.getString("id");
            strEmail = object.getString("email");

            strProvider = "facebook";
            strDeviceType="android";

            API.SocialLogin(MainActivity.this,strProvider,strFacebookId,strDeviceType,strDeviceId,strEmail);



//            Toast.makeText(this, strFacebookId, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, strEmail, Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();


        }


        ;


    }

    public void intent() {

        Intent nextScreen = new Intent(this, MapsActivity.class);
        startActivity(nextScreen);
    }
}
