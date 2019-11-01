package com.dhammika_dev.justgo.mvp.views;

import com.dhammika_dev.justgo.model.entities.response.LoginResponse;

public interface UserLoginView extends View {

    void showLogin(LoginResponse loginResponse);

}
