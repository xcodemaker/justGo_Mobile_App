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
import android.widget.EditText;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.domain.AuthService;
import com.dhammika_dev.justgo.domain.AuthServiceImpl;
import com.dhammika_dev.justgo.model.entities.request.PasswordResetRequest;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordResetFragment extends BaseFragment implements UserRegisterView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.confirm_password)
    EditText confirmPassword;
    @BindView(R.id.new_password)
    EditText newPassword;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public PasswordResetFragment() {
        // Required empty public constructor
    }

    public static PasswordResetFragment newInstance(String param1, String param2) {
        PasswordResetFragment fragment = new PasswordResetFragment();
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
        rootView = inflater.inflate(R.layout.fragment_password_reset, container, false);
        ButterKnife.bind(this, rootView);
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

    @OnClick(R.id.reset_password)
    void passwordReset() {
        if (newPassword.getText().toString().trim().isEmpty()) {
            showTopSnackBar(getString(R.string.new_password_required), getResources().getColor(R.color.sign_up_password_text_color));
        } else if (!isValidPassword(newPassword.getText().toString())) {
            showTopSnackBar(getString(R.string.password_invalid), getResources().getColor(R.color.sign_up_password_text_color));
        } else if (confirmPassword.getText().toString().trim().isEmpty()) {
            showTopSnackBar(getString(R.string.confirm_required), getResources().getColor(R.color.sign_up_password_text_color));
        } else if (!isValidConfirmPassword(confirmPassword.getText().toString().trim())) {
            showTopSnackBar(getString(R.string.confirm_password_invalid), getResources().getColor(R.color.sign_up_password_text_color));
        } else {
            PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
            passwordResetRequest.setPassword(newPassword.getText().toString());
            passwordResetRequest.setPassword_confirmation(confirmPassword.getText().toString());
            passwordResetRequest.setEmail(mParam2);
            passwordResetRequest.setToken(mParam1);
            performPasswordReset(passwordResetRequest);
        }
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    private boolean isValidConfirmPassword(String password) {
        return password != null && (newPassword.getText().toString()).equals(password);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout_auth, fragment);
//        transaction.addToBackStack("PassengerLogInFragment");
        transaction.commit();
    }


    private void performPasswordReset(PasswordResetRequest passwordResetRequest) {
        setLoading(true);
        ((AuthPresenter) presenter).passwordReset(passwordResetRequest);
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

    }

    @Override
    public void ShowPasswordResetResponse(PasswordResetResponse passwordResetResponse) {
        if (passwordResetResponse.getEmail().equals(mParam2)) {
            showAlertDialog(false, "Success!", "You password change successfully, Now you can login!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadFragment(new LogInFragment());
                }
            });
            setLoading(false);
//            getActivity().finish();
        } else {
            showAlertDialog(true, "Password reset Failed!", passwordResetResponse.getMessage(), new DialogInterface.OnClickListener() {
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
