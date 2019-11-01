package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.AuthService;
import com.dhammika_dev.justgo.domain.AuthServiceImpl;
import com.dhammika_dev.justgo.model.entities.request.LoginRequest;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.mvp.presenters.AuthPresenter;
import com.dhammika_dev.justgo.mvp.presenters.AuthPresenterImpl;
import com.dhammika_dev.justgo.mvp.views.UserLoginView;
import com.dhammika_dev.justgo.ui.activity.HomeActivity;
import com.dhammika_dev.justgo.ui.activity.PassengerHomeActivity;
import com.dhammika_dev.justgo.ui.activity.StaffHomeActivity;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LogInFragment extends BaseFragment implements SignUpFragment.OnFragmentInteractionListener, UserLoginView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.password)
    TextView password;
    String selectedUserType;
    private String mParam1;
    private String mParam2;
    private SharedPreferences preferences;
    private Fragment fragment;
    private OnFragmentInteractionListener mListener;

    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
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
        presenter.attachView(LogInFragment.this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
        setIconColors();
        ButterKnife.bind(this, rootView);
        preferences = BaseApplication.getBaseApplication().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        try {
            selectedUserType = getArguments().getString("selectedUserType");
        } catch (NullPointerException e) {
            System.out.println("null point exception");
            selectedUserType = "";
        }
        setUpUI();
        return rootView;
    }

    private void setUpUI() {
        String savedUserName = BaseApplication.getBaseApplication().getDecryptString(IPreferencesKeys.JUST_GO_SAVE_USER_NAME);
        String savedPassword = BaseApplication.getBaseApplication().getDecryptString(IPreferencesKeys.JUST_GO_SAVE_PASSWORD);

        if (savedUserName != null && !savedUserName.isEmpty() && savedPassword != null && !savedPassword.isEmpty()) {
            email.setText(savedUserName);
            password.setText(savedPassword);
        }
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

    @OnClick(R.id.froget_password)
    void redirectToForgotPassword() {
        loadFragment(new PasswordResetEmailEnterFragment());
    }

    private void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("selectedUserType", selectedUserType);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout_auth, fragment);
//        transaction.addToBackStack("LogInFragment");
        transaction.commit();
    }

    private void performLogin(String contentType, String xRequestedWith, LoginRequest loginRequest) {
        setLoading(true);
        ((AuthPresenter) presenter).doLogin(contentType, xRequestedWith, loginRequest);
    }

    public void saveObjectToSharedPreferences(String preferencesKeys, LoginResponse loginResponse) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginResponse);
        prefsEditor.putString(preferencesKeys, json);
        prefsEditor.apply();
    }



    @OnClick(R.id.signup)
    public void onClickSignUp() {
        fragment = new SignUpFragment();
        loadFragment(fragment);
    }

    @OnClick(R.id.login3)
    public void salonView() {
        boolean isValid = validate();
        if (isValid) {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email.getText().toString());
            loginRequest.setPassword(password.getText().toString());
            performLogin("application/json", "XMLHttpRequest", loginRequest);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private boolean validate() {
        final String userEmail = email.getText().toString();
        final String pass = password.getText().toString();
        boolean isValid = true;
        if (userEmail == null || userEmail.trim().length() == 0) {
            email.setError("Required");
            isValid = false;
        } else if (!isValidEmail(userEmail)) {
            email.setError("Invalid Email");
            isValid = false;
        }
        if (pass == null || pass.trim().length() == 0) {
            Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void showLogin(LoginResponse loginResponse) {

        if (loginResponse.getMessage().equals("Authorized")) {

            String mUserName = email.getText().toString().trim();
            String mPassword = password.getText().toString().trim();
            mUserName = mUserName.toLowerCase();

            if (!mUserName.isEmpty() && !mPassword.isEmpty()) {
                new MyAsyncTask().execute(mUserName, mPassword);
            }

            saveObjectToSharedPreferences(IPreferencesKeys.USER_INFO, loginResponse);
            Intent activityChangeIntent = new Intent(getContext(), HomeActivity.class);
            if (loginResponse.getUser_type().equals("passenger")) {
                activityChangeIntent = new Intent(getContext(), PassengerHomeActivity.class);
            } else if (loginResponse.getUser_type().equals("train_driver") || loginResponse.getUser_type().equals("ticket_checker")) {
                activityChangeIntent = new Intent(getContext(), StaffHomeActivity.class);
            }

            getContext().startActivity(activityChangeIntent);
            setLoading(false);
            getActivity().finish();
        } else {
            showAlertDialog(true, "Login Failed!", loginResponse.getMessage(), new DialogInterface.OnClickListener() {
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

    public class MyAsyncTask extends AsyncTask<String, String, String> {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        protected String doInBackground(String... params) {
            BaseApplication.getBaseApplication().saveEncryptPasswordAndUserName(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}
