package com.dhammika_dev.justgo.utils;

import rx.Scheduler;


public interface IScheduler {

    Scheduler mainThread();

    Scheduler backgroundThread();
}
