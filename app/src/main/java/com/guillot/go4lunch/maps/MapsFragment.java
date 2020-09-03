package com.guillot.go4lunch.maps;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guillot.go4lunch.CONSTANTS;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.Restaurant.RestaurantViewModel;
import com.guillot.go4lunch.authentication.User;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {

    private RestaurantViewModel mRestaurantViewModel;
    private Context context;
    private GoogleMap mMap;
    private View mView;
    private MapView mMapView;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng userLocation;
    private User mUser;
    private int radius = 500;
    private String type = "restaurant";
    private String key = "AIzaSyB3o6so9QZ4VMEXE96QQx1ctsWAe7nlIGk";


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
        mView = inflater.inflate(R.layout.maps_fragment, container, false);
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
        mRestaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
                Log.d("list", "location :" + location);
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                // TODO: 27/08/2020 check if i can create to string with lat and long
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18f));
                Log.d("list", "parametre :" + userLocation);

                mRestaurantViewModel.setRetrofit(userLocation, radius, type, key);
                Log.d("list", "parametre fragment :" + userLocation + " " + radius + " " + type + " " + key);
                mRestaurantViewModel.getRestaurants();
                createMarker();
            }
        });
    }

    private void createMarker() {
        Bitmap markerRestaurantGreen = BitmapFactory.decodeResource(getResources(), R.drawable.marker_restaurant_green);
        markerRestaurantGreen = scaleBitmap(markerRestaurantGreen, 120, 70);
        Bitmap finalMarkerRestaurantGreen = markerRestaurantGreen;
        mRestaurantViewModel.RestaurantLiveData.observe(this, markerRestaurant -> {
            if (markerRestaurant != null) {
                double lat = markerRestaurant.getGeometry().getLocation().getLat();
                double lng = markerRestaurant.getGeometry().getLocation().getLng();
                LatLng positionRestaurant = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                        .position(positionRestaurant)
                        .title(markerRestaurant.getName())
                        .icon(BitmapDescriptorFactory.fromBitmap(finalMarkerRestaurantGreen)));
            }
        });
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
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