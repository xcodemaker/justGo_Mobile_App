package com.dhammika_dev.justgo.domain;

import com.dhammika_dev.justgo.model.entities.response.TrainScheduleResponse;
import com.dhammika_dev.justgo.model.rest.LankagateAPIService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class ScheduleServiceImpl implements ScheduleService {

    private LankagateAPIService lankagateAPIService;

    public ScheduleServiceImpl(LankagateAPIService lankagateAPIService) {
        super();
        this.lankagateAPIService = lankagateAPIService;
    }

    @Override
    public Observable<TrainScheduleResponse> doSearchTrainSchedule(String accessToken, String contentType, String startStationID, String endStationID, String searchDate, String startTime, String endTime, String lang) {
        return lankagateAPIService.getApi()
                .getTrainSchedule(accessToken, contentType, startStationID, endStationID, searchDate, startTime, endTime, lang)
                .observeOn(AndroidSchedulers.mainThread());
    }

}
