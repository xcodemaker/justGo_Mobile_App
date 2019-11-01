package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.UserSettingService;
import com.dhammika_dev.justgo.domain.UserSettingServiceImpl;
import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.mvp.presenters.UserSettingPresenter;
import com.dhammika_dev.justgo.mvp.presenters.UserSettingPresenterImpl;
import com.dhammika_dev.justgo.mvp.views.UserSettingView;
import com.dhammika_dev.justgo.ui.activity.AuthActivity;
import com.dhammika_dev.justgo.utils.AppScheduler;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StaffSettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StaffSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffSettingFragment extends BaseFragment implements UserSettingView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences preferences;

    private OnFragmentInteractionListener mListener;

    public StaffSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaffSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffSettingFragment newInstance(String param1, String param2) {
        StaffSettingFragment fragment = new StaffSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initializePresenter() {
        UserSettingService mUserService = new UserSettingServiceImpl(new JustGoAPIService());
        presenter = new UserSettingPresenterImpl(getActivity(), mUserService, new AppScheduler());
        presenter.attachView(this);
        presenter.onCreate();
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
//        return inflater.inflate(R.layout.fragment_staff_setting, container, false);
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_staff_setting, container, false);
        ButterKnife.bind(this, rootView);
        preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        return rootView;
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

    @OnClick(R.id.logout)
    void OnClickLogOut() {
        performLogout();
    }

    private void performLogout() {
        setLoading(true);
        ((UserSettingPresenter) presenter).doLogout("Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, null));
    }

    @Override
    public void showLogout(LogoutResponse logoutResponse) {

        showAlertDialog(true, "Success !", logoutResponse.getMessage(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseApplication.getBaseApplication().clearData();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        setLoading(false);
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
