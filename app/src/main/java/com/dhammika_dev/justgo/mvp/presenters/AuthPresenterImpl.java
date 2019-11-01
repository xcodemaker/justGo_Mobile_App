package com.dhammika_dev.justgo.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.common.constants.ApplicationConstants;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.AuthService;
import com.dhammika_dev.justgo.domain.Service;
import com.dhammika_dev.justgo.model.entities.request.EmailValidateRequest;
import com.dhammika_dev.justgo.model.entities.request.LoginRequest;
import com.dhammika_dev.justgo.model.entities.request.PasswordResetRequest;
import com.dhammika_dev.justgo.model.entities.request.RequestTokenRequest;
import com.dhammika_dev.justgo.model.entities.response.EmailValidateResponse;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.model.entities.response.PasswordResetResponse;
import com.dhammika_dev.justgo.model.entities.response.RequestTokenResponse;
import com.dhammika_dev.justgo.model.entities.response.TokenVerifyResponse;
import com.dhammika_dev.justgo.model.rest.exception.RetrofitException;
import com.dhammika_dev.justgo.mvp.views.UserLoginView;
import com.dhammika_dev.justgo.mvp.views.UserRegisterView;
import com.dhammika_dev.justgo.mvp.views.View;
import com.dhammika_dev.justgo.utils.IScheduler;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

import static android.support.constraint.Constraints.TAG;

public class AuthPresenterImpl extends BasePresenter implements AuthPresenter {


    private UserLoginView mLoginAndRegisterView;
    private UserRegisterView userRegisterView;
    public AuthPresenterImpl(Activity activityContext, Service pService, IScheduler scheduler) {
        super(activityContext, pService, scheduler);
    }

    @Override
    public void doLogin(String contentType, String xRequestedWith, LoginRequest loginRequest) {
        subscription = doLoginObservable(contentType, loginRequest).subscribe(doLoginSubscriber());
    }

    @Override
    public void doEmailValidate(EmailValidateRequest emailValidateRequest) {
        subscription = doEmailValidateObservable(emailValidateRequest).subscribe(doEmailValidateSubscriber());
    }

    @Override
    public void doRegister(MultipartBody.Part profile_pic, RequestBody first_name, RequestBody last_name, RequestBody email, RequestBody address, RequestBody nic_or_passport, RequestBody contact_number, RequestBody user_type, RequestBody password, RequestBody password_confirmation) {
        subscription = doRegisterObservable(profile_pic, first_name, last_name, email, address, nic_or_passport, contact_number, user_type, password, password_confirmation)
                .subscribe(doRegisterSubscriber());
    }


    @Override
    public void requestResetToken(String contentType, String xRequestedWith, RequestTokenRequest requestTokenRequest) {
        subscription = requestResetTokenObservable(contentType, xRequestedWith, requestTokenRequest).subscribe(requestResetTokenSubscriber());
    }

    @Override
    public void verifyToken(String token) {
        subscription = requestVerifyTokenObservable(token).subscribe(requestVerifyTokenSubscriber());
    }

    @Override
    public void passwordReset(PasswordResetRequest requestTokenRequest) {
        subscription = passwordResetObservable(requestTokenRequest).subscribe(passwordResetSubscriber());
    }

    public Observable<PasswordResetResponse> passwordResetObservable(PasswordResetRequest requestTokenRequest) {
        try {
            return getService().passwordResetService(requestTokenRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observable<TokenVerifyResponse> requestVerifyTokenObservable(String token) {
        try {
            return getService().verifyTokenService(token)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Observable<RequestTokenResponse> requestResetTokenObservable(String contentType, String xRequestedWith, RequestTokenRequest requestTokenRequest) {
        try {
            return getService().requestResetTokenService(contentType, xRequestedWith, requestTokenRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Observable<LoginResponse> doLoginObservable(String contentType, LoginRequest loginRequest) {
        try {
            return getService().doLoginService(contentType, loginRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observable<LoginResponse> doRegisterObservable(MultipartBody.Part profile_pic, RequestBody first_name, RequestBody last_name, RequestBody email, RequestBody address, RequestBody nic_or_passport, RequestBody contact_number, RequestBody user_type, RequestBody password, RequestBody password_confirmation) {
        try {
            return getService().doRegisterService(profile_pic, first_name, last_name, email, address, nic_or_passport, contact_number, user_type, password, password_confirmation)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observable<EmailValidateResponse> doEmailValidateObservable(EmailValidateRequest emailValidateRequest) {
        try {
            return getService().doEmailValidate(emailValidateRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public DefaultSubscriber<PasswordResetResponse> passwordResetSubscriber() {
        return new DefaultSubscriber<PasswordResetResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.UNEXPECTED) {
                        PasswordResetResponse exceptionResponse = new PasswordResetResponse();
                        exceptionResponse.setMessage("unexpected error occurred!");
                        userRegisterView.ShowPasswordResetResponse(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.BADREQUEST) {
                        PasswordResetResponse exceptionResponse = new PasswordResetResponse();
                        exceptionResponse.setMessage("Server side error occurred");
                        userRegisterView.ShowPasswordResetResponse(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.NETWORK) {
                        PasswordResetResponse exceptionResponse = new PasswordResetResponse();
                        exceptionResponse.setMessage("please check your internet connection");
                        userRegisterView.ShowPasswordResetResponse(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.HTTP) {
                        PasswordResetResponse exceptionResponse = new PasswordResetResponse();
                        exceptionResponse.setMessage("Server side error occurred!");
                        userRegisterView.ShowPasswordResetResponse(exceptionResponse);
                    } else {
                        PasswordResetResponse response = error.getErrorBodyAs(PasswordResetResponse.class);
                        if (response == null) {
                            response = new PasswordResetResponse();
                            response.setMessage("Something went wrong try again later!");
                        } else {
                            response = new PasswordResetResponse();
                            response.setMessage("Something went wrong try again later!");
                        }
                        userRegisterView.ShowPasswordResetResponse(response);
                    }
                } catch (IOException ex) {
                    PasswordResetResponse exceptionResponse = new PasswordResetResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    userRegisterView.ShowPasswordResetResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(PasswordResetResponse response) {
                if (response != null) {
                    userRegisterView.ShowPasswordResetResponse(response);
                }
            }
        };
    }

    public DefaultSubscriber<TokenVerifyResponse> requestVerifyTokenSubscriber() {
        return new DefaultSubscriber<TokenVerifyResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.UNEXPECTED) {
                        TokenVerifyResponse exceptionResponse = new TokenVerifyResponse();
                        exceptionResponse.setMessage("unexpected error occurred!");
                        userRegisterView.showVerifyToken(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.BADREQUEST) {
                        TokenVerifyResponse exceptionResponse = new TokenVerifyResponse();
                        exceptionResponse.setMessage("Server side error occurred");
                        userRegisterView.showVerifyToken(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.NETWORK) {
                        TokenVerifyResponse exceptionResponse = new TokenVerifyResponse();
                        exceptionResponse.setMessage("please check your internet connection");
                        userRegisterView.showVerifyToken(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.HTTP) {
                        TokenVerifyResponse exceptionResponse = new TokenVerifyResponse();
                        exceptionResponse.setMessage("Server side error occurred!");
                        userRegisterView.showVerifyToken(exceptionResponse);
                    } else {
                        TokenVerifyResponse response = error.getErrorBodyAs(TokenVerifyResponse.class);
                        if (response == null) {
                            response = new TokenVerifyResponse();
                            response.setMessage("unexpected error occurred!");
                        } else {
                            response = new TokenVerifyResponse();
                            response.setMessage("Something went wrong try again later!");
                        }
                        userRegisterView.showVerifyToken(response);
                    }
                } catch (IOException ex) {
                    TokenVerifyResponse exceptionResponse = new TokenVerifyResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    userRegisterView.showVerifyToken(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(TokenVerifyResponse response) {
                if (response != null) {
                    userRegisterView.showVerifyToken(response);
                }
            }
        };
    }


    public DefaultSubscriber<LoginResponse> doLoginSubscriber() {
        return new DefaultSubscriber<LoginResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.UNEXPECTED) {
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setMessage("unexpected error occurred!");
                        mLoginAndRegisterView.showLogin(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.BADREQUEST) {
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setMessage("Server side error occurred");
                        mLoginAndRegisterView.showLogin(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.NETWORK) {
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setMessage("please check your internet connection");
                        mLoginAndRegisterView.showLogin(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.HTTP) {
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setMessage("Server side error occurred!");
                        mLoginAndRegisterView.showLogin(exceptionResponse);
                    } else {
                        LoginResponse response = error.getErrorBodyAs(LoginResponse.class);
                        if (response == null) {
                            response = new LoginResponse();
                            response.setMessage("unexpected error occurred!");
                        } else {
                            response = new LoginResponse();
                            response.setMessage("Something went wrong try again later!");
                        }
                        mLoginAndRegisterView.showLogin(response);
                    }
                } catch (IOException ex) {
                    LoginResponse exceptionResponse = new LoginResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mLoginAndRegisterView.showLogin(exceptionResponse);
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(LoginResponse response) {
                if (response != null) {
                    BaseApplication.getBaseApplication().clearData();
                    preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, response.getAccess_token()).apply();
                    mLoginAndRegisterView.showLogin(response);
                }
            }
        };
    }

    public DefaultSubscriber<LoginResponse> doRegisterSubscriber() {
        return new DefaultSubscriber<LoginResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.UNEXPECTED) {
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setMessage("unexpected error occurred!");
                        userRegisterView.showRegister(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.BADREQUEST) {
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setMessage("Server side error occurred");
                        userRegisterView.showRegister(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.NETWORK) {
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setMessage("please check your internet connection");
                        userRegisterView.showRegister(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.HTTP) {
                        LoginResponse exceptionResponse = new LoginResponse();
                        exceptionResponse.setMessage("Server side error occurred!");
                        userRegisterView.showRegister(exceptionResponse);
                    } else {
                        LoginResponse response = error.getErrorBodyAs(LoginResponse.class);
                        if (response == null) {
                            response = new LoginResponse();
                            response.setMessage("unexpected error occurred!");
                        } else {
                            response = new LoginResponse();
                            response.setMessage("Something went wrong try again later!");
                        }
                        userRegisterView.showRegister(response);
                    }
                } catch (IOException ex) {
                    LoginResponse exceptionResponse = new LoginResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    userRegisterView.showRegister(exceptionResponse);
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(LoginResponse response) {
                if (response != null) {
                    BaseApplication.getBaseApplication().clearData();
                    preferences.edit().putString(IPreferencesKeys.ACCESS_TOKEN, response.getAccess_token()).apply();
                    userRegisterView.showRegister(response);
                }
            }
        };
    }


    public DefaultSubscriber<RequestTokenResponse> requestResetTokenSubscriber() {
        return new DefaultSubscriber<RequestTokenResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.UNEXPECTED) {
                        RequestTokenResponse exceptionResponse = new RequestTokenResponse();
                        exceptionResponse.setMessage("unexpected error occurred!");
                        userRegisterView.showRequestToken(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.BADREQUEST) {
                        RequestTokenResponse exceptionResponse = new RequestTokenResponse();
                        exceptionResponse.setMessage("Server side error occurred");
                        userRegisterView.showRequestToken(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.NETWORK) {
                        RequestTokenResponse exceptionResponse = new RequestTokenResponse();
                        exceptionResponse.setMessage("please check your internet connection");
                        userRegisterView.showRequestToken(exceptionResponse);
                    } else if (error.getKind() == RetrofitException.Kind.HTTP) {
                        RequestTokenResponse exceptionResponse = new RequestTokenResponse();
                        exceptionResponse.setMessage("Server side error occurred!");
                        userRegisterView.showRequestToken(exceptionResponse);
                    } else {
                        RequestTokenResponse response = error.getErrorBodyAs(RequestTokenResponse.class);
                        if (response == null) {
                            response = new RequestTokenResponse();
                            response.setMessage("unexpected error occurred!");
                        } else {
                            response = new RequestTokenResponse();
                            response.setMessage("Something went wrong try again later!");
                        }
                        userRegisterView.showRequestToken(response);
                    }
                } catch (IOException ex) {
                    RequestTokenResponse exceptionResponse = new RequestTokenResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    userRegisterView.showRequestToken(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(RequestTokenResponse response) {
                if (response != null) {
                    userRegisterView.showRequestToken(response);
                }
            }
        };
    }

    public DefaultSubscriber<EmailValidateResponse> doEmailValidateSubscriber() {
        return new DefaultSubscriber<EmailValidateResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        EmailValidateResponse exceptionResponse = new EmailValidateResponse();
                        userRegisterView.showEmailValidation(exceptionResponse);
                    } else {
                        EmailValidateResponse response = error.getErrorBodyAs(EmailValidateResponse.class);
                        if (response == null) {
                            response = new EmailValidateResponse();
                        } else {
                        }
                        userRegisterView.showEmailValidation(response);
                    }
                } catch (IOException ex) {
                    EmailValidateResponse exceptionResponse = new EmailValidateResponse();
                    userRegisterView.showEmailValidation(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(EmailValidateResponse response) {
                if (response != null) {
                    userRegisterView.showEmailValidation(response);
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

    private AuthService getService() {
        return (AuthService) mService;
    }

    @Override
    public void attachView(View v) {
        if (v instanceof UserLoginView) {
            mLoginAndRegisterView = (UserLoginView) v;
            mView = mLoginAndRegisterView;
        } else if (v instanceof UserRegisterView) {
            userRegisterView = (UserRegisterView) v;
            mView = userRegisterView;
        }
    }
}
