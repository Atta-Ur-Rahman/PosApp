package com.example.attaurrahman.pos.genrilUtils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.fragment.FlowExpreeTerminalsFragment;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by AttaUrRahman on 4/17/2018.
 */

public class DialogUtils {
    public static SweetAlertDialog sweetAlertDialog = null;
    public static void fancyAlertDialog(Activity activity){
        new FancyAlertDialog.Builder(activity)
                .setTitle("POS")
                .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                .setMessage("Distance is out of 200 km")

                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                .build();

    }



    public static SweetAlertDialog showProgressSweetDialog(Context context, String message) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#179e99"));
        sweetAlertDialog.setTitleText(message);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        return sweetAlertDialog;
    }

    public static SweetAlertDialog showMessageDialog(Context context, String message) {
        sweetAlertDialog.setTitleText(message);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        return sweetAlertDialog;
    }
}

