package com.dhammika_dev.justgo.mvp.views;

import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.entities.response.UserDetailsResponse;

public interface ProfileView extends View {
    void showProfile(UserDetailsResponse userDetailsResponse);

    void showLogout(LogoutResponse logoutResponse);

    void showProfileImageUpdate(UserDetailsResponse userDetailsResponse);

    void showProfileDetailsUpdate(UserDetailsResponse userDetailsResponse);
}
