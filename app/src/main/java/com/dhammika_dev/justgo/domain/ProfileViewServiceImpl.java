package com.dhammika_dev.justgo.domain;

import com.dhammika_dev.justgo.model.entities.request.UpdateUserDetailsRequest;
import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.entities.response.UserDetailsResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class ProfileViewServiceImpl implements ProfileViewService {
    private JustGoAPIService justGoAPIService;

    public ProfileViewServiceImpl(JustGoAPIService justGoAPIService) {
        super();
        this.justGoAPIService = justGoAPIService;
    }

    @Override
    public Observable<UserDetailsResponse> getUSerDetailsService(String access_token) {
        return justGoAPIService.getApi()
                .getUserDetails(access_token)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<LogoutResponse> doLogoutService(String access_token) {
        return justGoAPIService.getApi()
                .doLogoutAPI(access_token)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserDetailsResponse> doUpdateUserProfileImage(String authorization, MultipartBody.Part profile_pic) {
        return justGoAPIService.getApi()
                .updateUserProfileImage(authorization, profile_pic)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserDetailsResponse> doUpdateProfileDetails(String contentType, String token, UpdateUserDetailsRequest updateUserDetailsRequest) {
        return justGoAPIService.getApi()
                .updateUserDetails(contentType, token, updateUserDetailsRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

}
