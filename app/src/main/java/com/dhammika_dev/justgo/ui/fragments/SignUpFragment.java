package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.domain.AuthService;
import com.dhammika_dev.justgo.domain.AuthServiceImpl;
import com.dhammika_dev.justgo.model.entities.request.EmailValidateRequest;
import com.dhammika_dev.justgo.model.entities.request.SignUpRequest;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignUpFragment extends BaseFragment implements UserRegisterView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.nic_or_passport)
    EditText nic_or_passport;
    @BindView(R.id.profile_first_name)
    TextView first_name;
    @BindView(R.id.profile_last_name)
    TextView last_name;
    @BindView(R.id.profile_email)
    TextView email;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.profile_phone)
    TextView phone;
    @BindView(R.id.profile_password)
    TextView password;
    @BindView(R.id.role_spinner)
    Spinner role_spinner;
    @BindView(R.id.role_type)
    LinearLayout role_type;
    String roleSelected = "";
    String selectedUserType;
    private String mParam1;
    private String mParam2;
    private SignUpFragment.OnFragmentInteractionListener mListener;
    private Fragment fragment;


    public SignUpFragment() {
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        presenter.attachView(SignUpFragment.this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = null;

        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        try {
            selectedUserType = getArguments().getString("selectedUserType");
        } catch (NullPointerException e) {
            System.out.println("null point exception");
            selectedUserType = "";
        }

        setIconColors();
        ButterKnife.bind(this, rootView);
        setUpUI();
        setDistrictSpinner();
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
        if (context instanceof SignUpFragment.OnFragmentInteractionListener) {
            mListener = (SignUpFragment.OnFragmentInteractionListener) context;
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
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout_auth, fragment);
        transaction.commit();
    }

    public void setDistrictSpinner() {

        String[] data = {"Train driver", "Ticket checker"};

        ArrayList<String> plants = new ArrayList<String>();
        plants.add("Select a role...");
        for (String item : data) {
            plants.add(item);

        }

        final List<String> plantsList = plants;

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item_district, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.district_spinner_dropdown_item);
        role_spinner.setAdapter(spinnerArrayAdapter);

        role_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    for (String item : data) {
                        if (item == selectedItemText) {
                            roleSelected = item;
                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpUI() {
        if (selectedUserType.equals("passenger")) {
            role_type.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            nic_or_passport.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.login)
    public void onClickSignUp() {
        fragment = new LogInFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectedUserType", selectedUserType);
        fragment.setArguments(bundle);
        loadFragment(fragment);
    }

    @OnClick(R.id.continueBtn)
    public void onClickContinue() {
        boolean isValid = validate();
        if (isValid) {
            final String userEmail = email.getText().toString();
            isRegisteredEmail(userEmail);
        }
    }


    private boolean validate() {
        final String userFirstName = first_name.getText().toString();
        final String userLastName = last_name.getText().toString();
        final String userEmail = email.getText().toString();
        final String userNIC = nic_or_passport.getText().toString();
        final String userRole = roleSelected;
        final String userPhone = phone.getText().toString();
        final String pass = password.getText().toString();
        boolean isValid = true;

        if (userFirstName == null || userFirstName.trim().length() == 0) {
            first_name.setError("Required");
            isValid = false;
        }
        if (userLastName == null || userLastName.trim().length() == 0) {
            last_name.setError("Required");
            isValid = false;
        }
        if (userRole == null || userRole.trim().length() == 0 && !selectedUserType.equals("passenger")) {
            showTopSnackBar("Please select your role", getResources().getColor(R.color.sign_up_password_text_color));
//            country.setError("Required");
            isValid = false;
        }

        if (userNIC == null || userNIC.trim().length() == 0 && !selectedUserType.equals("passenger")) {
            nic_or_passport.setError("Required");
            isValid = false;
        }
        if (userEmail == null || userEmail.trim().length() == 0) {
            email.setError("Required");
            isValid = false;
        } else if (!isValidEmail(userEmail)) {
            email.setError("Invalid Email");
            isValid = false;
        }

        if (userPhone == null || userPhone.trim().length() == 0) {
            phone.setError("Required");
            isValid = false;
        } else if (!isValidPhone(userPhone)) {
            phone.setError("Invalid Phone");
            isValid = false;
        }
        if (pass == null || pass.trim().length() == 0) {
            showTopSnackBar("Enter Password", getResources().getColor(R.color.sign_up_password_text_color));
//            Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
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

    private boolean isRegisteredEmail(String email) {
        boolean result = false;
        checkEmail(email);
        return result;
    }

    private boolean isValidPhone(String phone) {
        // String PHONE_PATTERN = "[1-9][0-9]{9}";
        String PHONE_PATTERN = "^[+]?[0-9]{10,13}$";


        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private void checkEmail(String email) {
        setLoading(true);
        EmailValidateRequest emailValidateRequest = new EmailValidateRequest();
        emailValidateRequest.setEmail(email);
        ((AuthPresenter) presenter).doEmailValidate(emailValidateRequest);
    }

    @Override
    public void showEmailValidation(EmailValidateResponse emailValidateResponse) {
        if (emailValidateResponse.isValid()) {

            setLoading(false);

            final String userFirstName = first_name.getText().toString();
            final String userLastName = last_name.getText().toString();
            final String userEmail = email.getText().toString();
            final String userRole = selectedUserType.equals("passenger") ? "passenger" : roleSelected;
            final String userAddress = selectedUserType.equals("passenger") ? "" : address.getText().toString();
            final String userNIC = selectedUserType.equals("passenger") ? "" : nic_or_passport.getText().toString();
            final String userPhone = phone.getText().toString();
            final String pass = password.getText().toString();


            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setFirst_name(userFirstName);
            signUpRequest.setLast_name(userLastName);
            signUpRequest.setEmail(userEmail);
            signUpRequest.setAddress(userAddress);
            signUpRequest.setNic_or_passport(userNIC);
            signUpRequest.setContact_number(userPhone);
            signUpRequest.setPassword(pass);

            if (userRole.equals("Train driver")) {
                signUpRequest.setUser_type("train_driver");
            } else if (userRole.equals("Ticket checker")) {
                signUpRequest.setUser_type("ticket_checker");
            } else if (userRole.equals("passenger")) {
                signUpRequest.setUser_type("passenger");
            }


            Gson gson = new Gson();
            String json = gson.toJson(signUpRequest);
            loadFragment(new SignUpContinueProfileFragment().newInstance(json, ""));
        } else {
            setLoading(false);
            email.setError("Email already registered");
        }
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

}
