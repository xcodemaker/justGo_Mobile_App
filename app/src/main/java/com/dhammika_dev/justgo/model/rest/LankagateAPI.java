package com.dhammika_dev.justgo.model.rest;


import com.dhammika_dev.justgo.model.entities.response.TicketPriceResponse;
import com.dhammika_dev.justgo.model.entities.response.TrainScheduleResponse;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;


public interface LankagateAPI {

    @GET("train/searchTrain")
    Observable<TrainScheduleResponse> getTrainSchedule(
            @Header("Authorization") String accessToken,
            @Header("Accept") String contentType,
            @Query("startStationID") String startStationID,
            @Query("endStationID") String endStationID,
            @Query("searchDate") String searchDate,
            @Query("startTime") String startTime,
            @Query("endTime") String endTime,
            @Query("lang") String lang);

    @GET("ticket/getPrice")
    Observable<TicketPriceResponse> getTicketPrice(
            @Header("Authorization") String accessToken,
            @Header("Accept") String contentType,
            @Query("startStationID") String startStationID,
            @Query("endStationID") String endStationID,
            @Query("lang") String lang);
}
