package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.AuthService;
import com.dhammika_dev.justgo.domain.AuthServiceImpl;
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
import com.dhammika_dev.justgo.ui.activity.HomeActivity;
import com.dhammika_dev.justgo.ui.activity.PassengerHomeActivity;
import com.dhammika_dev.justgo.ui.activity.StaffHomeActivity;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.dhammika_dev.justgo.widgets.CenterProgressDialog;
import com.google.gson.Gson;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SignUpContinueProfileFragment extends BaseFragment implements UserRegisterView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int SELECT_PICTURE = 1;
    public static SignUpContinueProfileFragment profileFragment = null;
    private final String KEY = "AKIAI6NX7OU22XNRFFHQ";
    private final String SECRET = "FQ4gRh7MBQMrFGQwlvhFT2a26lcDYaZ";
    Integer REQUEST_CAMERA = 200, SELECT_FILE = 1;
    //    Bitmap image;
    String imageEncoded;
    MultipartBody.Part profileImage;
    @BindView(R.id.image_profile)
    ImageView wahImageView;
    private String mParam1;
    private String mParam2;
    private Uri selectedImageUri;
    private CenterProgressDialog progressDialog;
    private SignUpRequest signUpRequest;
    private OnFragmentInteractionListener mListener;
    private String imageURI;
    private Bitmap mProfImage;
    private InputStream mImageStream;
    private SharedPreferences preferences;
    private Uri fileUri;


    public SignUpContinueProfileFragment() {
    }

    public static SignUpContinueProfileFragment newInstance(String param1, String param2) {
        SignUpContinueProfileFragment fragment = new SignUpContinueProfileFragment();
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
        presenter.attachView(SignUpContinueProfileFragment.this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_up_continue_profile, container, false);
        setIconColors();
        ButterKnife.bind(this, rootView);

        profileFragment = this;

        Gson gson = new Gson();
        String json = mParam1;
        signUpRequest = gson.fromJson(json, SignUpRequest.class);
        preferences = BaseApplication.getBaseApplication().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
//        System.out.println("=====================>>>>>>>>> object from profile update  :"+signUpRequest.toString()+"<<<<<<<<<<<<<=============================");
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

    @OnClick(R.id.upload_button)
    public void selectImages() {
        ProPicBottomSheetFragment bottomSheetFragment = new ProPicBottomSheetFragment();
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout_passenger_home, fragment);
        transaction.commit();
    }

    @OnClick(R.id.continueBtn)
    public void onClickContinue() {
        doCustomerRegister();
    }


    private void doCustomerRegister() {
        setLoading(true);
        System.out.println("=====================>>>>>>>>> signUpRequest   :" + signUpRequest.toString() + "<<<<<<<<<<<<<=============================");
        MultipartBody.Part profile_pic = profileImage;
        RequestBody first_name = requestBody(signUpRequest.getFirst_name());
        RequestBody last_name = requestBody(signUpRequest.getLast_name());
        RequestBody email = requestBody(signUpRequest.getEmail());
        RequestBody address = requestBody(signUpRequest.getAddress());
        RequestBody nic_or_passport = requestBody(signUpRequest.getNic_or_passport());
        RequestBody contact_number = requestBody(signUpRequest.getContact_number());
        RequestBody user_type = requestBody(signUpRequest.getUser_type());
        RequestBody password = requestBody(signUpRequest.getPassword());
        ((AuthPresenter) presenter).doRegister(profile_pic, first_name, last_name, email, address, nic_or_passport, contact_number, user_type, password, password);
    }

    private RequestBody requestBody(String value) {
        return RequestBody.create(
                MediaType.parse("multipart/form-data"),
                value
        );
    }


    public void saveObjectToSharedPreferences(String preferencesKeys, LoginResponse loginResponse) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginResponse);
        prefsEditor.putString(preferencesKeys, json);
        prefsEditor.apply();
    }


    public void setProfileImage(Bitmap image, Uri selectedImageUri, MultipartBody.Part tempprofileImage) {
        mProfImage = image;
        imageURI = selectedImageUri.toString();
        profileImage = tempprofileImage;
        wahImageView.setImageBitmap(image);
    }

    public void setGalleryProfileImage(InputStream imageStream, Uri selectedImageUri, MultipartBody.Part tempprofileImage) {
        mImageStream = imageStream;
        imageURI = selectedImageUri.toString();
        profileImage = tempprofileImage;
        wahImageView.setImageURI(selectedImageUri);
    }

    @Override
    public void showEmailValidation(EmailValidateResponse emailValidateResponse) {

    }

    @Override
    public void showRegister(LoginResponse signUpResponse) {
        if (signUpResponse.getMessage().equals("Authorized")) {
            saveObjectToSharedPreferences(IPreferencesKeys.USER_INFO, signUpResponse);
            Intent activityChangeIntent = new Intent(getContext(), HomeActivity.class);
            ;
            if (signUpResponse.getUser_type().equals("passenger")) {
                activityChangeIntent = new Intent(getContext(), PassengerHomeActivity.class);
            } else if (signUpResponse.getUser_type().equals("train_driver")) {
                activityChangeIntent = new Intent(getContext(), StaffHomeActivity.class);
            }
            getContext().startActivity(activityChangeIntent);
            setLoading(false);
            getActivity().finish();
        } else {
            showAlertDialog(true, "Registration Failed!", signUpResponse.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setLoading(false);
                }
            });
        }
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
