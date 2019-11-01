package com.dhammika_dev.justgo.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.common.constants.ApplicationConstants;
import com.dhammika_dev.justgo.domain.ProfileViewService;
import com.dhammika_dev.justgo.domain.Service;
import com.dhammika_dev.justgo.model.entities.request.UpdateUserDetailsRequest;
import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.entities.response.UserDetailsResponse;
import com.dhammika_dev.justgo.model.rest.exception.RetrofitException;
import com.dhammika_dev.justgo.mvp.views.ProfileView;
import com.dhammika_dev.justgo.mvp.views.View;
import com.dhammika_dev.justgo.utils.IScheduler;

import java.io.IOException;

import okhttp3.MultipartBody;
import rx.Observable;

import static android.support.constraint.Constraints.TAG;

public class ProfileViewPresenterImpl extends BasePresenter implements ProfileViewPresenter {

    private ProfileView mProfileView;

    public ProfileViewPresenterImpl(Activity activityContext, Service pService, IScheduler scheduler) {
        super(activityContext, pService, scheduler);
    }

    @Override
    public void getUserDetails(String access_token) {
        subscription = getUserDetailsObservable(access_token).subscribe(getuserdetailssubscriber());
    }


    public Observable<UserDetailsResponse> getUserDetailsObservable(String access_token) {
        try {
            return getService().getUSerDetailsService(access_token)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public DefaultSubscriber<UserDetailsResponse> getuserdetailssubscriber() {
        return new DefaultSubscriber<UserDetailsResponse>(this.mView) {

            @Override
            public void onNext(UserDetailsResponse response) {
                if (response != null) {
                    response.setStatusCode(200);
                    mProfileView.showProfile(response);
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        UserDetailsResponse exceptionResponse = new UserDetailsResponse();
                        exceptionResponse.setMessage("Your session has expired. Please login to pickup where you left off.");
                        exceptionResponse.setStatusCode(401);
                        mProfileView.showProfile(exceptionResponse);
                    } else {
                        UserDetailsResponse response = error.getErrorBodyAs(UserDetailsResponse.class);
                        if (response == null) {
                            response = new UserDetailsResponse();
                        }
                        response.setStatusCode(400);
                        response.setMessage("server side error occurred");
                        mProfileView.showProfile(response);
                    }
                } catch (IOException ex) {
                    UserDetailsResponse exceptionResponse = new UserDetailsResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    exceptionResponse.setStatusCode(400);
                    mProfileView.showProfile(exceptionResponse);
                    ex.printStackTrace();
                }
            }


        };
    }

    @Override
    public void doLogout(String access_token) {
        subscription = doLogoutObservable(access_token).subscribe(doLogoutSubscriber());
    }

    @Override
    public void doUpdateUserProfileImage(String authorization, MultipartBody.Part profile_pic) {
        subscription = doUpdateUserProfileImageObservable(authorization, profile_pic).subscribe(doUpdateUserProfileImageSubscriber());
    }

    @Override
    public void doUpdateProfileDetails(String contentType, String token, UpdateUserDetailsRequest updateUserDetailsRequest) {
        subscription = doUpdateUserProfileDetailsObservable(contentType, token, updateUserDetailsRequest).subscribe(doUpdateUserProfileDetailsSubscriber());
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

    public Observable<UserDetailsResponse> doUpdateUserProfileImageObservable(String authorization, MultipartBody.Part profile_pic) {
        try {
            return getService().doUpdateUserProfileImage(authorization, profile_pic)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observable<UserDetailsResponse> doUpdateUserProfileDetailsObservable(String contentType, String token, UpdateUserDetailsRequest updateUserDetailsRequest) {
        try {
            return getService().doUpdateProfileDetails(contentType, token, updateUserDetailsRequest)
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
                        mProfileView.showLogout(exceptionResponse);
                    } else {
                        LogoutResponse response = error.getErrorBodyAs(LogoutResponse.class);
                        if (response == null) {
                            response = new LogoutResponse();
                        } else {
                        }
                        mProfileView.showLogout(response);
                    }
                } catch (IOException ex) {
                    LogoutResponse exceptionResponse = new LogoutResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mProfileView.showLogout(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(LogoutResponse response) {
                if (response != null) {
                    BaseApplication.getBaseApplication().clearData();
                    mProfileView.showLogout(response);
                }
            }
        };
    }

    public DefaultSubscriber<UserDetailsResponse> doUpdateUserProfileImageSubscriber() {
        return new DefaultSubscriber<UserDetailsResponse>(this.mView) {


            @Override
            public void onNext(UserDetailsResponse response) {
                if (response != null) {
                    response.setStatusCode(200);
                    mProfileView.showProfileImageUpdate(response);
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        UserDetailsResponse exceptionResponse = new UserDetailsResponse();
                        exceptionResponse.setMessage("Your session has expired. Please login to pickup where you left off.");
                        exceptionResponse.setStatusCode(401);
                        mProfileView.showProfileImageUpdate(exceptionResponse);
                    } else {
                        UserDetailsResponse response = error.getErrorBodyAs(UserDetailsResponse.class);
                        if (response == null) {
                            response = new UserDetailsResponse();
                        }
                        response.setStatusCode(400);
                        response.setMessage("server side error occurred");
                        mProfileView.showProfileImageUpdate(response);
                    }
                } catch (IOException ex) {
                    UserDetailsResponse exceptionResponse = new UserDetailsResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    exceptionResponse.setStatusCode(400);
                    mProfileView.showProfileImageUpdate(exceptionResponse);
                    ex.printStackTrace();
                }
            }


        };
    }

    public DefaultSubscriber<UserDetailsResponse> doUpdateUserProfileDetailsSubscriber() {
        return new DefaultSubscriber<UserDetailsResponse>(this.mView) {


            @Override
            public void onNext(UserDetailsResponse response) {
                if (response != null) {
                    response.setStatusCode(200);
                    mProfileView.showProfileDetailsUpdate(response);
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        UserDetailsResponse exceptionResponse = new UserDetailsResponse();
                        exceptionResponse.setMessage("Your session has expired. Please login to pickup where you left off.");
                        exceptionResponse.setStatusCode(401);
                        mProfileView.showProfileDetailsUpdate(exceptionResponse);
                    } else {
                        UserDetailsResponse response = error.getErrorBodyAs(UserDetailsResponse.class);
                        if (response == null) {
                            response = new UserDetailsResponse();
                        }
                        response.setStatusCode(400);
                        response.setMessage("server side error occurred");
                        mProfileView.showProfileDetailsUpdate(response);
                    }
                } catch (IOException ex) {
                    UserDetailsResponse exceptionResponse = new UserDetailsResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    exceptionResponse.setStatusCode(400);
                    mProfileView.showProfileDetailsUpdate(exceptionResponse);
                    ex.printStackTrace();
                }
            }


        };
    }


    private ProfileViewService getService() {
        return (ProfileViewService) mService;
    }

    @Override
    public void attachView(View v) {
        if (v instanceof ProfileView) {
            mProfileView = (ProfileView) v;
            mView = mProfileView;
        }
    }
}
