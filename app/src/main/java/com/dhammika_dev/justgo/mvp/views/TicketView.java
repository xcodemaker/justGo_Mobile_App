package com.dhammika_dev.justgo.mvp.views;

import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketPriceResponse;
import com.dhammika_dev.justgo.model.entities.response.ValidateTicketResponse;

public interface TicketView extends View {
    void showTicketPrice(TicketPriceResponse ticketPriceResponse);

    void showTicketCreated(CreateTicketResponse createTicketResponse);

    void showMyTickets(TicketListResponse ticketListResponse);

    void showValidateTicket(ValidateTicketResponse validateTicketResponse);
}