package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.TicketService;
import com.dhammika_dev.justgo.domain.TicketServiceImpl;
import com.dhammika_dev.justgo.dto.Ticket;
import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketPriceResponse;
import com.dhammika_dev.justgo.model.entities.response.ValidateTicketResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.model.rest.LankagateAPIService;
import com.dhammika_dev.justgo.mvp.presenters.TicketPresenter;
import com.dhammika_dev.justgo.mvp.presenters.TicketPresenterImpl;
import com.dhammika_dev.justgo.mvp.views.TicketView;
import com.dhammika_dev.justgo.ui.activity.PassengerHomeActivity;
import com.dhammika_dev.justgo.ui.adapters.MyBookingsListAdapter;
import com.dhammika_dev.justgo.utils.AppScheduler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBookingsFragment extends BaseFragment implements TicketView, MyBookingsListAdapter.OnNoteListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context ct;
    @BindView(R.id.my_bookings_recycler_view)
    RecyclerView recyclerView;
    TicketListResponse ticketListResponse;
    private String mParam1;
    private String mParam2;
    private SharedPreferences preferences;
    private RecyclerView.Adapter adapter;
    private List<Ticket> listItems;
    private OnFragmentInteractionListener mListener;

    public MyBookingsFragment() {
        // Required empty public constructor
    }

    public static MyBookingsFragment newInstance(String param1, String param2) {
        MyBookingsFragment fragment = new MyBookingsFragment();
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
        TicketService ticketService = new TicketServiceImpl(new LankagateAPIService(), new JustGoAPIService());
        presenter = new TicketPresenterImpl(getActivity(), ticketService, new AppScheduler());
        presenter.attachView(this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_my_bookings, container, false);
        ButterKnife.bind(this, rootView);
        preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        listItems = new ArrayList<>();
        performMyTicketsRequest();
        return rootView;
    }

    public void performMyTicketsRequest() {
        ((TicketPresenter) presenter).getMyTickets("application/json", "Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, null));
        setLoading(true);
    }

    public void setAdapter() {
        adapter = new MyBookingsListAdapter(getContext(), listItems, this, ticketListResponse);
        recyclerView.setLayoutManager(new LinearLayoutManager(ct));
        recyclerView.setAdapter(adapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
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

    }

    @Override
    public void showMyTickets(TicketListResponse ticketListResponse) {
        if (ticketListResponse.getStatusCode() == 200) {
            if (ticketListResponse.getTickets().size() > 0) {
                this.ticketListResponse = ticketListResponse;
                System.out.println("================== ticketListResponse " + ticketListResponse.toString());
                listItems.addAll(ticketListResponse.getTickets());
                setAdapter();
            } else {
                showAlertDialog(true, "Oopps!", "There are no bookings available", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((PassengerHomeActivity) getActivity()).selectSpecificTab(1);
                    }
                });
            }
            setLoading(false);
        } else {
            setLoading(false);
            showAlertDialog(true, "Something went wrong!", ticketListResponse.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
    }

    @Override
    public void showValidateTicket(ValidateTicketResponse validateTicketResponse) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }

    @Override
    public void onNoteClick(int position) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
