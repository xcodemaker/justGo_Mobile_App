package com.dhammika_dev.justgo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDetails {
    public String id;
    public String price;
    public String class_type;
    public String date;
    public String distance;
    public String qr_code;
    public Object time;
    public String source;
    public String destination;
}
