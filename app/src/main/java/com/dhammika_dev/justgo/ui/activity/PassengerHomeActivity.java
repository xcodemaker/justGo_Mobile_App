package com.dhammika_dev.justgo.ui.activity;

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
import com.dhammika_dev.justgo.ui.fragments.CardPaymentFragment;
import com.dhammika_dev.justgo.ui.fragments.LiveTrainListFragment;
import com.dhammika_dev.justgo.ui.fragments.MyBookingsFragment;
import com.dhammika_dev.justgo.ui.fragments.PassengerHomeFragment;
import com.dhammika_dev.justgo.ui.fragments.PassengerNotificationFragment;
import com.dhammika_dev.justgo.ui.fragments.PaymentFragment;
import com.dhammika_dev.justgo.ui.fragments.ProfileEditFragment;
import com.dhammika_dev.justgo.ui.fragments.ProfileFragment;
import com.dhammika_dev.justgo.ui.fragments.ProfileUpdateImageBottomSheetFragment;
import com.dhammika_dev.justgo.ui.fragments.SearchBookingFragment;
import com.dhammika_dev.justgo.ui.fragments.TicketBookingConfirmFragment;
import com.dhammika_dev.justgo.ui.fragments.TicketBookingFragment;
import com.dhammika_dev.justgo.ui.fragments.TicketViewFragment;
import com.dhammika_dev.justgo.ui.fragments.TrainScheduleFragment;
import com.dhammika_dev.justgo.ui.fragments.ViewCurrentLocationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PassengerHomeActivity extends BaseActivity implements LiveTrainListFragment.OnFragmentInteractionListener,ProfileEditFragment.OnFragmentInteractionListener, ProfileUpdateImageBottomSheetFragment.OnFragmentInteractionListener, TicketViewFragment.OnFragmentInteractionListener, CardPaymentFragment.OnFragmentInteractionListener, PaymentFragment.OnFragmentInteractionListener, TicketBookingConfirmFragment.OnFragmentInteractionListener, ViewCurrentLocationFragment.OnFragmentInteractionListener, TicketBookingFragment.OnFragmentInteractionListener, PassengerHomeFragment.OnFragmentInteractionListener, PassengerNotificationFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, MyBookingsFragment.OnFragmentInteractionListener, TrainScheduleFragment.OnFragmentInteractionListener, SearchBookingFragment.OnFragmentInteractionListener {

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView navigation;
//    @BindView(R.id.back_button)
//    ImageView backBtn;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_train_schedule:
                    title.setText("Train Schedule");
                    toolbar.setNavigationIcon(null);
                    fragment = new PassengerHomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_ticket_booking:
                    title.setText("Ticket Booking");
                    toolbar.setNavigationIcon(null);
                    fragment = new SearchBookingFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_live_location:
                    title.setText("Live Location");
                    toolbar.setNavigationIcon(null);
                    loadFragment(new LiveTrainListFragment());
                    return true;
                case R.id.action_my_booking:
                    title.setText("My Booking");
                    toolbar.setNavigationIcon(null);
                    fragment = new MyBookingsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_more:
                    title.setText("Profile");
                    toolbar.setNavigationIcon(null);
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(null);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        getSupportActionBar().setTitle(null);
//        title.setText("Feed");
//        loadFragment(new AllPostFragment());
        loadFragment(new PassengerHomeFragment());
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_passenger_home, fragment);
//        transaction.addToBackStack("PassengerHomeActivity");
        transaction.commit();
    }

    public void loadFragment(Fragment fragment, Bundle args, String currentFragment) {
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(currentFragment);
        transaction.replace(R.id.frame_layout_passenger_home, fragment);
        transaction.commit();
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
                navigation.setSelectedItemId(R.id.action_train_schedule);
                break;
            case 1:
                navigation.setSelectedItemId(R.id.action_ticket_booking);
                break;
            case 2:
                navigation.setSelectedItemId(R.id.action_live_location);
                break;
            case 3:
                navigation.setSelectedItemId(R.id.action_my_booking);
                break;
            case 4:
                navigation.setSelectedItemId(R.id.action_more);
                break;
            case 5:
                Drawable mDrawable = getResources().getDrawable(R.drawable.back_arrow);
                mDrawable.setColorFilter(new
                        PorterDuffColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY));
//                title.setText("Profile");
                toolbar.setNavigationIcon(R.drawable.back_arrow);
                break;
        }
    }

    public void navigationVisible() {
        navigation.setVisibility(View.VISIBLE);
    }

    public void hideBackButton(boolean hide) {
//        if(hide){
//            backBtn.setVisibility(View.GONE);
//        }else {
//            backBtn.setVisibility(View.VISIBLE);
//        }
    }

//    @OnClick(R.id.back_button)
//    void onBackButtonPressed(){
//        FragmentManager fm = getFragmentManager();
//        System.out.println("back button pressed" + fm.getBackStackEntryCount());
//        if (fm.getBackStackEntryCount() > 0) {
//            Log.i("MainActivity", "popping backstack");
//            fm.popBackStack();
//        }
//    }

    @Override
    protected void initializePresenter() {

    }
}
