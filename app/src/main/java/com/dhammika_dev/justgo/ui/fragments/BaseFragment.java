package com.dhammika_dev.justgo.ui.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.mvp.presenters.Presenter;
import com.dhammika_dev.justgo.utils.AppScheduler;
import com.dhammika_dev.justgo.utils.IScheduler;


public abstract class BaseFragment extends Fragment {

    public static AlertDialog myAlertDialog;
    public static AlertDialog myAlertDialogOne;
    public static AlertDialog myAlertDialogTwo;
    protected JustGoAPIService oacService;
    protected IScheduler scheduler;
    protected Presenter presenter;
    AlertDialog alertDialog = null;
    private ProgressDialog progressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduler = new AppScheduler();
        oacService = new JustGoAPIService();
        initializePresenter();
        if (presenter != null) presenter.onCreate();
    }

    protected abstract void initializePresenter();

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) presenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.onStart();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    public void setLoading(boolean flag) {
        if (flag) {
            if (progressDialog != null) progressDialog.show();
            else {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Its loading.......");
                progressDialog.setTitle("Please Wait....");
                progressDialog.show();
            }
        } else {
            if (progressDialog != null) progressDialog.dismiss();
        }
    }

    protected void showAlertDialog(boolean setCancelable, String title, String message, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(setCancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", positiveListener);
        if (!getActivity().isFinishing()) myAlertDialogTwo = alertDialog.show();
    }

    protected void showAlertDialogAdvanced(boolean setCancelable, String title, String message, String positiveBtnTxt, String negativeBtnTxt,
                                           DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        if (myAlertDialog != null && myAlertDialog.isShowing()) return;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setCancelable(setCancelable);

        String titleTxt = (title != null) ? title : "Warning";
        String messageTxt = (message != null) ? message : "";
        alertDialog.setTitle(titleTxt);
        alertDialog.setMessage(messageTxt);

        if (positiveBtnTxt != null) {
            DialogInterface.OnClickListener positiveClickListener = (positiveListener != null) ? positiveListener : defaultDialogClickListener();
            alertDialog.setPositiveButton(positiveBtnTxt, positiveClickListener);
        }

        if (negativeBtnTxt != null) {
            DialogInterface.OnClickListener negativeClickListener = (negativeListener != null) ? negativeListener : defaultDialogClickListener();
            alertDialog.setNegativeButton(negativeBtnTxt, negativeClickListener);
        }

        myAlertDialog = alertDialog.create();
        myAlertDialogOne = myAlertDialog;
        if (!getActivity().isFinishing()) alertDialog.show();
    }

    protected DialogInterface.OnClickListener defaultDialogClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        };
    }

    protected void showTopSnackBar(String message, int bColor) {
        Snackbar snack = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snack.getView();
        snackbarView.setBackgroundColor(bColor);
        snack.show();
    }

    public void setIconColors() {


    }


}

