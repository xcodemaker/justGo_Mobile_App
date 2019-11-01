package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.domain.ProfileViewService;
import com.dhammika_dev.justgo.domain.ProfileViewServiceImpl;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.model.entities.response.LogoutResponse;
import com.dhammika_dev.justgo.model.entities.response.UserDetailsResponse;
import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.mvp.presenters.ProfileViewPresenter;
import com.dhammika_dev.justgo.mvp.presenters.ProfileViewPresenterImpl;
import com.dhammika_dev.justgo.mvp.views.ProfileView;
import com.dhammika_dev.justgo.ui.activity.AuthActivity;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends BaseFragment implements ProfileView {
    public static ProfileFragment profileFragment = null;
    final String TAG = ProfileFragment.this.getClass().getSimpleName();
    @BindView(R.id.profile_img)
    CircleImageView proPic;
    @BindView(R.id.tv_name)
    TextView name;
    @BindView(R.id.tv_email)
    TextView email;
    @BindView(R.id.tv_contact_num)
    TextView contactNum;
    @BindView(R.id.logout_btn)
    Button logoutBtn;
    SharedPreferences preferences;
    public ProfileFragment() {

    }

    public static String getTAG() {
        return "StaffProfileFragment";
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    protected void initializePresenter() {
        ProfileViewService mUserService = new ProfileViewServiceImpl(new JustGoAPIService());
        presenter = new ProfileViewPresenterImpl(getActivity(), mUserService, new AppScheduler());
        presenter.attachView(ProfileFragment.this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        preferences = BaseApplication.getBaseApplication().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        setUpUI();
        return rootView;
    }

    @OnClick(R.id.logout_btn)
    void OnClickLogOut() {
        showAlertDialogAdvanced(true, "Warning!", "Are you sure you want to logout?", "yes", "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performLogout();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.edit_btn)
    void onClickProfileEdit() {
        loadFragment(new ProfileEditFragment());
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (preferences.contains(IPreferencesKeys.ACCESS_TOKEN) && retrieveObjectFromSharedPreferences().getUser_type().equals("passenger")) {
            transaction.replace(R.id.frame_layout_passenger_home, fragment);
        } else {
            transaction.replace(R.id.frame_layout_staff_home, fragment);
        }
        transaction.commit();
    }

    public LoginResponse retrieveObjectFromSharedPreferences() {
        Gson gson = new Gson();
        String json = preferences.getString(IPreferencesKeys.USER_INFO, null);
        return gson.fromJson(json, LoginResponse.class);
    }


    protected void setUpUI() {
        performGetUserDetailsRequest("Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, null));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (myAlertDialog != null && myAlertDialog.isShowing())
            myAlertDialog.dismiss();
    }

    public void setUserDetails(UserDetailsResponse userDetailsResponse, boolean isEdit) {
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

            if (!isEdit) {
                if (userDetailsResponse.getUser().getFirst_name() != null && !userDetailsResponse.getUser().getFirst_name().isEmpty())
                    name.setText(new StringBuilder().append(userDetailsResponse.getUser().getFirst_name() + " " + userDetailsResponse.getUser().getLast_name()));
                if (userDetailsResponse.getUser().getContact_number() != null && !userDetailsResponse.getUser().getContact_number().isEmpty())
                    contactNum.setText(userDetailsResponse.getUser().getContact_number());
                if (userDetailsResponse.getUser().getEmail() != null && !userDetailsResponse.getUser().getEmail().isEmpty())
                    email.setText(userDetailsResponse.getUser().getEmail());
            }
        }
    }


    public void performGetUserDetailsRequest(String access_token) {
        ((ProfileViewPresenter) presenter).getUserDetails(access_token);
        setLoading(true);
    }

    @Override
    public void showProfile(UserDetailsResponse userDetailsResponse) {
        if (userDetailsResponse.getStatusCode() == 200) {
            if (userDetailsResponse.getUser() != null)
                setUserDetails(userDetailsResponse, false);
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


    private void performLogout() {
        setLoading(true);
        ((ProfileViewPresenter) presenter).doLogout("Bearer " + preferences.getString(IPreferencesKeys.ACCESS_TOKEN, null));
    }

    @Override
    public void showLogout(LogoutResponse logoutResponse) {

        showAlertDialog(true, "Success !", logoutResponse.getMessage(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseApplication.getBaseApplication().clearData();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                if (preferences.contains(IPreferencesKeys.ACCESS_TOKEN)) {
                    intent.putExtra("selectedUserType", retrieveObjectFromSharedPreferences().getUser_type());
                } else {
                    intent.putExtra("selectedUserType", "passenger");
                }
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        setLoading(false);
    }

    @Override
    public void showProfileImageUpdate(UserDetailsResponse userDetailsResponse) {

    }

    @Override
    public void showProfileDetailsUpdate(UserDetailsResponse userDetailsResponse) {

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
