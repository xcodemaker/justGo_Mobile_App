package com.dhammika_dev.justgo.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.dhammika_dev.justgo.common.constants.ApplicationConstants;
import com.dhammika_dev.justgo.domain.Service;
import com.dhammika_dev.justgo.domain.TicketService;
import com.dhammika_dev.justgo.model.entities.request.CreateTicketRequest;
import com.dhammika_dev.justgo.model.entities.request.ValidateTicketRequest;
import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketPriceResponse;
import com.dhammika_dev.justgo.model.entities.response.ValidateTicketResponse;
import com.dhammika_dev.justgo.model.rest.exception.RetrofitException;
import com.dhammika_dev.justgo.mvp.views.TicketView;
import com.dhammika_dev.justgo.mvp.views.View;
import com.dhammika_dev.justgo.utils.IScheduler;

import java.io.IOException;

import rx.Observable;

import static android.support.constraint.Constraints.TAG;

public class TicketPresenterImpl extends BasePresenter implements TicketPresenter {


    private TicketView ticketView;

    public TicketPresenterImpl(Activity activityContext, Service pService, IScheduler scheduler) {
        super(activityContext, pService, scheduler);
    }

    @Override
    public void getTicketPrice(String accessToken, String contentType, String startStationID, String endStationID, String lang) {
        subscription = getTicketPriceObservable(accessToken, contentType, startStationID, endStationID, lang).subscribe(getTicketPriceSubscriber());
    }

    @Override
    public void createTicket(String contentType, String token, CreateTicketRequest createTicketRequest) {
        subscription = createTicketObservable(contentType, token, createTicketRequest).subscribe(createTicketSubscriber());
    }

    @Override
    public void getMyTickets(String contentType, String token) {
        subscription = getMyTicketsObservable(contentType, token).subscribe(getMyTicketsScheduleSubscriber());
    }

    @Override
    public void validateTicket(String contentType, String token, ValidateTicketRequest validateTicketRequest) {
        subscription = validateTicketObservable(contentType, token, validateTicketRequest).subscribe(validateTicketSubscriber());
    }

    public Observable<ValidateTicketResponse> validateTicketObservable(String contentType, String token, ValidateTicketRequest validateTicketRequest) {
        try {
            return getService().validateTicket(contentType, token, validateTicketRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Observable<CreateTicketResponse> createTicketObservable(String contentType, String token, CreateTicketRequest createTicketRequest) {
        try {
            return getService().createTicket(contentType, token, createTicketRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observable<TicketListResponse> getMyTicketsObservable(String contentType, String token) {
        try {
            return getService().getMyTickets(contentType, token)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public DefaultSubscriber<ValidateTicketResponse> validateTicketSubscriber() {
        return new DefaultSubscriber<ValidateTicketResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        ValidateTicketResponse exceptionResponse = new ValidateTicketResponse();
                        exceptionResponse.setMessage("Your session has expired. Please login to pickup where you left off.");
                        exceptionResponse.setStatusCode(401);
                        ticketView.showValidateTicket(exceptionResponse);
                    } else {
                        ValidateTicketResponse response = error.getErrorBodyAs(ValidateTicketResponse.class);
                        if (response == null) {
                            response = new ValidateTicketResponse();
                        } else {
                        }
                        ticketView.showValidateTicket(response);
                    }
                } catch (IOException ex) {
                    ValidateTicketResponse exceptionResponse = new ValidateTicketResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    ticketView.showValidateTicket(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(ValidateTicketResponse response) {
                if (response != null) {
                    response.setStatusCode(200);
                    ticketView.showValidateTicket(response);
                }
            }
        };
    }

    public DefaultSubscriber<CreateTicketResponse> createTicketSubscriber() {
        return new DefaultSubscriber<CreateTicketResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        CreateTicketResponse exceptionResponse = new CreateTicketResponse();
                        exceptionResponse.setMessage("Your session has expired. Please login to pickup where you left off.");
                        exceptionResponse.setStatusCode(401);
                        ticketView.showTicketCreated(exceptionResponse);
                    } else {
                        CreateTicketResponse response = error.getErrorBodyAs(CreateTicketResponse.class);
                        if (response == null) {
                            response = new CreateTicketResponse();
                        } else {
                        }
                        ticketView.showTicketCreated(response);
                    }
                } catch (IOException ex) {
                    CreateTicketResponse exceptionResponse = new CreateTicketResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    ticketView.showTicketCreated(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(CreateTicketResponse response) {
                if (response != null) {
                    ticketView.showTicketCreated(response);
                }
            }
        };
    }

    public DefaultSubscriber<TicketListResponse> getMyTicketsScheduleSubscriber() {
        return new DefaultSubscriber<TicketListResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        TicketListResponse exceptionResponse = new TicketListResponse();
                        exceptionResponse.setMessage("Your session has expired. Please login to pickup where you left off.");
                        exceptionResponse.setStatusCode(401);
                        ticketView.showMyTickets(exceptionResponse);
                    } else {
                        TicketListResponse response = error.getErrorBodyAs(TicketListResponse.class);
                        if (response == null) {
                            response = new TicketListResponse();
                        } else {
                        }
                        ticketView.showMyTickets(response);
                    }
                } catch (IOException ex) {
                    TicketListResponse exceptionResponse = new TicketListResponse();
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    ticketView.showMyTickets(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(TicketListResponse response) {
                if (response != null) {
                    response.setStatusCode(200);
                    ticketView.showMyTickets(response);
                }
            }
        };
    }

    public Observable<TicketPriceResponse> getTicketPriceObservable(String accessToken, String contentType, String startStationID, String endStationID, String lang) {
        try {
            return getService().getTicketPrice(accessToken, contentType, startStationID, endStationID, lang)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public DefaultSubscriber<TicketPriceResponse> getTicketPriceSubscriber() {
        return new DefaultSubscriber<TicketPriceResponse>(this.mView) {


            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    if (error.getKind() == RetrofitException.Kind.EXPIRED) {
                        TicketPriceResponse exceptionResponse = new TicketPriceResponse();
                        exceptionResponse.setMESSAGE(ApplicationConstants.EMPTY_DATA);
                        ticketView.showTicketPrice(exceptionResponse);
                    } else {
                        TicketPriceResponse response = error.getErrorBodyAs(TicketPriceResponse.class);
                        if (response == null) {
                            response = new TicketPriceResponse();
                        } else {
                        }
                        ticketView.showTicketPrice(response);
                    }
                } catch (IOException ex) {
                    TicketPriceResponse exceptionResponse = new TicketPriceResponse();
                    exceptionResponse.setMESSAGE(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    ticketView.showTicketPrice(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(TicketPriceResponse response) {
                if (response != null) {
//                    BaseApplication.getBaseApplication().clearData();
                    ticketView.showTicketPrice(response);
                }
            }
        };
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    private TicketService getService() {
        return (TicketService) mService;
    }

    @Override
    public void attachView(View v) {
        if (v instanceof TicketView) {
            ticketView = (TicketView) v;
            mView = ticketView;
        }
    }


}