package com.guillot.go4lunch.list;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.ItemRestaurantBinding;
import com.guillot.go4lunch.maps.RestaurantRepository;
import com.guillot.go4lunch.model.Restaurant;



public class RestaurantListViewHolder extends RecyclerView.ViewHolder {

    private ItemRestaurantBinding binding;
    private Context context;

    RestaurantListViewHolder(@NonNull ItemRestaurantBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
    }

    public void updateRestaurantInfo(Restaurant restaurant){
        binding.textViewName.setText(restaurant.getName());

        binding.textViewAddress.setText(restaurant.getAddress());

        String distanceToDisplay = "0m";
        if(restaurant.getDistance() != null){
            distanceToDisplay = String.format("%sm", restaurant.getDistance().toString());
        }
        binding.textViewDistance.setText(distanceToDisplay);

        Glide.with(context)
                .load(RestaurantRepository.getInstance().getPhotoRestaurant(restaurant.getPhotoReference()))
                .centerCrop()
                .into(binding.imageViewRestaurant);
        Log.d("photo", "photo reference " + restaurant.getPhotoReference());


        LayerDrawable stars = (LayerDrawable) binding.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.orange),
                PorterDuff.Mode.SRC_ATOP);
        binding.ratingBar.setRating(restaurant.getRating());
    }

}