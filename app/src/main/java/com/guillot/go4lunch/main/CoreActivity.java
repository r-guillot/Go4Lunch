package com.guillot.go4lunch.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.guillot.go4lunch.list.RestaurantListFragment;
import com.guillot.go4lunch.maps.RestaurantMapFragment;
import com.guillot.go4lunch.mate.MatesFragment;
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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RestaurantMapFragment()).commit();
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
                selectedFragment = new RestaurantMapFragment();
                break;
            case R.id.list_logo:
                selectedFragment = new RestaurantListFragment();
                break;
            case R.id.mates_logo:
                selectedFragment = new MatesFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();

        return true;
    }
}