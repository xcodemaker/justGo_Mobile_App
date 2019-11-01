package com.dhammika_dev.justgo.mvp.presenters;

public interface SchedulePresenter extends Presenter {
    void doSearchTrainSchedule(String accessToken,
                               String contentType,
                               String startStationID,
                               String endStationID,
                               String searchDate,
                               String startTime,
                               String endTime,
                               String lang);
}
