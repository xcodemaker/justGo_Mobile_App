package com.dhammika_dev.justgo.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.ui.activity.PassengerHomeActivity;
import com.dhammika_dev.justgo.utils.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchBookingFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String FromStation, ToStation, Date;
    String StartTime = "";
    String EndTime = "";
    DatabaseHelper db;
    String[] stations;
    List<String> stationlist;
    @BindView(R.id.from_station)
    AutoCompleteTextView fromStation;
    @BindView(R.id.to_station)
    AutoCompleteTextView toStation;
    @BindView(R.id.date)
    TextView displayDate;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    public SearchBookingFragment() {

    }


    public static SearchBookingFragment newInstance(String param1, String param2) {
        SearchBookingFragment fragment = new SearchBookingFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_search_booking, container, false);
        ButterKnife.bind(this, rootView);

        ((PassengerHomeActivity) getActivity()).hideBackButton(true);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new Date();
        displayDate.setText(formatter.format(date));

        db = new DatabaseHelper(getContext());
        SQLiteDatabase database = db.getWritableDatabase();
        updateStationList(database);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                displayDate.setText(date);
            }
        };

        return rootView;
    }

    @OnClick({R.id.calender_icon, R.id.date, R.id.date_container})
    public void selectDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                dateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.schedule_search_container));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    public void updateStationList(SQLiteDatabase database) {
        Cursor cursor = db.getStations(database);
        stations = new String[cursor.getCount()];
        stationlist = Arrays.asList(stations);

        int i = 0;
        while (cursor.moveToNext()) {
            stations[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_2));
            Log.e("stations", stations[i]);
            i++;
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, stations);
        fromStation.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, stations);
        toStation.setAdapter(adapter2);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @OnClick(R.id.search_btn)
    public void openViewTrainSchedule() {
        ToStation = toStation.getText().toString().toUpperCase();
        FromStation = fromStation.getText().toString().toUpperCase();
        Date = displayDate.getText().toString();

        Boolean cheker = true;

        if (StartTime.isEmpty()) {
            StartTime = "00:00:00";
        }
        if (EndTime.isEmpty()) {
            EndTime = "23:59:59";
        }
        if (FromStation.isEmpty()) {
            toStation.setError("Field cannot be left blank");
            cheker = false;
        }
        if (FromStation.isEmpty()) {
            fromStation.setError("Field cannot be left blank");
            cheker = false;
        }
        if (!stationlist.contains(ToStation)) {
            toStation.setError("Station not found");
            cheker = false;
        }
        if (!stationlist.contains(FromStation)) {
            fromStation.setError("Station not found");
            cheker = false;
        }
        if (cheker) {
            Bundle args = new Bundle();
            args.putString("fromStation", FromStation);
            args.putString("toStation", ToStation);
            args.putString("date", Date);
            args.putString("endTime", EndTime);
            args.putString("startTime", StartTime);
            Fragment fragment = new TicketBookingFragment();
            loadFragment(fragment, args);

        }
    }

    private void loadFragment(Fragment fragment, Bundle args) {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack("PassengerHomeFragment");
        transaction.replace(R.id.frame_layout_passenger_home, fragment);
        transaction.commit();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
