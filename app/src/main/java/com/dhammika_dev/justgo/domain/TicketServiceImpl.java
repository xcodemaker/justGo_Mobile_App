package com.dhammika_dev.justgo.domain;

import com.dhammika_dev.justgo.model.entities.request.CreateTicketRequest;
import com.dhammika_dev.justgo.model.entities.request.ValidateTicketRequest;
import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketPriceResponse;
import com.dhammika_dev.justgo.model.entities.response.ValidateTicketResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.model.rest.LankagateAPIService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class TicketServiceImpl implements TicketService {

    private LankagateAPIService lankagateAPIService;
    private JustGoAPIService justGoAPIService;

    public TicketServiceImpl(LankagateAPIService lankagateAPIService, JustGoAPIService justGoAPIService) {
        super();
        this.lankagateAPIService = lankagateAPIService;
        this.justGoAPIService = justGoAPIService;
    }

    @Override
    public Observable<TicketPriceResponse> getTicketPrice(String accessToken, String contentType, String startStationID, String endStationID, String lang) {
        return lankagateAPIService.getApi()
                .getTicketPrice(accessToken, contentType, startStationID, endStationID, lang)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CreateTicketResponse> createTicket(String contentType, String token, CreateTicketRequest createTicketRequest) {
        return justGoAPIService.getApi()
                .createTicket(contentType, token, createTicketRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<TicketListResponse> getMyTickets(String contentType, String token) {
        return justGoAPIService.getApi()
                .getMyTickets(contentType, token)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ValidateTicketResponse> validateTicket(String contentType, String token, ValidateTicketRequest validateTicketRequest) {
        return justGoAPIService.getApi()
                .validateTicket(contentType, token, validateTicketRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }

}
