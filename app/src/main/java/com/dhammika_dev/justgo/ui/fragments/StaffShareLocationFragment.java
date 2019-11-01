package com.dhammika_dev.justgo.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.dto.lankagate.TrainsList;
import com.dhammika_dev.justgo.service.TrackerService;
import com.dhammika_dev.justgo.utils.DatabaseHelper;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.LOCATION_SERVICE;

public class StaffShareLocationFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PERMISSIONS_REQUEST = 1;
    SharedPreferences preferences;
    LocationManager locationManager;
    String travelingTrain = "1";
    String trainStartTime = "1503";
    @BindView(R.id.share_btn)
    Button shareButton;
    @BindView(R.id.trainId)
    EditText trainID;
    @BindView(R.id.from_station)
    AutoCompleteTextView fromStation;
    @BindView(R.id.to_station)
    AutoCompleteTextView toStation;
    @BindView(R.id.train_name) TextView train_name;
    DatabaseHelper db;
    String[] stations;
    List<String> stationlist;
    String FromStation, ToStation, TrainName;

    private OnFragmentInteractionListener mListener;

    TrainsList trainsList;

    public StaffShareLocationFragment() {
    }


    public static StaffShareLocationFragment newInstance(String param1, String param2) {
        StaffShareLocationFragment fragment = new StaffShareLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_staff_share_location, container, false);
        preferences = BaseApplication.getBaseApplication().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        setIconColors();
        ButterKnife.bind(this, rootView);

        db = new DatabaseHelper(getContext());
        SQLiteDatabase database = db.getWritableDatabase();
        updateStationList(database);

        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Bundle bundle = getArguments();
        if(bundle != null){
            trainsList = (TrainsList) bundle.getSerializable("trainsList");
            FromStation = getArguments().getString("fromStation");
            ToStation = getArguments().getString("toStation");
            TrainName = getArguments().getString("trainName");
            fromStation.setText(FromStation);
            toStation.setText(ToStation);
            train_name.setText(TrainName);
        }

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getContext(), "Please enable location services", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            updateUI();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
        return rootView;
    }

    private void updateUI() {

        shareButton.setEnabled(true);
        if( trainsList != null && trainsList.getTrainID() == (int)trainsList.getTrainID()){
            trainID.setText(trainsList.getTrainID()+"", TextView.BufferType.EDITABLE);
        }
    }


    private void startTrackerService() {
        System.out.println("=============================>>>>> startTrackerService called");
        Intent intent = new Intent(getActivity(), TrackerService.class);
        trainID.getText();
        ToStation = toStation.getText().toString().toUpperCase();
        FromStation = fromStation.getText().toString().toUpperCase();
        TrainName = train_name.getText().toString();
        if(TrainName.isEmpty()){
            train_name.setError("Train name required");
            return;
        }
        if (FromStation.isEmpty()) {
            fromStation.setError("Field cannot be left blank");
            return;
        }
        if (ToStation.isEmpty()) {
            toStation.setError("Field cannot be left blank");
            return;
        }
        if (!stationlist.contains(ToStation)) {
            toStation.setError("Station not found");
            return;
        }
        if (!stationlist.contains(FromStation)) {
            fromStation.setError("Station not found");
            return;
        }
        if (trainID.getText().length() == 0) {
            trainID.setError("Train Id required");
        } else {
            String path = "trains/" + trainID.getText();
            intent.putExtra("db_path", path);
            intent.putExtra("fromStation", FromStation);
            intent.putExtra("toStation", ToStation);
            intent.putExtra("trainName",TrainName);
            getActivity().startService(intent);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permissions granted", Toast.LENGTH_LONG).show();
            updateUI();
        } else {
            Toast.makeText(getContext(), "Permissions not granted", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.share_btn)
    void handleShareButton() {
        if (shareButton.isEnabled()) {
            startTrackerService();
//            Toast.makeText(getContext(), "Tracking Started", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Permissions not granted", Toast.LENGTH_LONG).show();
        }
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
