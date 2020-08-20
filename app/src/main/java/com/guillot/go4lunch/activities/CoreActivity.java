package com.guillot.go4lunch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.guillot.go4lunch.CONSTANTS;
import com.guillot.go4lunch.MapsFragment;
import com.guillot.go4lunch.MatesFragment;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.authentication.User;
import com.guillot.go4lunch.databinding.ActivityCoreBinding;

import java.util.Objects;


public class CoreActivity extends AppCompatActivity {
    private ActivityCoreBinding binding;
    private User user;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        viewBinding();
        bottomViewListener();
        getUser();

        Places.initialize(getApplicationContext(), CONSTANTS.GOOGLE_API_KEY);
        PlacesClient placesClient = Places.createClient(this);

//        Intent intent = getIntent();
//        user = intent.getParcelableExtra(CONSTANTS.USER_INTENT);
        Log.d("user", "user coreActivity: " + user);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsFragment(user)).commit();
    }

    private void viewBinding() {
        binding = ActivityCoreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void bottomViewListener() {
        binding.bottomView.setOnNavigationItemSelectedListener(item -> loadFragment(item.getItemId()));
    }

    private boolean loadFragment(int itemId) {
        Fragment selectedFragment = null;
        switch (itemId) {
            case R.id.map_logo:
                selectedFragment = new MapsFragment(user);
                break;
            case R.id.list_logo:
                selectedFragment = new ListFragment();
                break;
            case R.id.mates_logo:
                selectedFragment = new MatesFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();

        return true;
    }

    private void getUser() {
        mSharedPreferences = getPreferences(0);
        String userId = mSharedPreferences.getString(CONSTANTS.USER_ID, "");
        String userUsername = mSharedPreferences.getString(CONSTANTS.USER_USERNAME, "");
        Uri userProfilePicture = Uri.parse(mSharedPreferences.getString(CONSTANTS.URL_PROFILE_PICTURE, ""));

//        String[] latLng = mSharedPreferences.getString(CONSTANTS.USER_LOCATION, "").split(",");
//        double latitude = Double.parseDouble(latLng[0]);
//        double longitude = Double.parseDouble(latLng[1]);

        LatLng userLocation = new LatLng(45.833641, 6.864594);

        user = new User(userId, userUsername, userProfilePicture, userLocation);
    }


//    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.8534,2.3488&radius=1000&types=restaurant&key=AIzaSyB3o6so9QZ4VMEXE96QQx1ctsWAe7nlIGk
}