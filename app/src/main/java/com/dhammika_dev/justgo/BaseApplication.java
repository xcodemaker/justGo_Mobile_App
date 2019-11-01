package com.dhammika_dev.justgo;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.request.target.ViewTarget;
import com.dhammika_dev.justgo.common.constants.IPreferencesKeys;
import com.dhammika_dev.justgo.ui.activity.BaseActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;


public class BaseApplication extends Application {

    public final static String GOOGLE_KEY = "AIzaSyA8BPU-M41iFHzA8zRpn2RSQGBoq6Ahzd0";
    public static AlertDialog myAlertDialogTwo;
    private static BaseApplication baseApplication;
    private final String TAG = BaseApplication.this.getClass().getSimpleName();
    KeyStore keyStore;
    private SharedPreferences preferences;


    public BaseApplication() {
        super();
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = (BaseApplication) getApplicationContext();
        preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        ViewTarget.setTagId(R.id.glide_tag);

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (Exception e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void createNewKeys(String alias) {
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this)
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);

                KeyPair keyPair = generator.generateKeyPair();
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void deleteKey(final String alias) {
        try {
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public String encryptString(String alias, String initialText) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] vals = outputStream.toByteArray();

            return Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public String decryptString(String alias, String cipherText) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            return finalText;

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void saveEncryptPasswordAndUserName(String userName, String password) {
        deleteKey(IPreferencesKeys.JUST_GO_SAVE_USER_NAME);
        deleteKey(IPreferencesKeys.JUST_GO_SAVE_PASSWORD);

        createNewKeys(IPreferencesKeys.JUST_GO_SAVE_USER_NAME);
        createNewKeys(IPreferencesKeys.JUST_GO_SAVE_PASSWORD);

        if (!userName.isEmpty()) {
            String encryptUserName = encryptString(IPreferencesKeys.JUST_GO_SAVE_USER_NAME, userName);
            if (encryptUserName != null && !encryptUserName.isEmpty())
                preferences.edit().putString(IPreferencesKeys.JUST_GO_SAVE_USER_NAME, encryptUserName).apply();
        }
        if (!password.isEmpty()) {
            String encryptPassword = encryptString(IPreferencesKeys.JUST_GO_SAVE_PASSWORD, password);
            if (encryptPassword != null && !encryptPassword.isEmpty())
                preferences.edit().putString(IPreferencesKeys.JUST_GO_SAVE_PASSWORD, encryptPassword).apply();
        }
    }

    public String getDecryptString(String alias) {
        String cipherText = preferences.getString(alias, null);
        if (cipherText != null && !cipherText.isEmpty()) {
            return decryptString(alias, cipherText);
        }
        return null;
    }

    public void showAlertDialog(boolean setCancelable, String title, String message, DialogInterface.OnClickListener positiveListener) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder()
//                .setCancelable(setCancelable)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton("Ok", positiveListener);
////        if (!this.isFinishing())
//            myAlertDialogTwo = alertDialog.show();
        BaseActivity.baseActivity.showAlertDialog(setCancelable, title, message, positiveListener);
    }

    public void clearData() {
        clearUserData();
    }

    private void clearUserData() {
        preferences.edit().remove(IPreferencesKeys.USER_INFO).apply();
        preferences.edit().remove(IPreferencesKeys.ACCESS_TOKEN).apply();
        preferences.edit().remove(IPreferencesKeys.CATALOG_LIST).apply();
        preferences.edit().remove(IPreferencesKeys.IS_VERIFICATION_CODE_SEND).apply();
        preferences.edit().remove(IPreferencesKeys.IS_FORGOT_PASSWORD).apply();
        preferences.edit().remove(IPreferencesKeys.CONFIG_DETAIL).apply();
        preferences.edit().remove(IPreferencesKeys.CUSTOMER_INFO).apply();
        preferences.edit().remove(IPreferencesKeys.IS_SET_CUSTOMER).apply();
        preferences.edit().remove(IPreferencesKeys.CONTRACTOR_INFO).apply();
        preferences.edit().remove(IPreferencesKeys.CUSTOMER_SEARCH__NAME_FILTER).apply();
        preferences.edit().remove(IPreferencesKeys.CUSTOMER_SEARCH__REGION_FILTER).apply();
    }


}