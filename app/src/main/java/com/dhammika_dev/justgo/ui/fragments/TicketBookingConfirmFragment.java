package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.TicketService;
import com.dhammika_dev.justgo.domain.TicketServiceImpl;
import com.dhammika_dev.justgo.dto.lankagate.TrainsList;
import com.dhammika_dev.justgo.model.entities.request.CreateTicketRequest;
import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketPriceResponse;
import com.dhammika_dev.justgo.model.entities.response.ValidateTicketResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.model.rest.LankagateAPIService;
import com.dhammika_dev.justgo.mvp.presenters.TicketPresenter;
import com.dhammika_dev.justgo.mvp.presenters.TicketPresenterImpl;
import com.dhammika_dev.justgo.mvp.views.TicketView;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TicketBookingConfirmFragment extends BaseFragment implements TicketView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TrainsList trainsList;
    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    @BindView(R.id.firstRadioButton)
    RadioButton firstRadioButton;
    @BindView(R.id.secondRadioButton)
    RadioButton secondRadioButton;
    @BindView(R.id.thirdRadioButton)
    RadioButton thirdRadioButton;
    @BindView(R.id.start_station)
    TextView start_station;
    @BindView(R.id.end_station)
    TextView end_station;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.train_type)
    TextView train_type;
    @BindView(R.id.train_name)
    TextView train_name;
    @BindView(R.id.departure_at)
    TextView departure_at;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.price)
    TextView price;
    TicketPriceResponse ticketPriceResponse;
    String fromStation, searchDate, toStation, startStationID, endStationID;
    SharedPreferences preferences;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public TicketBookingConfirmFragment() {
        // Required empty public constructor
    }

    public static TicketBookingConfirmFragment newInstance(String param1, String param2) {
        TicketBookingConfirmFragment fragment = new TicketBookingConfirmFragment();
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
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_ticket_booking_confirm, container, false);
        ButterKnife.bind(this, rootView);

        preferences = BaseApplication.getBaseApplication().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);

        Bundle bundle = getArguments();
        trainsList = (TrainsList) bundle.getSerializable("trainsList");
        fromStation = getArguments().getString("fromStation");
        toStation = getArguments().getString("toStation");
        searchDate = getArguments().getString("searchDate");
        startStationID = getArguments().getString("startStationID");
        endStationID = getArguments().getString("endStationID");

        performGetTicketPrice();
        return rootView;
    }

    void performGetTicketPrice() {
        setLoading(true);
        ((TicketPresenter) presenter).getTicketPrice("Bearer c4e6458b-3417-3d4e-8ca3-dfb57a2a9a9b", "application/json", startStationID, endStationID, "en");
    }

    public void setUI() {
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                firstRadioButton.setBackgroundResource(0);
                secondRadioButton.setBackgroundResource(0);
                thirdRadioButton.setBackgroundResource(0);
                if (isChecked) {
                    checkedRadioButton.setBackground(getResources().getDrawable(R.drawable.schedule_search_button));
                    for (int x = 0; x < ticketPriceResponse.getRESULTS().getPriceList().size(); x++) {
                        if (ticketPriceResponse.getRESULTS().getPriceList().get(x).getClassName().equals("1st Class") && firstRadioButton.isChecked()) {
                            price.setText("Rs." + ticketPriceResponse.getRESULTS().getPriceList().get(x).getPriceLKR());
                        }
                        if (ticketPriceResponse.getRESULTS().getPriceList().get(x).getClassName().equals("2nd Class") && secondRadioButton.isChecked()) {
                            price.setText("Rs." + ticketPriceResponse.getRESULTS().getPriceList().get(x).getPriceLKR());
                        }
                        if (ticketPriceResponse.getRESULTS().getPriceList().get(x).getClassName().equals("3rd Class") && thirdRadioButton.isChecked()) {
                            price.setText("Rs." + ticketPriceResponse.getRESULTS().getPriceList().get(x).getPriceLKR());
                        }
                    }
                }
            }
        });
        start_station.setText(fromStation);
        end_station.setText(toStation);
        date.setText(searchDate);
        train_type.setText(trainsList.getTrainType());
        train_name.setText(trainsList.getTrainName());
        String formatedTime = "";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
            final Date dateObj = sdf.parse(trainsList.getDepatureTime());
            System.out.println(dateObj);
            System.out.println(new SimpleDateFormat("K:mm a").format(dateObj));
            formatedTime = new SimpleDateFormat("K:mm a").format(dateObj);

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        departure_at.setText(formatedTime);
        for (int i = 0; i < trainsList.getClassList().size(); i++) {
            if (trainsList.getClassList().get(i).getClassID() == 3) {
                thirdRadioButton.setChecked(true);
                for (int x = 0; x < ticketPriceResponse.getRESULTS().getPriceList().size(); x++) {
                    if (ticketPriceResponse.getRESULTS().getPriceList().get(x).getClassName().equals("3rd Class")) {
                        price.setText("Rs." + ticketPriceResponse.getRESULTS().getPriceList().get(x).getPriceLKR());
                    }
                }
            }
        }
        for (int x = 0; x < ticketPriceResponse.getRESULTS().getPriceList().size(); x++) {
            if (ticketPriceResponse.getRESULTS().getPriceList().get(x).getClassName().equals("1st Class")) {
                firstRadioButton.setVisibility(View.VISIBLE);
            }
            if (ticketPriceResponse.getRESULTS().getPriceList().get(x).getClassName().equals("2nd Class")) {
                secondRadioButton.setVisibility(View.VISIBLE);
            }
            if (ticketPriceResponse.getRESULTS().getPriceList().get(x).getClassName().equals("3rd Class")) {
                thirdRadioButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public LoginResponse retrieveObjectFromSharedPreferences() {
        Gson gson = new Gson();
        String json = preferences.getString(IPreferencesKeys.USER_INFO, null);
        return gson.fromJson(json, LoginResponse.class);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout_passenger_home, fragment);
        transaction.commit();
    }

    @OnClick(R.id.confirm_btn)
    public void handleConfirmClick() {
        Fragment fragment;
        fragment = (preferences.contains(IPreferencesKeys.ACCESS_TOKEN)) && retrieveObjectFromSharedPreferences().getUser_type().equals("passenger") ? new PaymentFragment() : new LogInFragment();
        CreateTicketRequest createTicketRequest = new CreateTicketRequest();
        createTicketRequest.setPrice(price.getText().toString());
        String classType = "";
        if (firstRadioButton.isChecked()) {
            classType = "1st Class";
        } else if (secondRadioButton.isChecked()) {
            classType = "2nd Class";
        } else if (thirdRadioButton.isChecked()) {
            classType = "3rd Class";
        }
        createTicketRequest.setClassType(classType);
        createTicketRequest.setDate(searchDate);
        createTicketRequest.setDistance(distance.getText().toString());
        createTicketRequest.setSource(fromStation);
        createTicketRequest.setDestination(toStation);
        createTicketRequest.setTrain_id(Integer.toString(trainsList.getTrainID()));
        createTicketRequest.setTrain_no(Integer.toString(trainsList.getTrainNo()));
        createTicketRequest.setArrival_time(trainsList.getArrivalTime());
        createTicketRequest.setDepartur_time(trainsList.getDepatureTime());
        createTicketRequest.setTrain_name(trainsList.getTrainName());
        createTicketRequest.setTrain_type(trainsList.getTrainType());
        createTicketRequest.setTrain_frequency(trainsList.getTrainFrequncy());
        Bundle args = new Bundle();
        args.putSerializable("ticketDetails", createTicketRequest);
        fragment.setArguments(args);
        loadFragment(fragment);
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
        if (ticketPriceResponse.isSUCCESS()) {
            this.ticketPriceResponse = ticketPriceResponse;
            setUI();
            distance.setText(ticketPriceResponse.getRESULTS().getPriceList().get(0).getDistanceKM() + "KM");
            setLoading(false);

        } else {
            showAlertDialog(true, "Something went wrong!", ticketPriceResponse.getMESSAGE(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setLoading(false);
                }
            });
        }
    }

    @Override
    public void showTicketCreated(CreateTicketResponse createTicketResponse) {

    }

    @Override
    public void showMyTickets(TicketListResponse ticketListResponse) {

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
