package com.dhammika_dev.justgo.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TimePicker;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.utils.DatabaseHelper;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PassengerHomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.end_time)
    TextView endTime;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    public PassengerHomeFragment() {
        // Required empty public constructor
    }

    public static PassengerHomeFragment newInstance(String param1, String param2) {
        PassengerHomeFragment fragment = new PassengerHomeFragment();
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
        // Inflate the layout for this fragment
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_passenger_home, container, false);
        ButterKnife.bind(this, rootView);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public LoginResponse retrieveObjectFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(IPreferencesKeys.USER_INFO, null);
        return gson.fromJson(json, LoginResponse.class);
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

    @OnClick(R.id.time_picker_from)
    public void showStartTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String am_pm = "";

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, minute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "am";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "pm";

                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

                startTime.setText(strHrsToShow + ":" + selectedMinute + " " + am_pm);
                if (am_pm == "am") {
                    StartTime = strHrsToShow + ":" + selectedMinute + ":00";
                } else {
                    StartTime = selectedHour + ":" + selectedMinute + ":00";
                }
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.schedule_search_container));
        mTimePicker.show();
    }

    @OnClick(R.id.time_picker_to)
    public void showEndTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String am_pm = "";

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, minute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "am";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "pm";

                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

                endTime.setText(strHrsToShow + ":" + selectedMinute + " " + am_pm);
                if (am_pm == "am") {
                    EndTime = strHrsToShow + ":" + selectedMinute + ":00";
                } else {
                    EndTime = selectedHour + ":" + selectedMinute + ":00";
                }
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.schedule_search_container));
        mTimePicker.show();
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
            fromStation.setError("Field cannot be left blank");
            cheker = false;
        }
        if (ToStation.isEmpty()) {
            toStation.setError("Field cannot be left blank");
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
            Fragment fragment = new TrainScheduleFragment();
            loadFragment(fragment, args);

        }
    }

    private void loadFragment(Fragment fragment, Bundle args) {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack("PassengerHomeFragment");
        if (retrieveObjectFromSharedPreferences().getUser_type().equals("passenger")) {
            transaction.replace(R.id.frame_layout_passenger_home, fragment);
        } else if (retrieveObjectFromSharedPreferences().getUser_type().equals("ticket_checker")) {
            transaction.replace(R.id.frame_layout_staff_home, fragment);
        } else {
            transaction.replace(R.id.frame_layout_passenger_home, fragment);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
