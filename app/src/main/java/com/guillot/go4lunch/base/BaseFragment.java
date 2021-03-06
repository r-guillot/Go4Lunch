package com.guillot.go4lunch.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.R;

import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    protected FusedLocationProviderClient fusedLocationClient;
    public static LatLng locationUser;

    protected static final String PERMS = Manifest.permission.ACCESS_FINE_LOCATION;
    protected static final int RC_LOCATION_PERMS = 100;

    public abstract void getLocationUser(LatLng locationUser);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
        fetchLastKnowLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
        fetchLastKnowLocation();
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION_PERMS)
    protected void fetchLastKnowLocation() {
        if (!EasyPermissions.hasPermissions(Objects.requireNonNull(getActivity()), PERMS)) {
            EasyPermissions.requestPermissions(
                    this, getString(R.string.need_permission_message), RC_LOCATION_PERMS, PERMS);
            return;
        }
        fusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(location -> {

                        locationUser = new LatLng(location.getLatitude(), location.getLongitude());
                        getLocationUser(locationUser);
                        }
                );
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_LOCATION_PERMS) {
            fetchLastKnowLocation();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    }
}