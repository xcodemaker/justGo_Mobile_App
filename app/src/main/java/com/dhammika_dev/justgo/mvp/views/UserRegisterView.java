package com.dhammika_dev.justgo.mvp.views;

import com.dhammika_dev.justgo.model.entities.response.EmailValidateResponse;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.model.entities.response.PasswordResetResponse;
import com.dhammika_dev.justgo.model.entities.response.RequestTokenResponse;
import com.dhammika_dev.justgo.model.entities.response.TokenVerifyResponse;

public interface UserRegisterView extends View {
    void showEmailValidation(EmailValidateResponse emailValidateResponse);

    void showRegister(LoginResponse signUpResponse);

    void showRequestToken(RequestTokenResponse requestTokenResponse);

    void showVerifyToken(TokenVerifyResponse requestTokenResponse);

    void ShowPasswordResetResponse(PasswordResetResponse passwordResetResponse);
}
