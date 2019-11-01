package com.dhammika_dev.justgo.mvp.views;

public interface View {
    void showMessage(String message);

    void showErrorMessage(String calledMethod, String error, String errorDescription);
}
