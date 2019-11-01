package com.dhammika_dev.justgo.domain;

import com.dhammika_dev.justgo.model.entities.request.EmailValidateRequest;
import com.dhammika_dev.justgo.model.entities.request.LoginRequest;
import com.dhammika_dev.justgo.model.entities.request.PasswordResetRequest;
import com.dhammika_dev.justgo.model.entities.request.RequestTokenRequest;
import com.dhammika_dev.justgo.model.entities.response.EmailValidateResponse;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.model.entities.response.PasswordResetResponse;
import com.dhammika_dev.justgo.model.entities.response.RequestTokenResponse;
import com.dhammika_dev.justgo.model.entities.response.TokenVerifyResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class AuthServiceImpl implements AuthService {

    private JustGoAPIService justGoService;

    public AuthServiceImpl(JustGoAPIService hairbeautyService) {
        super();
        this.justGoService = hairbeautyService;
    }

    @Override
    public Observable<LoginResponse> doLoginService(String contentType, LoginRequest loginRequest) {
        return justGoService.getApi()
                .doLoginAPI(contentType, loginRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<EmailValidateResponse> doEmailValidate(EmailValidateRequest emailValidateRequest) {
        return justGoService.getApi()
                .doEmailValidate(emailValidateRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<LoginResponse> doRegisterService(MultipartBody.Part profile_pic, RequestBody first_name, RequestBody last_name, RequestBody email, RequestBody address, RequestBody nic_or_passport, RequestBody contact_number, RequestBody user_type, RequestBody password, RequestBody password_confirmation) {
        return justGoService.getApi()
                .doRegister(profile_pic, first_name, last_name, email, address, nic_or_passport, contact_number, user_type, password, password_confirmation)
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<RequestTokenResponse> requestResetTokenService(String contentType, String xRequestedWith, RequestTokenRequest requestTokenRequest) {
        return justGoService.getApi()
                .requestResetToken(contentType, xRequestedWith, requestTokenRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<TokenVerifyResponse> verifyTokenService(String token) {
        return justGoService.getApi()
                .verifyToken(token)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PasswordResetResponse> passwordResetService(PasswordResetRequest requestTokenRequest) {
        return justGoService.getApi()
                .passwordReset(requestTokenRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }


}
