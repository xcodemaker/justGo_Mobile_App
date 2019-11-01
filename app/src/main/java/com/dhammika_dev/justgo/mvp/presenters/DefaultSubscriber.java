package com.dhammika_dev.justgo.mvp.presenters;

import android.util.Log;

import com.dhammika_dev.justgo.mvp.views.View;

import rx.Subscriber;

public class DefaultSubscriber<T> extends Subscriber<T> {
    private String TAG = "DefaultSubscriber";
    private View mView;

    public DefaultSubscriber(View pView) {
        mView = pView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable throwable) {
        if (mView != null) {
            //mView.showMessage(throwable.getLocalizedMessage());
        }
        throwable.printStackTrace();
        Log.e(TAG, "Error Occurred while retrieving List: " + throwable.getStackTrace());
    }

    @Override
    public void onNext(T t) {
    }
}
