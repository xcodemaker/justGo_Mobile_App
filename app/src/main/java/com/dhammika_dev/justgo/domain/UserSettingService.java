package com.dhammika_dev.justgo.domain;

import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;

import rx.Observable;

public interface UserSettingService extends Service {
    Observable<LogoutResponse> doLogoutService(String access_token);
}
