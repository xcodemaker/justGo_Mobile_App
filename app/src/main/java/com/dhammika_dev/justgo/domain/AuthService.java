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

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public interface AuthService extends Service {
    Observable<LoginResponse> doLoginService(String contentType, LoginRequest loginRequest);

    Observable<EmailValidateResponse> doEmailValidate(EmailValidateRequest emailValidateRequest);

    Observable<LoginResponse> doRegisterService(MultipartBody.Part profile_pic,
                                                RequestBody first_name,
                                                RequestBody last_name,
                                                RequestBody email,
                                                RequestBody address,
                                                RequestBody nic_or_passport,
                                                RequestBody contact_number,
                                                RequestBody user_type,
                                                RequestBody password,
                                                RequestBody password_confirmation);

    Observable<RequestTokenResponse> requestResetTokenService(
            String contentType,
            String xRequestedWith,
            RequestTokenRequest requestTokenRequest);

    Observable<TokenVerifyResponse> verifyTokenService(String token);

    Observable<PasswordResetResponse> passwordResetService(
            PasswordResetRequest requestTokenRequest);

}
