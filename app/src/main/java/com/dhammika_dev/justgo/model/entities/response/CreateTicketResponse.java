package com.dhammika_dev.justgo.model.entities.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTicketResponse implements Serializable {

    public String ticket_id;
    public String message;
    public String qr_code;
    int statusCode;

    @Override
    public String toString() {
        return "CreateTicketResponse{" +
                "ticket_id='" + ticket_id + '\'' +
                ", message='" + message + '\'' +
                ", qr_code='" + qr_code + '\'' +
                '}';
    }
}