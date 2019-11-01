package com.dhammika_dev.justgo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {
    public String id;
    public String train_id;
    public String ticket_details_id;
    public Train train;
    public TicketDetails ticket_details;

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", trainId='" + train_id + '\'' +
                ", ticketDetailsId='" + ticket_details_id + '\'' +
                ", train=" + train.toString() +
                ", ticketDetails=" + ticket_details.toString() +
                '}';
    }
}
