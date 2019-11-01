package com.dhammika_dev.justgo.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dhammika_dev.justgo.model.rest.JustGoAPIService;
import com.dhammika_dev.justgo.mvp.presenters.Presenter;
import com.dhammika_dev.justgo.utils.IScheduler;

public abstract class BaseActivity extends AppCompatActivity {

//    protected Presenter presenter;

    public static BaseActivity baseActivity = null;
    public static AlertDialog myAlertDialogTwo;
    public static AlertDialog myAlertDialog;
    public static AlertDialog myAlertDialogOne;
    protected JustGoAPIService oacService;
    protected IScheduler scheduler;
    protected Presenter presenter;
    AlertDialog alertDialog = null;
    private ProgressDialog progressDialog = null;

    protected abstract void initializePresenter();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        baseActivity = this;
    }

    public void setLoading(boolean flag) {
        if (flag) {
            if (progressDialog != null) progressDialog.show();
            else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Its loading.......");
                progressDialog.setTitle("Please Wait....");
                progressDialog.show();
            }
        } else {
            if (progressDialog != null) progressDialog.dismiss();
        }
    }

    protected void showTopSnackBar(String message, int bColor) {
        Snackbar snack = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snack.getView();
        snackbarView.setBackgroundColor(bColor);
        snack.show();
    }

    public void showAlertDialog(boolean setCancelable, String title, String message, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setCancelable(setCancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", positiveListener);
        if (!this.isFinishing()) myAlertDialogTwo = alertDialog.show();
    }


}
