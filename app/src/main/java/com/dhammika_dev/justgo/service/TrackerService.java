package com.dhammika_dev.justgo.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.ui.activity.StaffHomeActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class TrackerService extends Service {

    private static final String TAG = TrackerService.class.getSimpleName();
    private static String dbPath;
    String FromStation, ToStation, TrainName;
    private final IBinder mBinder = new LocalBinder();
    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };
    private FirebaseFirestore db;
    private NotificationManager mNM;
    private int NOTIFICATION = R.string.local_service_started;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
//        loginToFirebase();
        requestLocationUpdates();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbPath = intent.getStringExtra("db_path").trim();
        FromStation = intent.getStringExtra("fromStation").trim();
        ToStation = intent.getStringExtra("toStation").trim();
        TrainName = intent.getStringExtra("trainName").trim();
        Log.d("share-location", dbPath);
        System.out.println("=============================>>>>> onStartCommand called" + dbPath);
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_action_train_dark);
        startForeground(1, builder.build());
    }

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, StaffHomeActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_action_train_dark)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.local_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

//    private void loginToFirebase() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user!=null){
//            requestLocationUpdates();
//        }else{
//            Toast.makeText(getApplicationContext(),"Login first",Toast.LENGTH_LONG).show();
//        }
//    }

    private void requestLocationUpdates() {
        System.out.println("=============================>>>>> requestLocationUpdates called");
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            System.out.println("=============================>>>>> PERMISSION_OK");
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    Map<String, Object> locationData = new HashMap<>();
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        System.out.println("====================>>>>>>>>>> location update " + new GeoPoint(location.getLatitude(), location.getLongitude()));
                        locationData.put("location", new GeoPoint(location.getLatitude(), location.getLongitude()));
                        locationData.put("time", FieldValue.serverTimestamp());
                        locationData.put("FromStation", FromStation);
                        locationData.put("ToStation", ToStation);
                        locationData.put("TrainName",TrainName);
                        db.document(dbPath).set(locationData);
                    }
                }
            }, null);
        }
    }

    public class LocalBinder extends Binder {
        TrackerService getService() {
            return TrackerService.this;
        }
    }
}




