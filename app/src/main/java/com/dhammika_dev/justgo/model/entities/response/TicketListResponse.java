package com.dhammika_dev.justgo.model.entities.response;

import com.dhammika_dev.justgo.dto.Ticket;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketListResponse {
    public List<Ticket> tickets = null;
    public String message;
    int statusCode;

    @Override
    public String toString() {
        return "TicketListResponse{" +
                "tickets=" + tickets +
                ", message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}

