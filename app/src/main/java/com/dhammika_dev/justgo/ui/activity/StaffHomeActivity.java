package com.dhammika_dev.justgo.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.model.entities.response.LoginResponse;
import com.dhammika_dev.justgo.ui.fragments.PassengerHomeFragment;
import com.dhammika_dev.justgo.ui.fragments.ProfileEditFragment;
import com.dhammika_dev.justgo.ui.fragments.ProfileFragment;
import com.dhammika_dev.justgo.ui.fragments.ProfileUpdateImageBottomSheetFragment;
import com.dhammika_dev.justgo.ui.fragments.QRScanFragment;
import com.dhammika_dev.justgo.ui.fragments.SearchTrainForShareLocationFragment;
import com.dhammika_dev.justgo.ui.fragments.ShareLocationTrainListFragment;
import com.dhammika_dev.justgo.ui.fragments.StaffHomeFragment;
import com.dhammika_dev.justgo.ui.fragments.StaffNotificationFragment;
import com.dhammika_dev.justgo.ui.fragments.StaffSettingFragment;
import com.dhammika_dev.justgo.ui.fragments.StaffShareLocationFragment;
import com.dhammika_dev.justgo.ui.fragments.TrainScheduleFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffHomeActivity extends BaseActivity implements ShareLocationTrainListFragment.OnFragmentInteractionListener,SearchTrainForShareLocationFragment.OnFragmentInteractionListener,TrainScheduleFragment.OnFragmentInteractionListener, PassengerHomeFragment.OnFragmentInteractionListener, QRScanFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, ProfileEditFragment.OnFragmentInteractionListener, ProfileUpdateImageBottomSheetFragment.OnFragmentInteractionListener, StaffShareLocationFragment.OnFragmentInteractionListener, StaffSettingFragment.OnFragmentInteractionListener, StaffNotificationFragment.OnFragmentInteractionListener, StaffHomeFragment.OnFragmentInteractionListener {
    @BindView(R.id.my_toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_find_train:
                    title.setText("Train Schedule");
                    toolbar.setNavigationIcon(null);
                    fragment = new PassengerHomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_share_location:
                    title.setText("Share Location");
                    toolbar.setNavigationIcon(null);
                    fragment = new StaffShareLocationFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_more_driver:
                case R.id.action_more_checker:
                    title.setText("Profile");
                    toolbar.setNavigationIcon(null);
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_scan:
                    title.setText("Scan");
                    toolbar.setNavigationIcon(null);
                    fragment = new QRScanFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_find_train_for_share:
                    title.setText("Find Train");
                    toolbar.setNavigationIcon(null);
                    fragment = new SearchTrainForShareLocationFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (retrieveObjectFromSharedPreferences().getUser_type().equals("train_driver")) {
            setContentView(R.layout.activity_train_driver_home);
        } else if (retrieveObjectFromSharedPreferences().getUser_type().equals("ticket_checker")) {
            setContentView(R.layout.activity_ticket_checker_home);
        } else {

        }
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(null);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        getSupportActionBar().setTitle(null);
        if (retrieveObjectFromSharedPreferences().getUser_type().equals("train_driver")) {
            selectSpecificTab(1);
        } else if (retrieveObjectFromSharedPreferences().getUser_type().equals("ticket_checker")) {
            selectSpecificTab(4);
        }

    }

    public LoginResponse retrieveObjectFromSharedPreferences() {
        SharedPreferences preferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        System.out.println("======================>>>>>>>>>>>>>>" + preferences.contains(IPreferencesKeys.ACCESS_TOKEN) + "<<<<<<<<<<<<<<============================1");
        Gson gson = new Gson();
        String json = preferences.getString(IPreferencesKeys.USER_INFO, null);
        return gson.fromJson(json, LoginResponse.class);
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_staff_home, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();


    }

    public void loadFragment(Fragment fragment, Bundle args, String currentFragment) {
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(currentFragment);
        transaction.replace(R.id.frame_layout_staff_home, fragment);
        transaction.commit();
    }

    public String findId(int key) {
        List<Integer> searchStylist = new ArrayList<Integer>();

        searchStylist.add(key);


        Type listType = new TypeToken<List<String>>() {
        }.getType();

        Gson gson = new Gson();
        String json = gson.toJson(searchStylist, listType);

        return json;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void changeNavigation(String fragment) {

    }

//    @Override
//    public void onBackPressed(){
////        FragmentManager fm = getFragmentManager();
////        if (fm.getBackStackEntryCount() > 1) {
////            Log.i("MainActivity", "popping backstack");
////            fm.popBackStack();
////        } else {
////            Log.i("MainActivity", "nothing on backstack, calling super");
//            super.onBackPressed();
////        }
//    }


    public void selectSpecificTab(int tabId) {
        switch (tabId) {
            case 0:
                navigation.setSelectedItemId(R.id.action_find_train);
                break;
            case 1:
                navigation.setSelectedItemId(R.id.action_share_location);
                break;
            case 2:
                navigation.setSelectedItemId(R.id.action_more);
                break;
            case 3:
                Drawable mDrawable = getResources().getDrawable(R.drawable.back_arrow);
                mDrawable.setColorFilter(new
                        PorterDuffColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY));
//                title.setText("Profile");
                toolbar.setNavigationIcon(R.drawable.back_arrow);
                break;
            case 4:
                navigation.setSelectedItemId(R.id.action_scan);
                break;
            case 5:
                navigation.setSelectedItemId(R.id.action_find_train_for_share);
        }
    }

    public void navigationVisible() {
        navigation.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initializePresenter() {

    }
}
