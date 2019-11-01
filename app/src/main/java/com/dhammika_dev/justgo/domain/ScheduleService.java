package com.dhammika_dev.justgo.domain;

import com.dhammika_dev.justgo.model.entities.response.TrainScheduleResponse;

import rx.Observable;

public interface ScheduleService extends Service {
    Observable<TrainScheduleResponse> doSearchTrainSchedule(String accessToken,
                                                            String contentType,
                                                            String startStationID,
                                                            String endStationID,
                                                            String searchDate,
                                                            String startTime,
                                                            String endTime,
                                                            String lang);
}
