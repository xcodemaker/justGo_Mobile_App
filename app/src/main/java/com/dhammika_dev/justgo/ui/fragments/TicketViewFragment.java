package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.model.entities.response.CreateTicketResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TicketViewFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CreateTicketResponse createTicketResponse;
    @BindView(R.id.ticket_qr)
    ImageView ticket_qr;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public TicketViewFragment() {
        // Required empty public constructor
    }

    public static TicketViewFragment newInstance(String param1, String param2) {
        TicketViewFragment fragment = new TicketViewFragment();
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
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_ticket_view, container, false);
        setIconColors();
        ButterKnife.bind(this, rootView);
        Bundle bundle = getArguments();
        createTicketResponse = (CreateTicketResponse) bundle.getSerializable("createTicketResponse");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_qr);
        requestOptions.error(R.drawable.ic_error);
        Glide.with(getContext())
                .load(createTicketResponse.getQr_code())
                .apply(requestOptions)
                .into(ticket_qr);
        System.out.println("===============>>>> createTicketResponse from TicketView" + createTicketResponse.toString());
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @OnClick(R.id.download_btn)
    void handleDownload() {
        try {
            URL url = new URL(createTicketResponse.getQr_code());
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            File path = new File(Environment.getExternalStorageDirectory() + "/justgo"); //Creates app specific folder
            path.mkdirs();
            File imageFile = new File(path, createTicketResponse.getTicket_id() + ".png"); // Imagename.png
            FileOutputStream out = new FileOutputStream(imageFile);

            image.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
            out.flush();
            out.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(getContext(), new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                    showAlertDialog(true, "Downloaded", "Your ticket download to your phone", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setLoading(false);
                        }
                    });
                }
            });
        } catch (Exception e) {
            System.out.println(e);
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
