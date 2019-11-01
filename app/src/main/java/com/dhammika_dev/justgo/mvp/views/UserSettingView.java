package com.dhammika_dev.justgo.mvp.views;

import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;

public interface UserSettingView extends View {
    void showLogout(LogoutResponse logoutResponse);
}
