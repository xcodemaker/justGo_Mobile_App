package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.domain.AuthService;
import com.dhammika_dev.justgo.domain.AuthServiceImpl;
import com.dhammika_dev.justgo.model.entities.response.EmailValidateResponse;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.model.entities.response.PasswordResetResponse;
import com.dhammika_dev.justgo.model.entities.response.RequestTokenResponse;
import com.dhammika_dev.justgo.model.entities.response.TokenVerifyResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.mvp.presenters.AuthPresenter;
import com.dhammika_dev.justgo.mvp.presenters.AuthPresenterImpl;
import com.dhammika_dev.justgo.mvp.views.UserRegisterView;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PasswordResetVerificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PasswordResetVerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordResetVerificationFragment extends BaseFragment implements UserRegisterView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.otp_view)
    OtpView otp_view;
    String token;
    boolean isValid = false;
    String resetToken;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public PasswordResetVerificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerPasswordResetVerificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PasswordResetVerificationFragment newInstance(String param1, String param2) {
        PasswordResetVerificationFragment fragment = new PasswordResetVerificationFragment();
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
        AuthService mUserService = new AuthServiceImpl(new JustGoAPIService());
        presenter = new AuthPresenterImpl(getActivity(), mUserService, new AppScheduler());
        presenter.attachView(this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_password_reset_verification, container, false);
        ButterKnife.bind(this, rootView);
        try {
            resetToken = getArguments().getString("resetToken");
        } catch (NullPointerException e) {
            System.out.println("null point exception");
            resetToken = "";
        }
        if (resetToken.length() > 0) {
            otp_view.setText(resetToken);
            isValid = true;
            token = resetToken;
        }
        otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {

                // do Stuff
//                Log.d("onOtpCompleted=>", otp);
                isValid = true;
                System.out.println("============>>>>>>>>> onOtpCompleted: " + otp);
                token = otp;
            }
        });
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

    @OnClick(R.id.verificaton_btn)
    void verification() {
//        String token;
//        String userEmail = email.getText().toString();
//
        if (!isValid) {
//            otp_view.setError("Invalid token");
            showTopSnackBar("Invalid token", getResources().getColor(R.color.sign_up_password_text_color));
//            isValid = false;
        }
        if (isValid) {
            performTokenVerification(token);
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout_auth, fragment);
//        transaction.addToBackStack("PassengerLogInFragment");
        transaction.commit();
    }


    private void performTokenVerification(String token) {
        setLoading(true);
        ((AuthPresenter) presenter).verifyToken(token);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showEmailValidation(EmailValidateResponse emailValidateResponse) {

    }

    @Override
    public void showRegister(LoginResponse signUpResponse) {

    }

    @Override
    public void showRequestToken(RequestTokenResponse requestTokenResponse) {

    }

    @Override
    public void showVerifyToken(TokenVerifyResponse requestTokenResponse) {
        if (requestTokenResponse.getToken().equals(token)) {
            showAlertDialog(false, "Success!", "You can reset your password now!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadFragment(new PasswordResetFragment().newInstance(requestTokenResponse.getToken(), requestTokenResponse.getEmail()));
                }
            });
            setLoading(false);
//            getActivity().finish();
        } else {
            showAlertDialog(true, "Verification Failed!", requestTokenResponse.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setLoading(false);
                }
            });
        }
    }

    @Override
    public void ShowPasswordResetResponse(PasswordResetResponse passwordResetResponse) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showErrorMessage(String calledMethod, String error, String errorDescription) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
