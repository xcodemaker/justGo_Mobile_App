package com.dhammika_dev.justgo.mvp.presenters;

import com.dhammika_dev.justgo.model.entities.request.CreateTicketRequest;
import com.dhammika_dev.justgo.model.entities.request.ValidateTicketRequest;

public interface TicketPresenter extends Presenter {
    void getTicketPrice(String accessToken,
                        String contentType,
                        String startStationID,
                        String endStationID,
                        String lang);

    void createTicket(String contentType, String token, CreateTicketRequest createTicketRequest);

    void getMyTickets(String contentType, String token);

    void validateTicket(String contentType, String token, ValidateTicketRequest validateTicketRequest);
}
