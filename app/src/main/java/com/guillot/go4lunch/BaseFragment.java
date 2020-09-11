package com.guillot.go4lunch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    public Context context;
    public FusedLocationProviderClient mFusedLocationClient;
    public static LatLng LocationUser;

    private final String TAG = BaseFragment.class.getSimpleName();

    // FOR GPS PERMISSION
    protected static final String PERMS = Manifest.permission.ACCESS_FINE_LOCATION;
    protected static final int RC_LOCATION_PERMS = 100;


    public int radius = 500;
    public String type = "restaurant";
    public String key = "AIzaSyB3o6so9QZ4VMEXE96QQx1ctsWAe7nlIGk";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        Log.d(TAG, "onCreate: ");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fetchLastKnowLocation();
    }

    // --------------------
    // GET LOCATION USER
    // --------------------

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION_PERMS)
    protected void fetchLastKnowLocation() {
        Log.d(TAG, "fetchLastKnowLocation");
        if (!EasyPermissions.hasPermissions(getActivity(), PERMS)) {
            EasyPermissions.requestPermissions(
                    this, getString(R.string.need_permission_message), RC_LOCATION_PERMS, PERMS);
            return;
        }

        mFusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(location -> {
                            Log.d(TAG, "onSuccess location: " + location);
                    LocationUser = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "parametre :" + LocationUser);
                        }
                );
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted : " + requestCode);
        if (requestCode == RC_LOCATION_PERMS) {
            fetchLastKnowLocation();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied ");
    }

}