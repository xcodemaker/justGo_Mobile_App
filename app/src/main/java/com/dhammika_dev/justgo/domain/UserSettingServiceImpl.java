package com.dhammika_dev.justgo.domain;

import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class UserSettingServiceImpl implements UserSettingService {

    private JustGoAPIService hairbeautyService;

    public UserSettingServiceImpl(JustGoAPIService hairbeautyService) {
        super();
        this.hairbeautyService = hairbeautyService;
    }

    @Override
    public Observable<LogoutResponse> doLogoutService(String access_token) {
        return hairbeautyService.getApi()
                .doLogoutAPI(access_token)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
