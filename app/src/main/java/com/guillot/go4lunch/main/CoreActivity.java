package com.guillot.go4lunch.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.guillot.go4lunch.BuildConfig;
import com.guillot.go4lunch.list.RestaurantListFragment;
import com.guillot.go4lunch.maps.RestaurantMapFragment;
import com.guillot.go4lunch.mate.MatesFragment;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ActivityCoreBinding;
import com.guillot.go4lunch.restaurantDetails.RestaurantDetailActivity;

import java.util.Arrays;
import java.util.List;


public class CoreActivity extends AppCompatActivity {
    private ActivityCoreBinding binding;
    private static int AUTOCOMPLETE_REQUEST_CODE = 12;
    public final static String RESTAURANT = "RESTAURANT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        viewBinding();
        bottomViewListener();

        Places.initialize(getApplicationContext(), BuildConfig.ApiPlaceKey);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RestaurantMapFragment()).commit();
    }

    private void viewBinding() {
        binding = ActivityCoreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_logo:
                initAutoComplete();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void initAutoComplete() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.TYPES);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Intent", "onActivityResult: "+ data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place requestPlace = Autocomplete.getPlaceFromIntent(data);
                Log.d("Intent", "onActivityResult: "+ requestPlace);
                if (requestPlace.getTypes() != null) {
                    for (Place.Type type : requestPlace.getTypes()) {
                        if(type == Place.Type.RESTAURANT) {
                            Intent detailIntent = new Intent(this, RestaurantDetailActivity.class);
                            detailIntent.putExtra(RESTAURANT, requestPlace.getId());
                            Log.d("Intent", "start intent " + requestPlace.getName());
                            startActivity(detailIntent);
                        }

                    }
                }
            }
        }
    }

}