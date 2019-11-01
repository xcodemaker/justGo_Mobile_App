package com.dhammika_dev.justgo.mvp.presenters;

import com.dhammika_dev.justgo.model.entities.request.EmailValidateRequest;
import com.dhammika_dev.justgo.model.entities.request.LoginRequest;
import com.dhammika_dev.justgo.model.entities.request.PasswordResetRequest;
import com.dhammika_dev.justgo.model.entities.request.RequestTokenRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface AuthPresenter extends Presenter {
    void doLogin(String contentType, String xRequestedWith, LoginRequest loginRequest);

    void doEmailValidate(EmailValidateRequest emailValidateRequest);

    void doRegister(MultipartBody.Part profile_pic,
                    RequestBody first_name,
                    RequestBody last_name,
                    RequestBody email,
                    RequestBody address,
                    RequestBody nic_or_passport,
                    RequestBody contact_number,
                    RequestBody user_type,
                    RequestBody password,
                    RequestBody password_confirmation);

    void requestResetToken(
            String contentType,
            String xRequestedWith,
            RequestTokenRequest requestTokenRequest);

    void verifyToken(String token);

    void passwordReset(
            PasswordResetRequest requestTokenRequest);

}
