package com.dhammika_dev.justgo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Train {
    public String id;
    public String train_id;
    public String train_no;
    public String arrival_time;
    public String departur_time;
    public String source;
    public String destination;
    public Object train_name;
    public String train_type;
    public String train_frequency;

    @Override
    public String toString() {
        return "Train{" +
                "id='" + id + '\'' +
                ", train_id='" + train_id + '\'' +
                ", train_no='" + train_no + '\'' +
                ", arrival_time='" + arrival_time + '\'' +
                ", departur_time='" + departur_time + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", train_name=" + train_name +
                ", train_type='" + train_type + '\'' +
                ", train_frequency='" + train_frequency + '\'' +
                '}';
    }
}
