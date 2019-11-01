package com.dhammika_dev.justgo.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhammika_dev.justgo.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class ViewCurrentLocationFragment extends BaseFragment implements OnMapReadyCallback {

    private static final String TAG = ViewCurrentLocationFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST = 1;
    public Location loc;
    GoogleMap mgoogleMap;
    MapView mapView;
    View view;
    String trainId ;
    FirebaseFirestore db;
    PolylineOptions polyLineOptions;
    Marker trainMarker;
    Marker userMarker;
    List<LatLng> sourcePoints = new ArrayList<>(2);

    LatLng trainLocation;
    boolean isTrainOnPath = false;
    FusedLocationProviderClient client;

    public ViewCurrentLocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_current_location, container, false);
        mapView = view.findViewById(R.id.map);
        db = FirebaseFirestore.getInstance();
        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getContext(), "Please enable location services", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        trainId = getArguments().getString("trainId");
        return view;
    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());

        mgoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        polyLineOptions = new PolylineOptions();
        checkAuth();
        getRailWayLine("coastalLine", Color.parseColor("#3F5BA9"));
        getRailWayLine("kelaniValleyLine", Color.parseColor("#C6A4CF"));
        getRailWayLine("mainLine", Color.parseColor("#009D57"));
        getRailWayLine("mataleLine", Color.parseColor("#7CCFA9"));
        getRailWayLine("puttalamLine", Color.parseColor("#EE9C96"));
        getRailWayLine("northernLine", Color.parseColor("#795046"));
        getRailWayLine("batticaloaLine", Color.parseColor("#F8971B"));
        getRailWayLine("trincomaleeLine", Color.parseColor("#F4B400"));
        getRailWayLine("mannarLine", Color.parseColor("#B29189"));

        requestLocationUpdates();

    }


    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client = LocationServices.getFusedLocationProviderClient(getContext());
        int permission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, mLocationCallback, null);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            if (location != null) {
                if (location != null) {
                    System.out.println("============= current location " + String.format(Locale.US, "%s -- %s", location.getLatitude(), location.getLongitude()));
                    if(!isTrainOnPath)mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                    View mCustomMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_user_marker, null);
                    ImageView imageView = mCustomMarkerView.findViewById(R.id.profile_image);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.ic_person_black_24dp);
                    requestOptions.error(R.drawable.ic_error);
                    Glide.with(getContext())
                            .load("https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50.jpg")
                            .apply(requestOptions)
                            .into(imageView);
                    if (userMarker == null) {
                        userMarker = mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You").draggable(true).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, 0))));
                    } else {
                        userMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.removeLocationUpdates(mLocationCallback);
        }
    }

    private void checkAuth() {
        subcribeToLocationChanges();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST);
            return;
        }

        subcribeToLocationChanges();
    }


    private Bitmap getMarkerBitmapFromView(View view, @DrawableRes int resId) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void subcribeToLocationChanges() {
        String path = "trains/"+trainId;
        if(trainId.isEmpty()){return;}
        //TODO:replace hardcoded path with path
        final DocumentReference docRef = db.document(path);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                Map<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                if (data != null && snapshot.exists()) {
                    Log.e("cl", snapshot.toString());
                    setMarker(data);
                } else {
                    Toast.makeText(getActivity(), "No data available", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    public void getRailWayLine(String railLineName, int color) {
        String path = "rail_lines/" + railLineName;
        final DocumentReference docRef = db.document(path);
        final int colorValue = color;
        final List<LatLng> polyline = new ArrayList<>();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> railLine = (HashMap<String, Object>) document.getData();
                        List<GeoPoint> geoPointLine = (List<GeoPoint>) railLine.get("coordinates");

                        for (int i = 0; i < geoPointLine.size(); i++) {
                            LatLng temp = new LatLng(geoPointLine.get(i).getLatitude(), geoPointLine.get(i).getLongitude());
                            polyline.add(temp);
                        }
                        if (railLineName.equals("coastalLine")) {
                            sourcePoints.addAll(polyline);
                        }
                        mgoogleMap.addPolyline(new PolylineOptions().addAll(polyline).color(colorValue));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setMarker(Map<String, Object> data) {
        try {
            GeoPoint geoPoint = (GeoPoint) data.get("location");
            trainLocation = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
            Log.d(TAG, "pointer at [" + trainLocation.latitude + "," + trainLocation.longitude + "]");
            for (int i = 0; i < sourcePoints.size() - 1; i++) {
                LatLng segmentP1 = sourcePoints.get(i);
                LatLng segmentP2 = sourcePoints.get(i + 1);
                List<LatLng> segment = new ArrayList<>(2);
                segment.add(segmentP1);
                segment.add(segmentP2);

                if (PolyUtil.isLocationOnPath(trainLocation, segment, true, 300)) {
                    polyLineOptions = new PolylineOptions();
                    polyLineOptions.addAll(segment);
                    polyLineOptions.width(10);
                    polyLineOptions.color(Color.RED);
                    mgoogleMap.addPolyline(polyLineOptions);
                    LatLng snappedToSegment = getMarkerProjectionOnSegment(trainLocation, segment, mgoogleMap.getProjection());
                    addMarker(snappedToSegment);
                    mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(snappedToSegment, 15));
                    isTrainOnPath =true;
                    break;
                }
            }
        } catch (ClassCastException e) {
            Log.d(TAG, "current_location is not a map");
        }
    }

    private LatLng getMarkerProjectionOnSegment(LatLng carPos, List<LatLng> segment, Projection projection) {
        LatLng markerProjection = null;

        Point carPosOnScreen = projection.toScreenLocation(carPos);
        Point p1 = projection.toScreenLocation(segment.get(0));
        Point p2 = projection.toScreenLocation(segment.get(1));
        Point carPosOnSegment = new Point();

        float denominator = (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y);
        // p1 and p2 are the same
        if (Math.abs(denominator) <= 1E-10) {
            markerProjection = segment.get(0);
        } else {
            float t = (carPosOnScreen.x * (p2.x - p1.x) - (p2.x - p1.x) * p1.x
                    + carPosOnScreen.y * (p2.y - p1.y) - (p2.y - p1.y) * p1.y) / denominator;
            carPosOnSegment.x = (int) (p1.x + (p2.x - p1.x) * t);
            carPosOnSegment.y = (int) (p1.y + (p2.y - p1.y) * t);
            markerProjection = projection.fromScreenLocation(carPosOnSegment);
        }
        return markerProjection;
    }

    public void addMarker(LatLng latLng) {
        if (trainMarker == null) {
            trainMarker = mgoogleMap.addMarker(new MarkerOptions()
                    .position(latLng).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_tracker))));
        } else {
            trainMarker.setPosition(latLng);
        }
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
