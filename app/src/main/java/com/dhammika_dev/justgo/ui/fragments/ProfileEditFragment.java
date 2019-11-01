package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.CommonUtils;
import com.dhammika_dev.justgo.common.constants.ApplicationConstants;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.ProfileViewService;
import com.dhammika_dev.justgo.domain.ProfileViewServiceImpl;
import com.dhammika_dev.justgo.model.entities.request.UpdateUserDetailsRequest;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.entities.response.UserDetailsResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.mvp.presenters.ProfileViewPresenter;
import com.dhammika_dev.justgo.mvp.presenters.ProfileViewPresenterImpl;
import com.dhammika_dev.justgo.mvp.views.ProfileView;
import com.dhammika_dev.justgo.ui.activity.AuthActivity;
import com.dhammika_dev.justgo.ui.activity.PassengerHomeActivity;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.google.android.gms.common.util.IOUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;

public class ProfileEditFragment extends BaseFragment implements ProfileView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ProfileEditFragment profileFragment = null;
    SharedPreferences preferences;
    @BindView(R.id.imageview_account_profile)
    CircleImageView proPic;
    @BindView(R.id.profile_first_name)
    EditText profile_first_name;
    @BindView(R.id.profile_last_name)
    EditText profile_last_name;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.nic_or_passport)
    EditText nic_or_passport;
    @BindView(R.id.profile_phone)
    EditText profile_phone;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private UserDetailsResponse profileResponseObject;
    private ImageLoader imageLoader;
    public ProfileEditFragment() {
        this.imageLoader = ImageLoader.getInstance();
    }

    public static String getTAG() {
        return "ProfileEditFragment";
    }

    public static ProfileEditFragment newInstance() {
        return new ProfileEditFragment();
    }

    public static ProfileEditFragment newInstance(String param1, String param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
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
        ProfileViewService mUserService = new ProfileViewServiceImpl(new JustGoAPIService());
        presenter = new ProfileViewPresenterImpl(getActivity(), mUserService, new AppScheduler());
        presenter.attachView(this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        ButterKnife.bind(this, rootView);
        profileFragment = this;
        preferences = BaseApplication.getBaseApplication().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        setUpUI();
        return rootView;
    }

    @OnClick({R.id.floatingActionButton, R.id.imageview_account_profile})
    void onClickProfileImage() {
        ProfileUpdateImageBottomSheetFragment bottomSheetFragment = new ProfileUpdateImageBottomSheetFragment();
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }

    @OnClick(R.id.saveBtn)
    void onClickSaveBtn() {
        if (validate()) {
            saveUserDetailAlert();
        }
    }

    @OnClick(R.id.cancelBtn)
    void onClickCancel() {
        ((PassengerHomeActivity) getActivity()).selectSpecificTab(4);
    }

    private boolean validate() {
        final String userFirstName = profile_first_name.getText().toString();
        final String userLastName = profile_last_name.getText().toString();
        final String userNIC = nic_or_passport.getText().toString();
        final String userPhone = profile_phone.getText().toString();
        boolean isValid = true;

        if (userFirstName == null || userFirstName.trim().length() == 0) {
            profile_first_name.setError("Required");
            isValid = false;
        }
        if (userLastName == null || userLastName.trim().length() == 0) {
            profile_last_name.setError("Required");
            isValid = false;
        }

        if (userNIC == null || userNIC.trim().length() == 0 && !retrieveObjectFromSharedPreferences().getUser_type().equals("passenger")) {
            nic_or_passport.setError("Required");
            isValid = false;
        }

        if (userPhone == null || userPhone.trim().length() == 0) {
            profile_phone.setError("Required");
            isValid = false;
        } else if (!isValidPhone(userPhone)) {
            profile_phone.setError("Invalid Phone");
            isValid = false;
        }

        return isValid;
    }

    private boolean isValidPhone(String phone) {
        // String PHONE_PATTERN = "[1-9][0-9]{9}";
        String PHONE_PATTERN = "^[+]?[0-9]{10,13}$";


        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    protected void setUpUI() {
        if (preferences.contains(IPreferencesKeys.ACCESS_TOKEN) && retrieveObjectFromSharedPreferences().getUser_type().equals("passenger")) {
            address.setVisibility(View.GONE);
            nic_or_passport.setVisibility(View.GONE);
        }
        performGetUserDetailsRequest("Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, null));
    }

    public void performGetUserDetailsRequest(String access_token) {
        ((ProfileViewPresenter) presenter).getUserDetails(access_token);
        setLoading(true);
    }

    public void performUpdateUserDetailsRequest(UpdateUserDetailsRequest updateUserDetailsRequest) {
        ((ProfileViewPresenter) presenter).doUpdateProfileDetails("application/json", "Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, null), updateUserDetailsRequest);
        setLoading(true);
    }

    public void setUserDetails(UserDetailsResponse userDetailsResponse) {
        if (userDetailsResponse != null) {
            if (userDetailsResponse.getUser().getProfile_pic() != null && !userDetailsResponse.getUser().getProfile_pic().isEmpty()) {
                String imgUrl = userDetailsResponse.getUser().getProfile_pic();
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.ic_person_black_24dp);
                requestOptions.error(R.drawable.ic_error);
                Glide.with(getContext())
                        .load(imgUrl)
                        .apply(requestOptions)
                        .into(proPic);
            }

            if (userDetailsResponse.getUser().getFirst_name() != null && !userDetailsResponse.getUser().getFirst_name().isEmpty())
                profile_first_name.setText(new StringBuilder().append(userDetailsResponse.getUser().getFirst_name()));
            if (userDetailsResponse.getUser().getFirst_name() != null && !userDetailsResponse.getUser().getLast_name().isEmpty())
                profile_last_name.setText(new StringBuilder().append(userDetailsResponse.getUser().getLast_name()));
            if (userDetailsResponse.getUser().getContact_number() != null && !userDetailsResponse.getUser().getContact_number().isEmpty())
                profile_phone.setText(userDetailsResponse.getUser().getContact_number());
            if (userDetailsResponse.getUser().getEmail() != null && !userDetailsResponse.getUser().getAddress().isEmpty())
                address.setText(userDetailsResponse.getUser().getAddress());
            if (userDetailsResponse.getUser().getEmail() != null && !userDetailsResponse.getUser().getNic_or_passport().isEmpty())
                nic_or_passport.setText(userDetailsResponse.getUser().getNic_or_passport());
        }
    }

    public LoginResponse retrieveObjectFromSharedPreferences() {
        Gson gson = new Gson();
        String json = preferences.getString(IPreferencesKeys.USER_INFO, null);
        return gson.fromJson(json, LoginResponse.class);
    }


    public void saveUserDetailAlert() {
        showAlertDialogAdvanced(false, ApplicationConstants.SAVE_MY_DETAILS, ApplicationConstants.SAVE_MY_DETAILS_MESSAGE,
                getActivity().getResources().getString(R.string.save), getActivity().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateUserDetailsRequest updateUserDetailsRequest = new UpdateUserDetailsRequest();
                        updateUserDetailsRequest.setFirst_name(profile_first_name.getText().toString());
                        updateUserDetailsRequest.setLast_name(profile_last_name.getText().toString());
                        updateUserDetailsRequest.setContact_number(profile_phone.getText().toString());
                        updateUserDetailsRequest.setAddress(address.getText().toString());
                        updateUserDetailsRequest.setNic_or_passport(nic_or_passport.getText().toString());
                        performUpdateUserDetailsRequest(updateUserDetailsRequest);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setUserDetails(profileResponseObject);
                    }
                });
    }

    public void setProfileImage(Bitmap image, Uri selectedImageUri, MultipartBody.Part profileImage) {
        Glide.with(getContext())
                .asBitmap()
                .load(image)
                .into(proPic);
        performGetUploadProfileImage(profileImage);
    }

    public void setGalleryProfileImage(InputStream imageStream, Uri selectedImageUri, MultipartBody.Part profileImage) {
        try {
            Glide.with(getContext())
                    .load(IOUtils.toByteArray(imageStream))
                    .into(proPic);
        } catch (IOException e) {
            e.printStackTrace();
        }
        performGetUploadProfileImage(profileImage);
    }

    public void performGetUploadProfileImage(MultipartBody.Part profileImage) {
        if (CommonUtils.getInstance().isNetworkConnected()) {
            setLoading(true);
            ((ProfileViewPresenter) presenter).doUpdateUserProfileImage("Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, null), profileImage);
        } else {
            showAlertDialog(false, ApplicationConstants.WARNING,
                    ApplicationConstants.ERROR_MSG_CONNECTION_LOST, null);
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

    @Override
    public void showProfile(UserDetailsResponse userDetailsResponse) {
        if (userDetailsResponse.getStatusCode() == 200) {
            if (userDetailsResponse.getUser() != null)
                setUserDetails(userDetailsResponse);
            profileResponseObject = userDetailsResponse;
            setLoading(false);
        } else {
            setLoading(false);
            showAlertDialog(true, "Something went wrong!", userDetailsResponse.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (userDetailsResponse.getStatusCode() == 401) {
                        BaseApplication.getBaseApplication().clearData();
                        Intent intent = new Intent(getActivity(), AuthActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                }
            });
        }
    }

    @Override
    public void showLogout(LogoutResponse logoutResponse) {

    }

    @Override
    public void showProfileImageUpdate(UserDetailsResponse userDetailsResponse) {
        if (userDetailsResponse.getStatusCode() == 200) {
            if (userDetailsResponse.getUser() != null)
                setUserDetails(userDetailsResponse);
            profileResponseObject = userDetailsResponse;
            setLoading(false);
        } else {
            setLoading(false);
            showAlertDialog(true, "Something went wrong!", userDetailsResponse.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (userDetailsResponse.getStatusCode() == 401) {
                        BaseApplication.getBaseApplication().clearData();
                        Intent intent = new Intent(getActivity(), AuthActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                }
            });
        }
    }

    @Override
    public void showProfileDetailsUpdate(UserDetailsResponse userDetailsResponse) {
        if (userDetailsResponse.getStatusCode() == 200) {
            if (userDetailsResponse.getUser() != null)
                setUserDetails(userDetailsResponse);
            profileResponseObject = userDetailsResponse;
            setLoading(false);
            ((PassengerHomeActivity) getActivity()).selectSpecificTab(4);
        } else {
            setLoading(false);
            showAlertDialog(true, "Something went wrong!", userDetailsResponse.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (userDetailsResponse.getStatusCode() == 401) {
                        BaseApplication.getBaseApplication().clearData();
                        Intent intent = new Intent(getActivity(), AuthActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
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
