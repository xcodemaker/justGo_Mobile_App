package com.dhammika_dev.justgo.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.dhammika_dev.justgo.common.constants.ApplicationConstants;
import com.dhammika_dev.justgo.domain.ScheduleService;
import com.dhammika_dev.justgo.domain.Service;
import com.dhammika_dev.justgo.model.entities.response.TrainScheduleResponse;
import com.dhammika_dev.justgo.model.rest.exception.RetrofitException;
import com.dhammika_dev.justgo.mvp.views.ScheduleView;
import com.dhammika_dev.justgo.mvp.views.View;
import com.dhammika_dev.justgo.utils.IScheduler;

import java.io.IOException;

import rx.Observable;

import static android.support.constraint.Constraints.TAG;

public class SchedulePresenterImpl extends BasePresenter implements SchedulePresenter {


    private ScheduleView mScheduleView;

    public SchedulePresenterImpl(Activity activityContext, Service pService, IScheduler scheduler) {
        super(activityContext, pService, scheduler);
    }

    @Override
    public void doSearchTrainSchedule(String accessToken, String contentType, String startStationID, String endStationID, String searchDate, String startTime, String endTime, String lang) {
        subscription = doSearchTrainScheduleObservable(accessToken, contentType, startStationID, endStationID, searchDate, startTime, endTime, lang).subscribe(doSearchTrainScheduleSubscriber());
    }


    public Observable<TrainScheduleResponse> doSearchTrainScheduleObservable(String accessToken, String contentType, String startStationID, String endStationID, String searchDate, String startTime, String endTime, String lang) {
        try {
            return getService().doSearchTrainSchedule(accessToken, contentType, startStationID, endStationID, searchDate, startTime, endTime, lang)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public DefaultSubscriber<TrainScheduleResponse> doSearchTrainScheduleSubscriber() {
        return new DefaultSubscriber<TrainScheduleResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        TrainScheduleResponse exceptionResponse = new TrainScheduleResponse();
                        exceptionResponse.setMESSAGE(ApplicationConstants.EMPTY_DATA);
                        mScheduleView.showSearchTrainSchedule(exceptionResponse);
                    } else {
                        TrainScheduleResponse response = error.getErrorBodyAs(TrainScheduleResponse.class);
                        if (response == null) {
                            response = new TrainScheduleResponse();
                        } else {
                        }
                        mScheduleView.showSearchTrainSchedule(response);
                    }
                } catch (IOException ex) {
                    TrainScheduleResponse exceptionResponse = new TrainScheduleResponse();
                    exceptionResponse.setMESSAGE(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mScheduleView.showSearchTrainSchedule(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(TrainScheduleResponse response) {
                if (response != null) {
                    mScheduleView.showSearchTrainSchedule(response);
                }
            }
        };
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    private ScheduleService getService() {
        return (ScheduleService) mService;
    }

    @Override
    public void attachView(View v) {
        if (v instanceof ScheduleView) {
            mScheduleView = (ScheduleView) v;
            mView = mScheduleView;
        }
    }


}