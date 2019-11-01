package com.dhammika_dev.justgo.mvp.views;

import com.dhammika_dev.justgo.model.entities.response.TrainScheduleResponse;

public interface ScheduleView extends View {
    void showSearchTrainSchedule(TrainScheduleResponse trainScheduleResponse);
}