package com.eunwon.duri.dialog;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.eunwon.duri.R;
import com.eunwon.duri.activity.LogInActivity;

public class LogoutDialog {
    private Context context;
    private Activity activity;
    Button logoutButton;
    Button cancelButton;

    public LogoutDialog(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void show() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.show();

        logoutButton = dialog.findViewById(R.id.logout_logout_btn);
        cancelButton = dialog.findViewById(R.id.logout_cancel_btn);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = activity.getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putBoolean("isLogIn", false);
                editor.commit();

                Intent intent = new Intent(activity, LogInActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }
}
