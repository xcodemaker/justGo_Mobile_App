package com.dhammika_dev.justgo.model.entities.response;

import com.dhammika_dev.justgo.dto.lankagate.PriceListResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketPriceResponse {
    public PriceListResult RESULTS;
    public String MESSAGE;
    public boolean SUCCESS;
    public int NOFRESULTS;
    public String STATUSCODE;

    @Override
    public String toString() {
        return "TicketPriceResponse{" +
                "RESULTS=" + RESULTS +
                ", MESSAGE='" + MESSAGE + '\'' +
                ", SUCCESS=" + SUCCESS +
                ", NOFRESULTS=" + NOFRESULTS +
                ", STATUSCODE='" + STATUSCODE + '\'' +
                '}';
    }
}
