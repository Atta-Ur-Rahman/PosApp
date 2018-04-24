package com.example.attaurrahman.pos.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.activity.MainActivity;
import com.example.attaurrahman.pos.genrilUtils.HTTPMultiPartEntity;
import com.example.attaurrahman.pos.genrilUtils.Utilities;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.example.attaurrahman.pos.Configuration.Config.BASE_URL;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FlowExpreeTerminalsFragment extends Fragment implements View.OnClickListener {

    View parentView;
    @BindView(R.id.iv_first_image)
    ImageView ivFirstImage;
    @BindView(R.id.iv_second_image)
    ImageView ivSecondImage;
    @BindView(R.id.iv_current_time)
    ImageView ivCurrentTime;
    @BindView(R.id.iv_current_location)
    ImageView ivCurrentLocation;
    @BindView(R.id.iv_authentication)
    ImageView ivAuthentication;
    @BindView(R.id.iv_check_authentication)
    ImageView ivCheckAuthentication;
    @BindView(R.id.iv_current_check_time)
    ImageView ivCheckTime;
    @BindView(R.id.btn_flow_express_terminals_send)
    Button btnFlowSend;
    @BindView(R.id.et_comments)
    EditText etComments;
    @BindView(R.id.progress_view)
    CircularProgressView circularProgressView;
    LocationManager locationManager;

    Boolean flagFirstImage, flagSecondImage, flagLocation, flagTime, flagAuthentication;


    String strFirstImage, strSecondImage, strApiToken, strCurrentDateandTime, strLatitude, strLongitude;
    Uri image_uri;
    File fileFirstImage, fileSecondImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_flow_expree_terminals, container, false);

        ButterKnife.bind(this, parentView);
        flagFirstImage = false;
        flagSecondImage = false;
        flagLocation = false;
        flagTime = false;
        flagAuthentication = false;

        strApiToken = Utilities.getSharedPreferences(getActivity()).getString("token", "");


        ivFirstImage.setOnClickListener(this);
        ivSecondImage.setOnClickListener(this);
        btnFlowSend.setOnClickListener(this);
        ivCurrentLocation.setOnClickListener(this);
        ivCurrentTime.setOnClickListener(this);
        ivAuthentication.setOnClickListener(this);

        return parentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_first_image:

                //DialogUtils.fancyAlertDialog(getActivity());
                // intentCameraPic();
                fancydialo();
                flagFirstImage = true;
                flagSecondImage = false;

                break;
            case R.id.iv_second_image:
                //intentGalleryPic();
                fancydialo();
                flagSecondImage = true;
                flagFirstImage = false;
                break;
            case R.id.iv_current_location:
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getActivity(), "No GPS", Toast.LENGTH_SHORT).show();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Utilities.getLocation(getActivity(), locationManager, parentView);
                    strLatitude = Utilities.getSharedPreferences(getActivity()).getString("lattitude", "");
                    strLongitude = Utilities.getSharedPreferences(getActivity()).getString("longitude", "");
                    flagLocation = true;

                }
                break;
            case R.id.iv_current_time:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                strCurrentDateandTime = sdf.format(new Date());
                ivCheckTime.setVisibility(View.VISIBLE);
                flagTime = true;

                break;
            case R.id.iv_authentication:
                ivCheckAuthentication.setVisibility(View.VISIBLE);
                flagAuthentication = true;
                break;
            case R.id.btn_flow_express_terminals_send:

                if (fileFirstImage == null) {
                    Toast.makeText(getActivity(), "Choose First Image", Toast.LENGTH_SHORT).show();
                } else if (fileSecondImage == null) {
                    Toast.makeText(getActivity(), "Choose Second Image", Toast.LENGTH_SHORT).show();
                } else if (etComments.getText().length() < 4) {
                    Toast.makeText(getActivity(), "Enter Comments", Toast.LENGTH_SHORT).show();
                } else if (!flagLocation) {
                    Toast.makeText(getActivity(), "Set Location", Toast.LENGTH_SHORT).show();
                } else if (!flagTime) {
                    Toast.makeText(getActivity(), "Set Time ", Toast.LENGTH_SHORT).show();
                } else if (!flagAuthentication) {
                    Toast.makeText(getActivity(), "Set Authentication", Toast.LENGTH_SHORT).show();
                } else {

                    new UploadFileToServer().execute();
                }


                break;


        }
    }

    private void fancydialo() {
        new FancyAlertDialog.Builder(getActivity())
                .setTitle("Rate us if you like the app")
                .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                .setMessage("Do you really want to Exit ?")
                .setNegativeBtnText("Gallery")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Camera")
                .setNegativeBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        intentCameraPic();
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        intentGalleryPic();
                    }
                })
                .build();
    }

    private void intentCameraPic() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);//zero can be replaced with any action code
    }

    private void intentGalleryPic() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {


            case 0://camera
                if (resultCode == RESULT_OK) {


                    Bitmap bm = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File sourceFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Pos/img.jpg");


                    FileOutputStream fo;
                    try {
                        sourceFile.createNewFile();
                        fo = new FileOutputStream(sourceFile);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (flagFirstImage) {

                        ivFirstImage.setImageBitmap(bm);
                        strFirstImage = sourceFile.getAbsolutePath().toString();
                        fileFirstImage = new File(strFirstImage);

                    }
                    if (flagSecondImage) {
                        ivSecondImage.setImageBitmap(bm);
                        strSecondImage = sourceFile.getAbsolutePath().toString();
                        fileSecondImage = new File(strSecondImage);
                    }


                } else {

                    Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();

                }
                break;


            case 1://gallery
                if (resultCode == RESULT_OK) {
                    image_uri = imageReturnedIntent.getData();
                    if (flagFirstImage) {

                        ivFirstImage.setImageURI(image_uri);
                        strFirstImage = getImagePath(image_uri);
                        fileFirstImage = new File(strFirstImage);


                    }
                    if (flagSecondImage) {
                        ivSecondImage.setImageURI(image_uri);
                        strSecondImage = getImagePath(image_uri);
                        fileSecondImage = new File(strFirstImage);
                    }


                } else {
                    Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }


    public String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            circularProgressView.startAnimation();

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(BASE_URL + "job_completed");
            try {
                HTTPMultiPartEntity entity = new HTTPMultiPartEntity(
                        new HTTPMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) 100) * 100));


                            }
                        });
                // Adding file data to http body
                // Extra parameters if you want to pass to server
                entity.addPart("image1", new FileBody(fileFirstImage));
                entity.addPart("image2", new FileBody(fileSecondImage));
                Looper.prepare();
                entity.addPart("api_token", new StringBody(strApiToken));
                entity.addPart("job_id", new StringBody("3"));
                entity.addPart("comment", new StringBody(etComments.getText().toString()));
                entity.addPart("latitude", new StringBody(strLatitude));
                entity.addPart("longitude", new StringBody(strLongitude));
                entity.addPart("current_time", new StringBody(strCurrentDateandTime));


                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                responseString = EntityUtils.toString(r_entity);

                Log.d("response string", responseString.toString());


                if (responseString.contains("true")) {
                    //alertDialogSpot.dismiss();
                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();

                } else if (responseString.contains("Distance is out of 200 km")) {
                    //circularProgressView.stopAnimation();
                    //Utilities.connectFragmentWithOutBackStack(getActivity(), new FlowExpreeTerminalsFragment());
                    //DialogUtils.fancyAlertDialog(getActivity());

                } else if (responseString.contains("false")) {
                    if (responseString.contains(""))
                        //  Utilities.connectFragmentWithOutBackStack(getActivity(), new FlowExpreeTerminalsFragment());
                        Toast.makeText(getActivity(), "Successful false", Toast.LENGTH_LONG).show();

                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
//                    pDialog.dismiss();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                responseString = e.toString();
//                    pDialog.dismiss();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }

            Log.d("zma return string", responseString);
            return responseString;

        }
    }


}
