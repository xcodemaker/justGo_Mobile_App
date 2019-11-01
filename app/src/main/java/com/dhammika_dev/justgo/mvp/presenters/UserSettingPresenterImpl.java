package com.dhammika_dev.justgo.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.common.constants.ApplicationConstants;
import com.dhammika_dev.justgo.domain.Service;
import com.dhammika_dev.justgo.domain.UserSettingService;
import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.rest.exception.RetrofitException;
import com.dhammika_dev.justgo.mvp.views.UserLoginView;
import com.dhammika_dev.justgo.mvp.views.UserSettingView;
import com.dhammika_dev.justgo.mvp.views.View;
import com.dhammika_dev.justgo.utils.IScheduler;

import java.io.IOException;

import rx.Observable;

import static android.support.constraint.Constraints.TAG;

public class UserSettingPresenterImpl extends BasePresenter implements UserSettingPresenter {


    private UserSettingView mLoginAndRegisterView;
    private UserLoginView getmLoginAndRegisterView;
    public UserSettingPresenterImpl(Activity activityContext, Service pService, IScheduler scheduler) {
        super(activityContext, pService, scheduler);
    }

    @Override
    public void doLogout(String access_token) {
        subscription = doLogoutObservable(access_token).subscribe(doLogoutSubscriber());
    }

    public Observable<LogoutResponse> doLogoutObservable(String access_token) {
        try {
            return getService().doLogoutService(access_token)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public DefaultSubscriber<LogoutResponse> doLogoutSubscriber() {
        return new DefaultSubscriber<LogoutResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        LogoutResponse exceptionResponse = new LogoutResponse();
                        exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_TOKEN_EXPIRED);
                        mLoginAndRegisterView.showLogout(exceptionResponse);
                    } else {
                        LogoutResponse response = error.getErrorBodyAs(LogoutResponse.class);
                        if (response == null) {
                            response = new LogoutResponse();
                        } else {
                        }
                        mLoginAndRegisterView.showLogout(response);
                    }
                } catch (IOException ex) {
                    LogoutResponse exceptionResponse = new LogoutResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mLoginAndRegisterView.showLogout(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(LogoutResponse response) {
                if (response != null) {
                    BaseApplication.getBaseApplication().clearData();
                    mLoginAndRegisterView.showLogout(response);
                }
            }
        };
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
    public void onDestroy() {

    }

    private UserSettingService getService() {
        return (UserSettingService) mService;
    }

    @Override
    public void attachView(View v) {
        if (v instanceof UserSettingView) {
            mLoginAndRegisterView = (UserSettingView) v;
            mView = mLoginAndRegisterView;
        }
    }
}
