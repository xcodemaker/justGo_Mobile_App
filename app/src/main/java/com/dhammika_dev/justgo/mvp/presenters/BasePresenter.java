package com.dhammika_dev.justgo.mvp.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.Service;
import com.dhammika_dev.justgo.mvp.views.View;
import com.dhammika_dev.justgo.utils.IScheduler;

import rx.Subscription;
import rx.subscriptions.Subscriptions;


public abstract class BasePresenter implements Presenter {
    protected Activity activity;
    protected Service mService;
    protected Subscription subscription = (Subscription) Subscriptions.empty();
    protected Subscription subscriptionCreate = (Subscription) Subscriptions.empty();
    protected Subscription subscriptionDelete = (Subscription) Subscriptions.empty();

    protected IScheduler scheduler;
    protected View mView;

    protected SharedPreferences preferences;
    private String access_token;


    protected BasePresenter(Activity activityContext, Service pService, IScheduler scheduler) {
        this.activity = activityContext;
        this.mService = pService;
        this.scheduler = scheduler;

        this.preferences = activityContext.getSharedPreferences(activityContext.getPackageName(), Context.MODE_PRIVATE);
        this.access_token = preferences.getString(IPreferencesKeys.ACCESS_TOKEN, "");

    }

    public void unSubscribe(Subscription subscription) {
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public String getAccessToken() {
        if (access_token == null || access_token.equals("")) {
            Context mContext = BaseApplication.getBaseApplication();
            SharedPreferences preferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
            return preferences.getString(IPreferencesKeys.ACCESS_TOKEN, "");
        } else {
            return access_token;
        }
    }


    @Override
    public void onCreate() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void attachView(View v) {
        mView = v;
    }

    @Override
    public void onDestroy() {
        unSubscribe(subscription);
        unSubscribe(subscriptionCreate);
        unSubscribe(subscriptionDelete);
    }
}
