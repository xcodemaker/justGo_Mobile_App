package com.dhammika_dev.justgo.domain;

import com.dhammika_dev.justgo.model.entities.request.CreateTicketRequest;
import com.dhammika_dev.justgo.model.entities.request.ValidateTicketRequest;
import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketPriceResponse;
import com.dhammika_dev.justgo.model.entities.response.ValidateTicketResponse;

import rx.Observable;

public interface TicketService extends Service {
    Observable<TicketPriceResponse> getTicketPrice(String accessToken,
                                                   String contentType,
                                                   String startStationID,
                                                   String endStationID,
                                                   String lang);

    Observable<CreateTicketResponse> createTicket(String contentType, String token, CreateTicketRequest createTicketRequest);

    Observable<TicketListResponse> getMyTickets(String contentType, String token);

    Observable<ValidateTicketResponse> validateTicket(String contentType, String token, ValidateTicketRequest validateTicketRequest);
}
