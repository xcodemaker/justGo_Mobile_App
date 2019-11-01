package com.dhammika_dev.justgo.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.TicketService;
import com.dhammika_dev.justgo.domain.TicketServiceImpl;
import com.dhammika_dev.justgo.model.entities.request.ValidateTicketRequest;
import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketListResponse;
import com.dhammika_dev.justgo.model.entities.response.TicketPriceResponse;
import com.dhammika_dev.justgo.model.entities.response.ValidateTicketResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.model.rest.LankagateAPIService;
import com.dhammika_dev.justgo.mvp.presenters.TicketPresenter;
import com.dhammika_dev.justgo.mvp.presenters.TicketPresenterImpl;
import com.dhammika_dev.justgo.mvp.views.TicketView;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QRScanFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener, BarcodeReaderFragment.BarcodeReaderListener, TicketView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 1208;
    //    @BindView(R.id.tv_result) TextView mTvResult;
    @BindView(R.id.fm_container)
    FrameLayout fm_container;
    @BindView(R.id.btn_fragment)
    Button btn_fragment;
    ValidateTicketResponse validateTicketResponse;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences preferences;

    public QRScanFragment() {
        // Required empty public constructor
    }


    public static QRScanFragment newInstance(String param1, String param2) {
        QRScanFragment fragment = new QRScanFragment();
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

    private void addBarcodeReaderFragment(boolean state) {
        BarcodeReaderFragment readerFragment = BarcodeReaderFragment.newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);

        FragmentManager supportFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if (state) {
            fragmentTransaction.replace(R.id.fm_container, readerFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            readerFragment.pauseScanning();
            fragmentTransaction.replace(R.id.fm_container, readerFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getContext(), "error in  scanning", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            System.out.println("============= >>>>>>>>> onActivityResult" + barcode.rawValue);
//            mTvResultHeader.setText("On Activity Result");
//            mTvResult.setText(barcode.rawValue);
        }
    }

    @Override
    protected void initializePresenter() {
        TicketService ticketService = new TicketServiceImpl(new LankagateAPIService(), new JustGoAPIService());
        presenter = new TicketPresenterImpl(getActivity(), ticketService, new AppScheduler());
        presenter.attachView(this);
        presenter.onCreate();
    }

    public void performValidateTicketRequest(String ticket_id) {
        ValidateTicketRequest validateTicketRequest = new ValidateTicketRequest();
        validateTicketRequest.setTicket_id(ticket_id);
        ((TicketPresenter) presenter).validateTicket("application/json", "Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, null), validateTicketRequest);
        setLoading(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_qrscan, container, false);
        ButterKnife.bind(this, rootView);
        preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        btn_fragment.setOnTouchListener(this);
        return rootView;
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
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            addBarcodeReaderFragment(true);
            fm_container.setVisibility(View.VISIBLE);
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            addBarcodeReaderFragment(false);
            fm_container.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void onScanned(Barcode barcode) {
        String token = barcode.rawValue;
        try {
            JWT jwt = new JWT(token);
            if (jwt.getIssuer().equals("justgo") && jwt.getSubject().equals("ticket")) {
                if (jwt.isExpired(10)) {
                    System.out.println("============= >>>>>>>>> this is expired ticket");
                } else {
                    System.out.println("============= >>>>>>>>> valid ticket " + jwt.getClaim("id").asString());
                    performValidateTicketRequest(jwt.getClaim("id").asString());
                }

            }
        } catch (DecodeException e) {
            System.out.println("============= >>>>>>>>> invalid ticket ");
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showTicketPrice(TicketPriceResponse ticketPriceResponse) {

    }

    @Override
    public void showTicketCreated(CreateTicketResponse createTicketResponse) {

    }

    @Override
    public void showMyTickets(TicketListResponse ticketListResponse) {

    }

    @Override
    public void showValidateTicket(ValidateTicketResponse validateTicketResponse) {
        if (validateTicketResponse.getStatusCode() == 200) {
            this.validateTicketResponse = validateTicketResponse;
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.custom_alert);
            Button dialogButton = (Button) dialog.findViewById(R.id.dialog_dismiss);
            ImageView validImage = (ImageView) dialog.findViewById(R.id.right_img);
            ImageView invalidImage = (ImageView) dialog.findViewById(R.id.wrong_img);
            TextView start_station = (TextView) dialog.findViewById(R.id.start_station);
            TextView date_book = (TextView) dialog.findViewById(R.id.date);
            TextView end_station = (TextView) dialog.findViewById(R.id.end_station);
            TextView title = (TextView) dialog.findViewById(R.id.title);
            TextView firstClass = (TextView) dialog.findViewById(R.id.firstClass);
            TextView secondClass = (TextView) dialog.findViewById(R.id.secondClass);
            TextView thirdClass = (TextView) dialog.findViewById(R.id.thirdClass);
            start_station.setText(validateTicketResponse.getTicket().getTicket_details().getSource());
            date_book.setText(validateTicketResponse.getTicket().getTicket_details().getDate());
            end_station.setText(validateTicketResponse.getTicket().getTicket_details().getDestination());

            if (validateTicketResponse.getTicket().getTicket_details().getClass_type().equals("1st Class")) {
                firstClass.setVisibility(View.VISIBLE);
            }
            if (validateTicketResponse.getTicket().getTicket_details().getClass_type().equals("2nd Class")) {
                secondClass.setVisibility(View.VISIBLE);
            }
            if (validateTicketResponse.getTicket().getTicket_details().getClass_type().equals("3rd Class")) {
                thirdClass.setVisibility(View.VISIBLE);
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            Date today = new Date();
            try {
                date = format.parse(validateTicketResponse.getTicket().getTicket_details().getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = date.getTime() - today.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            if (days < 0) {
                invalidImage.setVisibility(View.VISIBLE);
                title.setText("Expired Ticket");
            } else {
                validImage.setVisibility(View.VISIBLE);
                title.setText("Valid Ticket");
            }
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            setLoading(false);
        } else {
            setLoading(false);
            showAlertDialog(true, "Something went wrong!", validateTicketResponse.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
