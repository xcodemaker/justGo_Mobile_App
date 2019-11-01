package com.dhammika_dev.justgo.mvp.presenters;

import com.dhammika_dev.justgo.model.entities.request.UpdateUserDetailsRequest;

import okhttp3.MultipartBody;

public interface ProfileViewPresenter {
    void getUserDetails(String access_token);

    void doLogout(String access_token);

    void doUpdateUserProfileImage(String authorization, MultipartBody.Part profile_pic);

    void doUpdateProfileDetails(String contentType,
                                String token,
                                UpdateUserDetailsRequest updateUserDetailsRequest);
}
