package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.TicketService;
import com.dhammika_dev.justgo.domain.TicketServiceImpl;
import com.dhammika_dev.justgo.model.entities.request.CreateTicketRequest;
import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketPriceResponse;
import com.dhammika_dev.justgo.model.entities.response.ValidateTicketResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.model.rest.LankagateAPIService;
import com.dhammika_dev.justgo.mvp.presenters.TicketPresenter;
import com.dhammika_dev.justgo.mvp.presenters.TicketPresenterImpl;
import com.dhammika_dev.justgo.mvp.views.TicketView;
import com.dhammika_dev.justgo.ui.activity.AuthActivity;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.manojbhadane.PaymentCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardPaymentFragment extends BaseFragment implements TicketView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.creditCard)
    PaymentCardView paymentCardView;
    CreateTicketRequest createTicketRequest;
    CreateTicketResponse createTicketResponse;
    SharedPreferences preferences;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public CardPaymentFragment() {
        // Required empty public constructor
    }


    public static CardPaymentFragment newInstance(String param1, String param2) {
        CardPaymentFragment fragment = new CardPaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initializePresenter() {
        TicketService mUserService = new TicketServiceImpl(new LankagateAPIService(), new JustGoAPIService());
        presenter = new TicketPresenterImpl(getActivity(), mUserService, new AppScheduler());
        presenter.attachView(this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_card_payment, container, false);
        setIconColors();
        ButterKnife.bind(this, rootView);
        Bundle bundle = getArguments();
        createTicketRequest = (CreateTicketRequest) bundle.getSerializable("ticketDetails");
        System.out.println("===============>>>> ticketDetails" + createTicketRequest.toString());
        preferences = BaseApplication.getBaseApplication().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        paymentCardView.setOnPaymentCardEventListener(new PaymentCardView.OnPaymentCardEventListener() {
            @Override
            public void onCardDetailsSubmit(String month, String year, String cardNumber, String cvv) {
                System.out.println("card details submited" + month + " " + year + " " + cardNumber + " " + cvv);
                performGetTicket();
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onCancelClick() {

            }
        });
        return rootView;
    }

    void performGetTicket() {
        setLoading(true);
        ((TicketPresenter) presenter).createTicket("application/json", "Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, null), createTicketRequest);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showTicketPrice(TicketPriceResponse ticketPriceResponse) {

    }

    @Override
    public void showTicketCreated(CreateTicketResponse createTicketResponse) {
        System.out.println("====================>> showTicketCreated called");
        if (createTicketResponse.getMessage().equals("Ticket created")) {
            this.createTicketResponse = createTicketResponse;
            setLoading(false);
            System.out.println("====================>> showTicketCreated response" + createTicketResponse.toString());
            Fragment fragment = new TicketViewFragment();
            Bundle args = new Bundle();
            args.putSerializable("createTicketResponse", createTicketResponse);
            fragment.setArguments(args);
            loadFragment(fragment);
        } else {
            setLoading(false);
            showAlertDialog(true, "Something went wrong!", createTicketResponse.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (createTicketResponse.getStatusCode() == 401) {
                        BaseApplication.getBaseApplication().clearData();
                        Intent intent = new Intent(getActivity(), AuthActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                }
            });
        }
    }

    @Override
    public void showMyTickets(TicketListResponse ticketListResponse) {

    }

    @Override
    public void showValidateTicket(ValidateTicketResponse validateTicketResponse) {

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout_passenger_home, fragment);
        transaction.commit();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
