package com.guillot.go4lunch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.Restaurant.Restaurant;
import com.guillot.go4lunch.Restaurant.RestaurantViewModel;
import com.guillot.go4lunch.maps.MapsFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment extends Fragment {
    public Context context;
    public FusedLocationProviderClient mFusedLocationClient;
    public LatLng LocationUser;
    public RestaurantViewModel mRestaurantViewModel;
    public GoogleMap mMap;
    public List<Restaurant> restaurantListRV;

    public int radius = 500;
    public String type = "restaurant";
    public String key = "AIzaSyB3o6so9QZ4VMEXE96QQx1ctsWAe7nlIGk";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        initViewModel();
//        createList();

        assert context != null;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableLocationUser(mMap);
        locationUpdate();
    }

    public void initViewModel() {
        mRestaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

    public void enableLocationUser(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            locationUpdate();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    CONSTANTS.REQUEST_LOCATION_PERMISSION);
        }
    }

    @SuppressLint("MissingPermission")
    public void locationUpdate() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                Log.d("list", "location :" + location);
                LocationUser = new LatLng(location.getLatitude(), location.getLongitude());
                // TODO: 27/08/2020 check if i can create to string with lat and long
                Log.d("list", "parametre :" + LocationUser);

                mRestaurantViewModel.setRetrofit(LocationUser, radius, type, key);
                Log.d("list", "parametre fragment :" + LocationUser + " " + radius + " " + type + " " + key);

                mRestaurantViewModel.getRestaurants();
                mRestaurantViewModel.RestaurantListLiveData.observe(this, liveDataListRestaurant -> {
                    if (liveDataListRestaurant != null) {
                        restaurantListRV = liveDataListRestaurant;
                        Log.d("locationUpdate", "list size base " + restaurantListRV.size());
                    }
                });
            }
        });
    }

//    public void createList() {
//        mRestaurantViewModel.RestaurantLiveData.observe(this, markerRestaurant -> {
//            if (markerRestaurant != null) {
//                restaurantListRV.add(markerRestaurant);
//            }
//        });
//    }

}