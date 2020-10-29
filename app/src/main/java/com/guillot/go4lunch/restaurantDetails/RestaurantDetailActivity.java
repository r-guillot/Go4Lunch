package com.guillot.go4lunch.restaurantDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;

import com.bumptech.glide.Glide;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ActivityDetailsRestaurantBinding;
import com.guillot.go4lunch.main.CoreActivity;
import com.guillot.go4lunch.model.Restaurant;

import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {
    private ActivityDetailsRestaurantBinding binding;
    private RestaurantDetailsViewModel viewModel;
    private String intentRestaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_restaurant);
        initViewModel();
        viewBinding();

        getInfoRestaurant();
        Log.d("Intent", "intentRestaurantId: " + intentRestaurantId);
    }

    private void viewBinding() {
        binding = ActivityDetailsRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);
    }

    private void getInfoRestaurant() {
        Intent intent = getIntent();
        intentRestaurantId = intent.getStringExtra(CoreActivity.RESTAURANT);
        Log.d("Intent", "intentRestaurantId2: " + intentRestaurantId);

        viewModel.init();
        viewModel.executeNetworkRequest(intentRestaurantId);
        viewModel.getRestaurants().observe(this, this::setGraphicElement);
    }

    private void setGraphicElement(Restaurant restaurant) {
        binding.textViewNameRestaurant.setText(restaurant.getName());
        binding.textViewAddressRestaurant.setText(restaurant.getAddress());

        if (restaurant.getUrlPhoto() != null) {
            Glide.with(this)
                    .load(viewModel.getPhoto(restaurant.getUrlPhoto()))
                    .into(binding.imageViewPictureRestaurant);
        } else {
            binding.imageViewPictureRestaurant.setImageResource(R.drawable.marker_restaurant_green_48px);
        }
    }
}