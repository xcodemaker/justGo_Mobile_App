package com.dhammika_dev.justgo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScheduleList {
    private String train;
    private String arrival;
    private String departure;
    private String type;
    private String frequency;
    private Boolean isRunning;
    private String origin;
    private String destination;
}
