package com.dhammika_dev.justgo.mvp.presenters;


import com.dhammika_dev.justgo.mvp.views.View;

public interface Presenter<T> {
    void onCreate();

    void onStart();

    void onStop();

    void onDestroy();

    void attachView(View v);
}
