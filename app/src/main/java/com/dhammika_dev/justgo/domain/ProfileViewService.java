package com.dhammika_dev.justgo.domain;

import com.dhammika_dev.justgo.model.entities.request.UpdateUserDetailsRequest;
import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.entities.response.UserDetailsResponse;

import okhttp3.MultipartBody;
import rx.Observable;

public interface ProfileViewService extends Service {
    Observable<UserDetailsResponse> getUSerDetailsService(String access_token);

    Observable<LogoutResponse> doLogoutService(String access_token);

    Observable<UserDetailsResponse> doUpdateUserProfileImage(String authorization, MultipartBody.Part profile_pic);

    Observable<UserDetailsResponse> doUpdateProfileDetails(String contentType,
                                                           String token,
                                                           UpdateUserDetailsRequest updateUserDetailsRequest);
}

