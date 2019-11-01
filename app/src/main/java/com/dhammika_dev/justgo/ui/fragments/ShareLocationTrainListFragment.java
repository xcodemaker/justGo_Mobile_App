package com.dhammika_dev.justgo.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.domain.ScheduleService;
import com.dhammika_dev.justgo.domain.ScheduleServiceImpl;
import com.dhammika_dev.justgo.dto.ScheduleList;
import com.dhammika_dev.justgo.dto.lankagate.TrainsList;
import com.dhammika_dev.justgo.model.entities.response.TrainScheduleResponse;
import com.dhammika_dev.justgo.model.rest.LankagateAPIService;
import com.dhammika_dev.justgo.mvp.presenters.SchedulePresenter;
import com.dhammika_dev.justgo.mvp.presenters.SchedulePresenterImpl;
import com.dhammika_dev.justgo.mvp.views.ScheduleView;
import com.dhammika_dev.justgo.ui.activity.StaffHomeActivity;
import com.dhammika_dev.justgo.ui.adapters.LocationShareListAdapter;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.dhammika_dev.justgo.utils.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareLocationTrainListFragment extends BaseFragment implements ScheduleView, LocationShareListAdapter.OnNoteListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseHelper db;
    String fromStation, toStation, date, endTime, startTime;
    String fromStationID, toStationID;
    TableLayout table;
    Context ct;
    AlertDialog.Builder window;
    String[] Options;
    TrainScheduleResponse trainScheduleResponse;
    @BindView(R.id.booking_recycler_view)
    RecyclerView recyclerView;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private RecyclerView.Adapter adapter;
    private List<ScheduleList> listItems;

    public ShareLocationTrainListFragment() {
        // Required empty public constructor
    }

    public static ShareLocationTrainListFragment newInstance(String param1, String param2) {
        ShareLocationTrainListFragment fragment = new ShareLocationTrainListFragment();
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
        ScheduleService mUserService = new ScheduleServiceImpl(new LankagateAPIService());
        presenter = new SchedulePresenterImpl(getActivity(), mUserService, new AppScheduler());
        presenter.attachView(this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_ticket_booking, container, false);
        ButterKnife.bind(this, rootView);

        Options = new String[]{"Current Location", "Expected Arrival Times"};


        ct = getContext();

        fromStation = getArguments().getString("fromStation");
        toStation = getArguments().getString("toStation");
        date = getArguments().getString("date");
        endTime = getArguments().getString("endTime");
        startTime = getArguments().getString("startTime");


        listItems = new ArrayList<>();

        db = new DatabaseHelper(getContext());
        SQLiteDatabase database = db.getWritableDatabase();

        Cursor cursor_fromStation = db.getStationID(database, fromStation);
        Cursor cursor_toStation = db.getStationID(database, toStation);
        Cursor c_test = db.getNoOfRows(database);

        int c = 0;
        while (c <= 3) {
            if (cursor_fromStation.getCount() == 1 && cursor_toStation.getCount() == 1) {
                while (cursor_fromStation.moveToNext()) {
                    fromStationID = Integer.toString(cursor_fromStation.getInt(cursor_fromStation.getColumnIndex("STATION_ID")));
                }
                while (cursor_toStation.moveToNext()) {
                    toStationID = Integer.toString(cursor_toStation.getInt(cursor_toStation.getColumnIndex("STATION_ID")));
                }
                break;
            } else {
                if (c == 3) {
                    Toast.makeText(getContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                }
                db.onUpgrade(database, 1, 1);
                cursor_fromStation = db.getStationID(database, fromStation);
                cursor_toStation = db.getStationID(database, toStation);
            }
            c++;
        }

        performSearchTrainSchedule("Bearer c4e6458b-3417-3d4e-8ca3-dfb57a2a9a9b", "application/json", fromStationID, toStationID, date, startTime, endTime, "en");

        return rootView;
    }

    private void performSearchTrainSchedule(String accessToken, String contentType, String startStationID, String endStationID, String searchDate, String startTime, String endTime, String lang) {
        setLoading(true);
        ((SchedulePresenter) presenter).doSearchTrainSchedule(accessToken, contentType, startStationID, endStationID, searchDate, startTime, endTime, lang);
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
    public void showSearchTrainSchedule(TrainScheduleResponse trainScheduleResponse) {
        if (trainScheduleResponse.isSUCCESS()) {
            this.trainScheduleResponse = trainScheduleResponse;
            if (trainScheduleResponse.getRESULTS().getDirectTrains().getTrainsList().size() != 0) {
                List<TrainsList> trainList = trainScheduleResponse.getRESULTS().getDirectTrains().getTrainsList();
                for (int i = 0; i < trainList.size(); i++) {
                    String arrivalTimeAtEndStation = trainList.get(i).getArrivalTimeEndStation();
                    String formatedTime = "";
                    try {
                        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
                        final Date dateObj = sdf.parse(arrivalTimeAtEndStation);
                        System.out.println(dateObj);
                        System.out.println(new SimpleDateFormat("K:mm a").format(dateObj));
                        formatedTime = new SimpleDateFormat("K:mm a").format(dateObj);

                    } catch (final ParseException e) {
                        e.printStackTrace();
                    }
                    TableRow row = new TableRow(ct);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                    row.setLayoutParams(lp);
                    String departure = date + " at " + formatedTime;
                    ScheduleList item = new ScheduleList();
                    item.setDeparture(departure);
                    item.setOrigin(fromStation);
                    item.setDestination(toStation);
                    item.setTrain(trainList.get(i).getTrainName().equals("") ? trainList.get(i).getTrainNo()+"" : trainList.get(i).getTrainName());
                    listItems.add(item);
                }
                setAdapter();
            }
            setLoading(false);

        } else {
            setLoading(false);
            showAlertDialog(true, "Something went wrong!", trainScheduleResponse.getMESSAGE(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((StaffHomeActivity) getActivity()).selectSpecificTab(5);
                }
            });
        }
    }

    public void setAdapter() {
        adapter = new LocationShareListAdapter(getContext(), listItems, this, trainScheduleResponse);
        recyclerView.setLayoutManager(new LinearLayoutManager(ct));
        recyclerView.setAdapter(adapter);
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
