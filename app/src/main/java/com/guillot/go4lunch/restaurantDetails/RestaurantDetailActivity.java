package com.guillot.go4lunch.restaurantDetails;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ActivityDetailsRestaurantBinding;
import com.guillot.go4lunch.main.CoreActivity;
import com.guillot.go4lunch.model.Restaurant;

public class RestaurantDetailActivity extends AppCompatActivity {
    private ActivityDetailsRestaurantBinding binding;
    private RestaurantDetailsViewModel viewModel;
    private String intentRestaurantId;
    private int result;

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

        if (restaurant.getPhotoReference() != null) {
            Glide.with(this)
                    .load(viewModel.getPhoto(restaurant.getPhotoReference()))
                    .centerCrop()
                    .into(binding.imageViewPictureRestaurant);
        } else {
            binding.imageViewPictureRestaurant.setImageResource(R.drawable.marker_restaurant_green_48px);
        }

        binding.textViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + restaurant.getPhoneNumber()));
                startActivity(intent);
            }
        });
        binding.textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.setData(Uri.parse("web:" + restaurant.getWebSite()));
                startActivity(intent);
            }
        });

    }
}