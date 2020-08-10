package com.guillot.go4lunch.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.places.model.PlaceSearchRequestParams;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ActivityMapBinding;

import org.json.JSONObject;

public class CoreActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    private ActivityMapBinding binding;
    private GoogleMap mMap;
    private static final int REQUEST_LOCATION_PERMISSION = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        viewBinding();

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(),"AIzaSyB3o6so9QZ4VMEXE96QQx1ctsWAe7nlIGk");
        PlacesClient placesClient = Places.createClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng paris = new LatLng(48.8534, 2.3488);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 18f));

        enableLocationUser(mMap);
    }

    private void enableLocationUser(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }


//    private void viewBinding() {
//        binding = ActivityMapBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
//    }

//    private void bottomViewListener() {
//        binding.bottomView.OnNavigationItemSelectedListener();
//    }

//    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.8534,2.3488&radius=1000&types=restaurant&key=AIzaSyB3o6so9QZ4VMEXE96QQx1ctsWAe7nlIGk
}