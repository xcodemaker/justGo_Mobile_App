package com.dhammika_dev.justgo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        preferences = BaseApplication.getBaseApplication().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
    }

    @OnClick(R.id.button1)
    public void onClickPassengerView() {

        System.out.println("======================>>>>>>>>>>>>>>" + preferences.contains(IPreferencesKeys.USER_INFO) + "<<<<<<<<<<<<<<============================");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = (preferences.contains(IPreferencesKeys.ACCESS_TOKEN)) && retrieveObjectFromSharedPreferences().getUser_type().equals("passenger") ? new Intent(getApplicationContext(), PassengerHomeActivity.class) : new Intent(getApplicationContext(), AuthActivity.class);
                        intent.putExtra("selectedUserType", "passenger");
                        startActivity(intent);
//                        finish();
                    }
                });
            }
        }, 0);
    }

    @OnClick(R.id.button2)
    public void onClickStaffButton() {

//        System.out.println("======================>>>>>>>>>>>>>>"+retrieveObjectFromSharedPreferences().getUser_type()+"<<<<<<<<<<<<<<============================");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = (preferences.contains(IPreferencesKeys.ACCESS_TOKEN)) && (retrieveObjectFromSharedPreferences().getUser_type().equals("train_driver") || retrieveObjectFromSharedPreferences().getUser_type().equals("ticket_checker")) ? new Intent(getApplicationContext(), StaffHomeActivity.class) : new Intent(getApplicationContext(), AuthActivity.class);
                        intent.putExtra("selectedUserType", "train_driver");
                        startActivity(intent);
//                        finish();
                    }
                });
            }
        }, 0);

    }

    public LoginResponse retrieveObjectFromSharedPreferences() {
        Gson gson = new Gson();
        String json = preferences.getString(IPreferencesKeys.USER_INFO, null);
        return gson.fromJson(json, LoginResponse.class);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
