package com.cs407.landmarkapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

// TODO: create database of badges and show them on map; camera button and function; AR

// TODO: constantly update current location and calculate distance away from markers
public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private MapView mapView;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private ArrayList<MarkerOptions> badgeList = new ArrayList<>();
    private LatLng mLastKnownLocationLatLng;
    private Location mCurrentLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        SearchView searchView = view.findViewById(R.id.searchView);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        int permission = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            try {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }

        LatLng CSBuilding = new LatLng(43.07122779948943, -89.40657139659501);
        MarkerOptions CSBlgBadge = new MarkerOptions().title("CS Building").position(CSBuilding)
                .icon(BitmapFromVector(getContext(), R.drawable.uw_cs_building_badge));
        googleMap.addMarker(CSBlgBadge);

        LatLng UnionSouth = new LatLng(43.072100761939716, -89.4076678353536);
        MarkerOptions USouthBadge = new MarkerOptions().title("Union South").position(UnionSouth)
                .icon(BitmapFromVector(getContext(), R.drawable.union_south_badge));
        googleMap.addMarker(USouthBadge);

        badgeList.add(CSBlgBadge);
        badgeList.add(USouthBadge);

        displayMyLocation();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void displayMyLocation() {
        int permission = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                    task -> {
                        Location mLastKnownLocation = task.getResult();
                        if (task.isSuccessful() && mLastKnownLocation != null) {
                            mLastKnownLocationLatLng =
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(mLastKnownLocationLatLng).title("Current"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLastKnownLocationLatLng, 10));
                            mCurrentLocation = mLastKnownLocation;
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // generates a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // sets bounds to vector drawable
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());

        // creates bitmap for drawable
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        // adds bitmap in canvas
        Canvas canvas = new Canvas(bitmap);

        // draws vector drawable in canvas
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /*protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.Builder()
                .setIntervalMillis(10000)
                .setFastestIntervalMillis(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build();
    }*/

}