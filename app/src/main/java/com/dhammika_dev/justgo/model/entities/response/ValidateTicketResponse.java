package com.dhammika_dev.justgo.model.entities.response;

import com.dhammika_dev.justgo.dto.Ticket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTicketResponse {
    public Ticket ticket = null;
    public String message;
    int statusCode;

    @Override
    public String toString() {
        return "TicketListResponse{" +
                "tickets=" + ticket +
                ", message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}

