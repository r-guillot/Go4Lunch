package com.guillot.go4lunch.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.guillot.go4lunch.BuildConfig;
import com.guillot.go4lunch.api.NotificationHelper;
import com.guillot.go4lunch.authentication.SignInActivity;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.list.RestaurantListFragment;
import com.guillot.go4lunch.maps.RestaurantMapFragment;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ActivityCoreBinding;
import com.guillot.go4lunch.mates.MatesFragment;
import com.guillot.go4lunch.details.RestaurantDetailActivity;
import com.guillot.go4lunch.model.User;
import com.guillot.go4lunch.settings.SettingsActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityCoreBinding binding;
    private static int AUTOCOMPLETE_REQUEST_CODE = 12;
    private MainViewModel viewModel;
    private NotificationHelper notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        viewBinding();
        setSupportActionBar(binding.toolbar);
        notification = new NotificationHelper(this);
        initViewModel();
        notification.resetRestaurantData();
        notification.createNotificationChannel();
        viewModel.getCurrentUser();
        bottomViewListener();
        drawerMenu();

        Places.initialize(getApplicationContext(), BuildConfig.ApiPlaceKey);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RestaurantMapFragment()).commit();
    }

    @Override
    protected void onResume() {
        viewModel.getCurrentUser();
        super.onResume();
    }

    private void viewBinding() {
        binding = ActivityCoreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.init();
        setUpUser();
        viewModel.checkIfNotificationIsEnabled();
        initNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_logo) {
            initAutoComplete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void drawerMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.chosenRestaurant:
                if (!viewModel.getChosenRestaurant().equals("")) {
                    intent = new Intent(this, RestaurantDetailActivity.class);
                    intent.putExtra(Constants.RESTAURANT, viewModel.getChosenRestaurant());
                } else {
                    Toast.makeText(this, R.string.no_lunch, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.logout:
                viewModel.signOut();
                intent = new Intent(this, SignInActivity.class);
                finish();
                break;
        }
        if (intent != null){
        startActivity(intent);}
        this.binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpUser() {viewModel.getCurrentUserLiveData().observe(this, this::filNavHeader);}

    public void filNavHeader(User user) {
        ImageView userPic =(ImageView) binding.navView.getHeaderView(0).findViewById(R.id.profile_pic_image_view);
        if (user.getUrlProfilePicture() != null) {
            Glide.with(this)
                    .load(user.getUrlProfilePicture())
                    .centerCrop()
                    .circleCrop()
                    .into(userPic);
        } else {
            userPic.setImageResource(R.drawable.image_not_avaiable);
        }
        TextView userNameTextView = (TextView) binding.navView.getHeaderView(0).findViewById(R.id.name_text_view);
        userNameTextView.setText(user.getUsername());
        TextView userMailTextView = (TextView) binding.navView.getHeaderView(0).findViewById(R.id.mail_text_view);
        userMailTextView.setText(user.getUserMail());
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        assert selectedFragment != null;
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
        Log.d("Intent", "onActivityResult: " + data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place requestPlace = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));
                if (requestPlace.getTypes() != null) {
                        if (requestPlace.getTypes().contains(Place.Type.RESTAURANT)){
                            Intent detailIntent = new Intent(this, RestaurantDetailActivity.class);
                            detailIntent.putExtra(Constants.RESTAURANT, requestPlace.getId());
                            startActivity(detailIntent);
                        } else {
                            Toast.makeText(this, R.string.not_restaurant, Toast.LENGTH_SHORT).show();
                        }
                }
            }
        }
    }

    private void initNotification(){
        viewModel.getIsNotificationEnable().observe(this, this::initNotificationInNotificationHelper);
    }

    private void initNotificationInNotificationHelper(boolean isEnable) {
        notification.configureNotification(isEnable);
    }
}