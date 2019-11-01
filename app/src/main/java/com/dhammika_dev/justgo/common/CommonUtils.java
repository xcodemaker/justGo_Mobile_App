package com.dhammika_dev.justgo.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

public class CommonUtils {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static CommonUtils instance = null;

    private CommonUtils() {
    }

    public static CommonUtils getInstance() {
        if (instance == null) instance = new CommonUtils();
        return instance;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getBaseApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public int[] calculateScreenDimens() {
        WindowManager windowManager = (WindowManager) BaseApplication.getBaseApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int dimension[] = new int[2];
        dimension[0] = size.x;  //width
        dimension[1] = size.y;  //height

        return dimension;
    }

    public float dpToPx(float valueInDp) {
        try {
            DisplayMetrics metrics = BaseApplication.getBaseApplication().getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0f;
    }


    public String getPathFromUri(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] data = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
            cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPathFromUriGallery(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String sel = MediaStore.Images.Media._ID + "=?";

            String[] filePath = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePath, sel, new String[]{id}, null);
//            context.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
//                    column, sel, new String[]{id}, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            return cursor.getString(columnIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        System.out.println("=====================>>>>>>>>> cursor info  :" + cursor + "<<<<<<<<<<<<<=============================");
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if (idx == -1) {
                String wholeID = DocumentsContract.getDocumentId(contentURI);
                String id = wholeID.split(":")[1];
                String sel = MediaStore.Images.Media._ID + "=?";
                String[] filePath = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePath, sel, new String[]{id}, null);
                cursor.moveToFirst();
                idx = cursor.getColumnIndex(filePath[0]);
            }
            System.out.println("=====================>>>>>>>>> index info  :" + idx + "<<<<<<<<<<<<<=============================");
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    public boolean isEmptyString(String object) {
        return (object == null || object.trim().equals("null") || object.trim().length() <= 0);
    }

    public boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public SharedPreferences getSharedPreferences(Activity activity, String filter) {
        return activity.getSharedPreferences(filter, Context.MODE_PRIVATE);
    }

    public void setValueToSharedPreferences(SharedPreferences preference, String key, String value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //Get data from shared preference
    public String getValueFromSharedPreferences(SharedPreferences preference, String key) {
        return preference.getString(key, " ");
    }

    public boolean getKeyValueFromSharedPreferences(SharedPreferences preference, String name) {
        String bool = "false";
        Map<String, ?> keySet = preference.getAll();
        for (Map.Entry<String, ?> entry : keySet.entrySet()) {
            if (entry.getKey().equals(name)) {
                bool = ((String) entry.getValue());
            }
        }
        return bool.equals("true");
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String convertImageToBase64(String filePath) {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    public Bitmap convertBase64ToBitmap(String base64String) {
        Bitmap bmp = null;
        try {
            byte[] decodeString = Base64.decode(base64String, Base64.NO_WRAP);
            bmp = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }


    private File getPhotoDirectory(Activity activity) {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            outputDir = new File(photoDir, activity.getResources().getString(R.string.app_name));
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Toast.makeText(activity, "Failed to create directory " + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }
}
