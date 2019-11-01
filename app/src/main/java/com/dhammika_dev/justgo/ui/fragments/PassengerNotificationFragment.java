package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhammika_dev.justgo.R;


public class PassengerNotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //int currentOffset;


//    private int[] gallayImages = {R.drawable.gallery_imag1, R.drawable.gallery_imag2, R.drawable.gallery_imag3,
//             R.drawable.gallery_imag4, R.drawable.gallery_imag5, R.drawable.gallery_imag6, R.drawable.gallery_imag7,
//             R.drawable.gallery_imag8};
//
//    private AsymmetricGridView listView;
//    private GalleryAdapter adapter;
//    protected Preference preference;
//    protected ArrayList<String> gallayImages;
//    private ArrayList<GalleryDO> images;


    private OnFragmentInteractionListener mListener;

    public PassengerNotificationFragment() {
        // Required empty public constructor
    }

    public static PassengerNotificationFragment newInstance(String param1, String param2) {
        PassengerNotificationFragment fragment = new PassengerNotificationFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_passenger_notification, container, false);

//        AsymmetricGridView listView = (AsymmetricGridView) rootView.findViewById(R.id.listView);
//
//        // Choose your own preferred column width
//        listView.setRequestedColumnWidth(Utils.dpToPx(getActivity(), 120));
//        final List<SimplePOJO> items = getDefaultItems(10);
//
//        // initialize your items array
//        GalleryAdapter adapter = new GalleryAdapter(getActivity(), items);
//        AsymmetricGridViewAdapter asymmetricAdapter =
//                new AsymmetricGridViewAdapter<>(getActivity(), listView, adapter);
//        listView.setAdapter(asymmetricAdapter);

        return rootView;


    }


//    public List<SimplePOJO> getDefaultItems(int qty) {
//        List<SimplePOJO> items = new ArrayList<>();
//
//        for (int i = 0; i < qty; i++) {
//            int colSpan = Math.random() < 0.2f ? 2 : 1;
//            int rowSpan = colSpan;
//            if (i == 0) {
//                rowSpan = 2;
//            }
//            SimplePOJO item = new SimplePOJO(colSpan, rowSpan, currentOffset + i);
//            items.add(item);
//        }
//
//        currentOffset += qty;
//
//        return items;
//    }

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
