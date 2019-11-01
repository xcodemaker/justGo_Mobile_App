package com.dhammika_dev.justgo.model.entities.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTicketRequest implements Serializable {
    String price;
    String classType;
    String date;
    String distance;
    String time;
    String source;
    String destination;
    String train_id;
    String train_no;
    String arrival_time;
    String departur_time;
    String train_name;
    String train_type;
    String train_frequency;

    @Override
    public String toString() {
        return "CreateTicketRequest{" +
                "price='" + price + '\'' +
                ", classType='" + classType + '\'' +
                ", date='" + date + '\'' +
                ", distance='" + distance + '\'' +
                ", time='" + time + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", train_id='" + train_id + '\'' +
                ", train_no='" + train_no + '\'' +
                ", arrival_time='" + arrival_time + '\'' +
                ", departur_time='" + departur_time + '\'' +
                ", train_name='" + train_name + '\'' +
                ", train_type='" + train_type + '\'' +
                ", train_frequency='" + train_frequency + '\'' +
                '}';
    }
}
