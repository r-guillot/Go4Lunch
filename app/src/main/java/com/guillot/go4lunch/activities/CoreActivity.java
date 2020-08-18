package com.guillot.go4lunch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.guillot.go4lunch.CONSTANTS;
import com.guillot.go4lunch.MapsFragment;
import com.guillot.go4lunch.MatesFragment;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ActivityCoreBinding;


public class CoreActivity extends AppCompatActivity {
    private ActivityCoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        viewBinding();
        bottomViewListener();

        Places.initialize(getApplicationContext(), CONSTANTS.GOOGLE_API_KEY);
        PlacesClient placesClient = Places.createClient(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsFragment()).commit();
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
                selectedFragment = new MapsFragment();
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


//    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.8534,2.3488&radius=1000&types=restaurant&key=AIzaSyB3o6so9QZ4VMEXE96QQx1ctsWAe7nlIGk
}