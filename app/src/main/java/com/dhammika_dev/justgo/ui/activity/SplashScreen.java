package com.dhammika_dev.justgo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.mvp.presenters.Presenter;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.dhammika_dev.justgo.utils.IScheduler;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashScreen extends BaseActivity {

    protected JustGoAPIService oacService;
    protected IScheduler scheduler;
    protected Presenter presenter;
    SharedPreferences preferences;
    @BindView(R.id.background)
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        scheduler = new AppScheduler();
        oacService = new JustGoAPIService();
        initializePresenter();
        if (presenter != null) presenter.onCreate();
        preferences = BaseApplication.getBaseApplication().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        }, 5000);

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
}
