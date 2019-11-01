package com.dhammika_dev.justgo.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TableLayout;
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
import com.dhammika_dev.justgo.ui.adapters.ExpandableScheduleListAdapter;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.dhammika_dev.justgo.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrainScheduleFragment extends BaseFragment implements ScheduleView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseHelper db;
    String fromStation, toStation, date, endTime, startTime;
    String fromStationID, toStationID;
    TableLayout table;
    Context ct;
    AlertDialog.Builder window;
    String[] Options;
    ExpandableScheduleListAdapter listAdapter;
    //    ExpandableListView expListView;
    List<TrainsList> listDataHeader = new ArrayList<>();
    HashMap<TrainsList, TrainsList> listDataChild = new HashMap<>();
    TrainScheduleResponse trainScheduleResponse;
    @BindView(R.id.lvExp)
    ExpandableListView expListView;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private RecyclerView.Adapter adapter;
    private List<ScheduleList> listItems;

    public TrainScheduleFragment() {
        // Required empty public constructor
    }

    public static TrainScheduleFragment newInstance(String param1, String param2) {
        TrainScheduleFragment fragment = new TrainScheduleFragment();
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
        rootView = inflater.inflate(R.layout.fragment_train_schedule, container, false);
        ButterKnife.bind(this, rootView);


        Options = new String[]{"Current ocation", "Expected Arrival Times"};


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

    public void setListeners() {
        listAdapter = new ExpandableScheduleListAdapter(getContext(), listDataHeader, listDataChild, trainScheduleResponse);

        expListView.setAdapter(listAdapter);


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return false;
            }
        });
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
            listDataHeader.addAll(trainScheduleResponse.getRESULTS().getDirectTrains().getTrainsList());
            for (int i = 0; i < trainScheduleResponse.getRESULTS().getDirectTrains().getTrainsList().size(); i++) {
                listDataChild.put(trainScheduleResponse.getRESULTS().getDirectTrains().getTrainsList().get(i), trainScheduleResponse.getRESULTS().getDirectTrains().getTrainsList().get(i));
            }
            setListeners();
            setLoading(false);

        } else {
            showAlertDialog(true, "Something went wrong!", trainScheduleResponse.getMESSAGE(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setLoading(false);
                }
            });
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
