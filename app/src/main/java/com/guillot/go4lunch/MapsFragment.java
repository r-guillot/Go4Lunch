package com.guillot.go4lunch;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.authentication.User;
import com.guillot.go4lunch.viewmodel.UserLocationViewModel;

import java.util.Objects;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {

    private UserLocationViewModel mViewModel;
    private Context context;
    private GoogleMap mMap;
    private View mView;
    private MapView mMapView;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng userLocation;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.blank_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        // init map
        mMapView = mView.findViewById(R.id.fragment_map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(UserLocationViewModel.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mViewModel.locationUserUpdate.observe(this, locationUser -> {
        locationUpdate();

        enableLocationUser(mMap);
        locationButtonDesign();
    }

    private void enableLocationUser(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            locationUpdate();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    CONSTANTS.REQUEST_LOCATION_PERMISSION);
        }
        locationUpdate();
    }

    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(context, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @SuppressLint("MissingPermission")
    private void locationUpdate() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18f));

//                mViewModel.setLocationUserLiveData(userLocation);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void locationButtonDesign() {
        View locationButton = ((View) mView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

        RelativeLayout.LayoutParams rLP = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rLP.setMargins(0,0,30,180);
    }

}