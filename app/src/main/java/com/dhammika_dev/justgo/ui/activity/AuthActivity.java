package com.dhammika_dev.justgo.ui.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.ui.fragments.LogInFragment;
import com.dhammika_dev.justgo.ui.fragments.PasswordResetEmailEnterFragment;
import com.dhammika_dev.justgo.ui.fragments.PasswordResetFragment;
import com.dhammika_dev.justgo.ui.fragments.PasswordResetVerificationFragment;
import com.dhammika_dev.justgo.ui.fragments.ProPicBottomSheetFragment;
import com.dhammika_dev.justgo.ui.fragments.SignUpContinueProfileFragment;
import com.dhammika_dev.justgo.ui.fragments.SignUpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.airbnb.deeplinkdispatch.DeepLink;

public class AuthActivity extends BaseActivity implements ProPicBottomSheetFragment.OnFragmentInteractionListener, SignUpContinueProfileFragment.OnFragmentInteractionListener, PasswordResetFragment.OnFragmentInteractionListener, PasswordResetVerificationFragment.OnFragmentInteractionListener, PasswordResetEmailEnterFragment.OnFragmentInteractionListener, LogInFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener {

    @BindView(R.id.my_toolbar1)
    Toolbar toolbar;
    String selectedUserType;
    String intentUri;
    String resetToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        selectedUserType = getIntent().getStringExtra("selectedUserType");

        Drawable mDrawable15 = this.getResources().getDrawable(R.drawable.ic_back);
        mDrawable15.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY));
        loadFragment(new LogInFragment());

        readUriData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(AuthActivity.this, HomeActivity.class);
                finish();
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        Bundle bundle = new Bundle();
        bundle.putString("selectedUserType", selectedUserType);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame_layout_auth, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void readUriData() {
        Uri data = getIntent().getData();
        if (data != null) {
            intentUri = data.toString();
            resetToken = intentUri.substring(intentUri.lastIndexOf("/") + 1);
            if (resetToken.length() > 5) {
                System.out.println("==============>>>>> verification code" + resetToken);
                Bundle bundle = new Bundle();
                bundle.putString("resetToken", resetToken);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment fragment = new PasswordResetVerificationFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.frame_layout_auth, fragment);
//        transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void initializePresenter() {

    }
}
